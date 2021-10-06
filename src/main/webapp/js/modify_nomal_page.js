/*
* 쓰기 페이지
*
*/

let tmpIndex = 0;
let tmpImg = [];

// 페이지 로딩될때 content 부분에서 이미지 리스트 저장하는 배열
let lodedImgList = [];

// 윈도우 온로드 부분 == 페이지 로딩하면서 바로 실행되는 부분
$(()=>{
	lodedImgList = getImgList($('#editor').html());
	//console.log('lodedImgList: ',lodedImgList);
})


// alert 메세지 꾸밈용 라이브러리 -> toat
toastr.options = {
	"closeButton": false,
	"debug": false,
	"newestOnTop": false,
	"progressBar": true,
	"positionClass": "toast-top-right",
	"preventDuplicates": false,
	"onclick": null,
	"showDuration": "100",
	"hideDuration": "1000",
	"timeOut": "1500",
	"extendedTimeOut": "1000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
};

// 에디터 활성화 메서드
const editor = new toastui.Editor({
	el: document.querySelector('#editor'),
	previewStyle: 'vertical',
	initialEditType: 'wysiwyg',
	height: '85%',
	language: 'ko',
	placeholder: '자유롭게 글을 작성해보세요!',
	hooks: {
		addImageBlobHook: (blob, cb) => {
			sendImage(blob, (imageUrl) => {
				cb(imageUrl, blob.name);
				
				//이미지 저장될떄마다 temp에 배열로 url저장
				tmpImg[tmpIndex] =imageUrl;
				//console.log('tempImg:' , tmpImg);
				tmpIndex++;
				
			});
			return false;
		}
	}
});

// 글쓰다가 페이지 이동시 경고창 띄우기
$(window).on('beforeunload', function () {
	return '작성 중인 글이 있습니다. 페이지를 나가시겠습니까?';
});

// 제목 길이 체크 메서드 
function checkLength(event) {
	if (event.value.length >= event.maxLength) {
		$('#title').css('color', 'red');
	} else {
		$('#title').css('color', 'black');
	}
}

// 에디터 안에 이미지 url 로 전송해주는 메서드
function sendImage(img, callBackFunc) {
	const image = new FormData();
	image.append('image', img);
	
	$.ajax({
		data: image,
		type: "post",
		url: "../normal_board/save_img.jsp",
		contentType: false,
		processData: false,
		success: function (apiRes) {
			//img_upload.jsp 에서 넘겨준 url
			const res = JSON.parse(apiRes);
			callBackFunc(res.url);
		},
		error: function(request,status,error){
        console.log("code:",request.status);
		console.log("message:",request.responseText);
		console.log("error:"+error);

		}
	});
}

// 저장 버튼 메서드 
function clickSaveBtn() {
	// 저장 버튼 눌렀을때는 경고창 안띄우기
	$(window).off('beforeunload');

	const content = editor.getHtml(); // DB 에 넣을 html 내용
	const markdownContent = editor.getMarkdown(); // thumbUrl 만 뽑아내기용 content
	const thumbUrl = getImageUrl(markdownContent); //최상단 이미지를 썸네일로
	//console.log("thumbUrl:",thumbUrl);
	
	
	const finalImgList = getImgList(content);
	const mergedList = tmpImg.concat(lodedImgList);	//원래 글에 있던 이미지 + 글쓰면서 로딩한 이미지
	//console.log("mergedList:",mergedList);
	/*
	const removedImg = mergedList[0] != null ? mergedList.filter(x => !finalImgList.includes(x)):"";
	//console.log('removedImg : ', removedImg);
	*/
	
	let removedImg = "";
	
	if(finalImgList != null){
		removedImg = mergedList[0] != null ? mergedList.filter(x => !finalImgList.includes(x)):"";
	}
	
	if(finalImgList == null){
		removedImg = mergedList;
	}
	console.log('removedImg : ', removedImg);
	
	
	const isTitleEmpty = $('#title').val() == "" ? true : false;
	const isContentEmpty = content == "" ? true : false;

	if (isTitleEmpty) {
		// 제목 빈칸일때
		toastr.info('제목을 입력해주세요!');
	} else if (isContentEmpty) {
		// 내용 비었을때
		toastr.info('내용을 입력해주세요!');
	} else if ($('#title').val().length == 51) {
		// 제목 길이 50자 이상일때
		toastr.info('제목은 50자까지만 입력해주세요.');
	} else if (!isTitleEmpty && !isContentEmpty) {
		// 빈칸 없을때 ajax 실행 -> DB 로 전송(데이터,success 수정)
		$.ajax({
			data: {
				title: $('#title').val(),
				subTitle: $('#sub_title').val(),
				content: content,
				thumbUrl: thumbUrl,
				category: $('#category').val(),
				type: $('input[name="type"]:checked').val(), //동물타입
				removedImg:removedImg,
				seq:$('#seq').val()
			},
			type: "post",
			url: "./modify_nomal_ok.jsp",
			dataType: "json",
			success: function (result) {
				//write_ok.jsp 에서 넘겨준 flag
				//console.log('clickSaveBtn 정상 실행!');
				//console.log('result : ', result);
				
				//console.log($('input[name="type"]:checked').val());
				const flag = result.flag;
				if (flag == 1) {
					alert('글수정에 성공했습니다.');
					location.href='./normal_board_view.jsp?category='+result.category+'&seq='+result.seq; //list 페이지로 이동
					
				} else if (flag == 0) {
					alert('수정에 실패했습니다 ㅠㅠ');
					location.reload();
				}
			},
			error: function () {
				console.log("status : " , request.status);
				console.log("message : " , request.responseText);
				console.log("error : " , error);
			}
		});
	}
}

// content에서 제일 상단 이미지 url 만 뽑아주는 메서드
function getImageUrl(markDownString) {
	let splitIndexList = [0, 0];

	['(', ')'].forEach((key, index) => {
		try {
			const subStringIndex = markDownString.indexOf(key);
			// start 인덱스 요소는 +1 필요
			splitIndexList[index] = index === 0 ? subStringIndex + 1 : subStringIndex;
		} catch (error) {
			console.log(error);
		}
	});
	// splitIndexList 에 0 이 있는지 검사 -> 있으면 true
	const isEmptyImage = splitIndexList.some(index => index === 0);

	// 이미지 경로 못찾으면 빈 공백 응답
	if (isEmptyImage) {
		return '';
	}
	return markDownString.substring(...splitIndexList);
}


// content 에서 이미지 url 만 전부 뽑아 배열로 저장하는 메서드
function getImgList(markDownString){
	// 이미지 url 만 뽑기위한 정규식
	const imgReg = /http?:\/\/[^.]*?(\.[^.]+?)*?\.(jpg|jpeg|gif|png|JPG|JPEG|PNG)/gm;
	let imgList = markDownString.match(imgReg);
	
	return imgList;
}