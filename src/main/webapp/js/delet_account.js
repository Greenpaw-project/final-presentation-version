window.onload = () =>{
	let password = document.getElementById("password");
	let btnSubmit = document.getElementById("btn_submit");
	btnSubmit.addEventListener("click", () =>{
		//console.log("click");
		//console.log(password.value);
		deleteAccount(password.value);
	});
};

let deleteAccount = (pwd) => {	
	//console.log(pwd)
	$.ajax({
		url: '../mypage/delete_account_ok.jsp',
		type: 'post',
		dataType: 'json',
		data: {
			'pwd' : pwd 
		},
		success: ( result ) => {
			console.log(result.flag);
			if(result.flag == 1){
				alert('정상적으로 탈퇴되었습니다.');
				location.href='../main.jsp';
			} else if(result.flag == 0){
				alert('비밀번호를 확인해주세요');
			} else {
				alert("what the....");
			}
		},
		error: () => {
			console.log('서버 에러');
			alert('시스템 에러');
		}
		
	})
	
}
	


