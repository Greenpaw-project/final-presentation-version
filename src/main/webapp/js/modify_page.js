/*
* modify
* delete + write
*/



let tmpIndex = 0;
let tmpImg = [];

let lodedImgList = [];

$(() => {
	lodedImgList = getImgList($('#editor').html());
	console.log('lodedImgList : ',lodedImgList);
});



// alert 메세지 꾸밈용 라이브러리 -> toatr
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
	hooks: {
		addImageBlobHook: (blob, cb) => {
			sendImage(blob, (imageUrl) => {
				cb(imageUrl, blob.name);

				// 이미지 저장 될때마다 tmpImg 에 배열로 url 저장
				tmpImg[tmpIndex] = imageUrl;
				//console.log('tmpImg : ', tmpImg);
				tmpIndex++;

			});
			return false;
		}
	}
});

// 글쓰다가 페이지 이동시 경고창 띄우기
$(window).on('beforeunload', function() {
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
		url: "./save_img.jsp",
		contentType: false,
		processData: false,
		success: function(apiRes) {
			//img_upload.jsp 에서 넘겨준 url
			const res = JSON.parse(apiRes);
			callBackFunc(res.url);
		},
		error: function() {
			console.log('save_img.jsp error');
		}
	});
}

// 저장 버튼 메서드 
function clickSaveBtn() {
	// 저장 버튼 눌렀을때는 경고창 안띄우기
	$(window).off('beforeunload');

	const content = editor.getHtml(); // DB 에 넣을 html 내용
	const markdownContent = editor.getMarkdown(); // 섬네일 뽑을려고 받아오는 내용

	const thumbUrl = getImageUrl(markdownContent); //최상단 이미지를 썸네일로
	const finalImgList = getImgList(content); // 최종 저장 이미지 배열

	const mergedList = tmpImg.concat(lodedImgList); // 원래 글에 있던 이미지 + 글쓰면서 로딩됐던 이미지
	let removedImg;
	console.log('-----------');
	if (thumbUrl !=''){
		removedImg = mergedList.filter(x => !finalImgList.includes(x));
	}
	console.log('removedImg : ', removedImg);
	console.log('-----------');

	const isTitleEmpty = $('#title').val() == "" ? true : false;
	const isContentEmpty = content == "" ? true : false;

	const isPrivate = $('#isPrivate').is(':checked') == false ? true : false;
	console.log('isPrivate : ',isPrivate);

	if (isTitleEmpty) {
		// 제목 빈칸일때
		toastr.info('제목을 입력해주세요!');
	} else if (isContentEmpty) {
		// 내용 비었을때
		toastr.info('내용을 입력해주세요!');
	} else if ($('#title').val().length == 51) {
		// 제목 길이 50자 이상일때
		toastr.info('제목은 50자까지만 입력해주세요.');
	} else if (thumbUrl == '') {
		// 썸네일 안넣었을 때
		toastr.info('대표 이미지를 넣어주세요.');
	} else if (!isTitleEmpty && !isContentEmpty) {
		// 빈칸 없을때 ajax 실행 -> DB 로 전송
		$.ajax({
			data: {
				title: $('#title').val(),
				//subTitle: $('#sub_title').val(),
				content: content,
				thumbUrl: thumbUrl,
				familyMemberType: $('input[name=type]:checked').val(),
				isPrivate: isPrivate,
				removedImg: removedImg,
				type: $('#type')[0].value,
				cpage: $('#cpage')[0].value,
				seq: $('#seq')[0].value
			},
			type: "post",
			url: "./personal_album_modify_ok.jsp",
			dataType: "json",
			success: function(result) {
				//write_ok.jsp 에서 넘겨준 flag
				console.log('clickSaveBtn 정상 실행!');
				//console.log('flag : ', result);
				console.log(thumbUrl);
				const flag = result.flag;
				if (flag == 1) {
					alert('글이 등록되었습니다.');
					// 수정 완료된 글로 이동
					location.href = result.redirect;
					
				} else if (flag == 0) {
					alert('글쓰기에 실패했습니다');
				}
			},
			error: function() {
				console.log('write_ok.jsp error');
				alert('시스템 오류');
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
function getImgList(markDownString) {
	// 이미지 url 만 뽑기위한 정규식
	const imgReg = /http?:\/\/[^.]*?(\.[^.]+?)*?\.(jpg|jpeg|gif|png|JPG|JPEG|PNG)/gm;
	let imgList = markDownString.match(imgReg);
	//console.log('imgList : ',imgList);
	return imgList;
}

