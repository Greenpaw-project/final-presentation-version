/**
 * delete
 */


let seq = $("#seq").val();
//console.log(seq);
let category =$('#category').val();
//console.log(category);
let page = $('#page').val();
let nickname = $('#nickname').val();


let deleteBtn = document.getElementById('delete');

deleteBtn.addEventListener('click', function() {
	//이미지 리스트 가져오기
	console.log("click delete");
	const imageList = getImageList($('#content').html());
	
	if(confirm('정말 삭제하시겠습니까?') == true){
		//ajax로 삭제 실행후 list로 돌아가기
		deleteOk( imageList );
	} else {
		location.reload();
	}
});

	function deleteOk( imageList ){
		console.log('imageList : ',imageList);
	$.ajax({
		url:'../normal_board/normal_board_delete.jsp',
		data: {
			seq:seq,
			category:category,
			page:page,
			author:nickname,
			imageList: imageList
		},
		method:'post',
		dataType:'json',
		success:function(result){
			console.log(result.flag);
			if ( result.flag == 1){
				//성공일때
				alert('글삭제에 성공했습니다.');
				location.href='./normal_board1.jsp?category='+result.category+'&page='+result.page;
			} else {
				// 실패일때
				alert('글 삭제에 실패했습니다 ㅠㅠ');
				location.reload();
			}
		},
		error:function( request, status, error ){
    		console.log("status : " , request.status);
			console.log("message : " , request.responseText)
			console.log("error : " , error);
		}
	});
}


function getImageList( content ){
	// 이미지 url 만 뽑기위한 정규식
    const imgReg = /http?:\/\/[^.]*?(\.[^.]+?)*?\.(jpg|jpeg|gif|png|JPG|JPEG|PNG)/gm;
    let imageList = content.match(imgReg);
    //console.log('imageList : ',imageList);
    return imageList;
};
