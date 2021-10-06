/*
*
* 매니저 - 글 관리쪽 js 
*
*/

$('#search-btn').click(function(){
	if ($('#search-input').val() == "") {
		alert('검색어를 입력해주세요.');
		histoty.back();
	}
});

$('#removeBtn').click(async function(){
	const seqList = getCheckedSeqList();

	if (seqList.length === 0) {
		alert('삭제할 항목을 선택해 주세요.');
		return;
	}

	const isRemove = await confirm('정말 삭제하시겠습니까?');

	if (isRemove) {
		console.log(seqList);
		// ajax 
		$.ajax({
			url: './deletePost.jsp',
			type: 'get',
			dataType: 'json',
			data: {
				'seqList': seqList
			},
			success: function(result){
				console.log('flag 값 받아오기');
				console.log('ajax 에서 받아온 flag : ', result.flag);
				if (result.flag == 1) {
					alert('글 삭제에 성공했습니다.');
					location.reload();
				}
				if (result.flag == 0) {
					alert('글 삭제에 실패했습니다.');
					location.reload();
				}

			},
			error: function(){
				console.log('서버 에러');
			}
		});
	}
});

$('#hiddenBtn').click(async function(){
	const seqList = getCheckedSeqList();

	if (seqList.length === 0) {
		alert('공개 여부를 설정 할 항목을 선택해 주세요.');
		return;
	}

	const isHidden = await confirm('정말 변경하시겠습니까?');

	if (isHidden) {
		console.log(seqList);
		// ajax 
		$.ajax({
			url: './hiddenPost.jsp',
			type: 'get',
			dataType: 'json',
			data: {
				'seqList': seqList
			},
			success: function(result){
				console.log('flag 값 받아오기');
				console.log('ajax 에서 받아온 flag : ', result.flag);
				if (result.flag == 1) {
					alert('글 공개상태 변경에 성공했습니다.');
					location.reload();
				}
				if (result.flag == 0) {
					alert('글 공개상태 변경에 실패했습니다.');
					location.reload();
				}

			},
			error: function(){
				console.log('서버 에러');
			}
		});
	}
});

$('#all_checked_filter').change(function(e) {

	const isCheked = $(this).is(':checked');
	console.log($(this).is(':checked'));
	$("input[name=board_seq]").each(function() {
		$(this).attr("checked", isCheked);
	});
});

const getCheckedSeqList = function(){
	let result = [];
	$("input[name=board_seq]:checked").each(function() {
		result = [...result, $(this).val()];
	});
	return result;
}

function viewPost(seq) {
	const test = $('#list_' + seq); // 해당 li 영역
	const categoryName = test.find('div:eq(1)').html(); // 카테고리 값 받아오기
	const categoryArr = ['0', '자유게시판', '성장앨범', '아이자랑', '후기', '나눔거래', '질문과 답변','공지사항'];
	const categoryNum = categoryArr.indexOf(categoryName); // 카테고리 seq 매칭

	let url = ''; // 이동시킬 url 생성
	switch (categoryNum) {
		case 1:
			url = '../normal_board/normal_board_view.jsp?category=1&seq=' + seq + '&page=1';
			break;
		case 2:
			url = '../personal_album/personal_album_view.jsp?cpage=1&seq=' + seq;
			break;
		case 3:
			url = '../community_album/community_album_view.jsp?cpage=1&seq=' + seq;
			break;
		case 4:
			url = '../review_board/review_view.jsp?cpage=1&seq=' + seq;
			break;
		case 5:
			url = '../thumb_board/thumb_view.jsp?cpage=1&seq=' + seq;
			break;
		case 6:
			url = '../normal_board/normal_board_view.jsp?category=6&seq=' + seq + '&page=1';
			break;
		case 7:
			url = '../normal_board/normal_board_view.jsp?category=7&seq=' + seq ;
			break;
	}
	location.href = url;
}