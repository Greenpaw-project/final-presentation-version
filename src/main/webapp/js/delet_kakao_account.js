window.onload = () =>{
	let btnYes = document.getElementById("yes");
	let bynNo = document.getElementById("no");
	
	//카카오 계정 탈퇴 yes
	btnYes.addEventListener("click", () =>{
		deleteKakaoAccount();
	});
	
	//카카오 계정 탈퇴 no
	bynNo.addEventListener("click", () =>{
		alert("마이페이지로 돌아갑니다.");
		history.back();
	});
};

let deleteKakaoAccount = () => {	
	$.ajax({
		url: '../mypage/kakao_delete_account_ok.jsp',
		type: 'post',
		dataType: 'json',
		success: ( result ) => {
			console.log(result.flag);
			if(result.flag == 1){
				alert('정상적으로 탈퇴되었습니다.');
				location.href='../main.jsp';
			} else {
				alert("탈퇴에 실패하였습니다");
			}
		},
		error: () => {
			console.log('서버 에러');
			alert('시스템 에러');
		}
		
	})
	
}
	


