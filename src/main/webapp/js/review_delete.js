/**
 * delete modal
 */


let seq = document.getElementById('seq').value;
console.log(seq);

const deleteBtn = document.getElementById('delete');

deleteBtn.addEventListener('click', ()=>{
	console.log('delete click!')
	const imageList = getImageList($('.viewer').html());
	console.log('imageList: ',imageList);
	Swal.fire({
	  title: '정말 삭제하시겠습니까?',
	  text: "삭제하시면 되돌릴 수 없습니다.",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: '네 삭제할래요!',
	  cancelButtonText: '취소',
	  showLoaderOnConfirm: true,
	  backdrop: true,
	  preConfirm: () => {
	    return $.ajax({
			url: '../review_board/review_delete_ok.jsp',
			type: 'post',
			data: {
				seq: seq,
				imageList: imageList
			}
			})
			.then( response => {
				return response
			})
			.catch (error => {
				console.log(error);
				swal.showValidationError(
					`An error ocurred: ${error.status}`
				)
			})
		},
		/*fetch(`../review_board/review_delete_ok.jsp?seq=${seq}`)
	      .then(response => {
	        if (!response.ok) {
	          throw new Error(response.statusText)
	        }
	        return response.json()
	      })
	      .catch(error => {
	        Swal.showValidationMessage(
	          '시스템 에러'
	        )
	      })*/
	  allowOutsideClick: () => !Swal.isLoading()
	}).then((result) => {
		console.log(result);
	  if (result.isConfirmed == true) {
	    Swal.fire({
	      title: '삭제되었습니다.'
	    }).then((result)=>{
			if (result.isConfirmed) {
				location.href='../review_board/Review_board.jsp?categorySeq=4' //list 페이지로 이동						
				}
	  		})
		}
	})
});

function getImageList( content ){
	// 이미지 url 만 뽑기위한 정규식
    const imgReg = /http?:\/\/[^.]*?(\.[^.]+?)*?\.(jpg|jpeg|gif|png|JPG|JPEG|PNG)/gm;
    let imageList = content.match(imgReg);
    //console.log('imageList : ',imageList);
    return imageList;
};