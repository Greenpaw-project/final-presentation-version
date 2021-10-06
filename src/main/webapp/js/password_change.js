/**
 * 비밀번호 변경
 */

// 필수입력값들 적격 확인용 변수들 
    let pwdOk = false;
    let pwdCheckOk = false;
	
    /* 적격검사 메세지용 메서드 */
    function validNotify({ id, color = 'red', text }) {
    	$(id).css('color', color);
		$(id).text(text);
    }
    
    /* window onload */
    $(function(){
		let btnSubmit = document.getElementById("btn_submit");
	
		$("#new_password").keyup(function(){
			originPasswordCheck();
		});
		$("#new_password_check").keyup(function(){
			passwordDoubleCheck();
		});
		btnSubmit.addEventListener("click", pwChange);
	});
    /* window onload 끝 */
    
    /* disable 값 변경 용 메서드 */
    function switchDisabled(){
    	const isValid = pwdOk && pwdCheckOk;
    	if ( isValid ){
    		$('#btn_submit').attr('disabled', false);
    	} else {
    		$('#btn_submit').attr('disabled', true);
    	}
    }
    
    /* 비밀번호 체크 메서드 시작 
    	1. 비밀번호 적격검사 메서드 : originPasswordCheck() 
    	2. 비밀번호 확인 메서드 : passwordDoubleCheck()
    */
    
    /* 1 */
    function originPasswordCheck(){
		const oldPassword = $("#old_password").val()
    	const originPassword = $("#new_password").val();
    	
    	const isValidLength = /^[a-zA-Z0-9]{8,20}$/;
		const isJustNum = originPassword.search(/[0-9]/g);
		const isJustEng = originPassword.search(/[a-z]/ig);
		
		if(oldPassword == originPassword){
			validNotify({ id: '#origin-password-message', text: '기존 비밀번호와 동일합니다.'});
			pwdOk = false;
			switchDisabled();
		}else if (originPassword == null || originPassword == "") { 
			validNotify({ id: '#origin-password-message', text: '필수입력 입니다.'});
			pwdOk = false;
			switchDisabled();
		} else if (originPassword.search(/\s/) != -1) {
			//비번 빈칸 포함 안됨 
			validNotify({ id: '#origin-password-message', text: '비밀번호는 공백을 포함할 수 없습니다.'});
			pwdOk = false;
			switchDisabled();
		} else if ( !isValidLength.test(originPassword) ) {
			//비번 영문 및 숫자 8~20자 
			validNotify({ id: '#origin-password-message', text: '비밀번호는 숫자와 영문자 조합으로 8~20자리를 사용해야 합니다.'});
			pwdOk = false;
			switchDisabled();
		} else if ( isJustNum<0 || isJustEng<0 ) {
			//비번은 숫자만 / 영어만 안됨
			validNotify({ id: '#origin-password-message', text: '비밀번호는 숫자와 영문자를 혼용하여야 합니다.'});
			pwdOk = false;
			switchDisabled();
		} else {
			validNotify({ id: '#origin-password-message', color: 'green', text: '사용가능한 비밀번호 입니다.'});
			pwdOk = true; 
			switchDisabled();
		}
    };
    
    /* 2 */
	function passwordDoubleCheck(){
		const originPassword = $("#new_password").val();
		const doubleCheck = $("#new_password_check").val();
		
		if(originPassword != doubleCheck){
			// 비번 & 비번 확인창 값 비교
			validNotify({ id: '#check-password-message', text: '패스워드가 일치하지 않습니다.'});
			pwdCheckOk = false;
			switchDisabled();
		} else if(pwdOk == false){
			validNotify({ id: '#check-password-message', text: '올바른 형식의 비밀번호를 입력해주세요.'});
			pwdCheckOk = false;
			switchDisabled();
		}else{
			validNotify({ id: '#check-password-message', color: 'green', text: '패스워드가 일치합니다.'});
			pwdCheckOk = true;
			switchDisabled();
		}	
	};
	
	/* 비밀번호 체크 메서드 끝 */
	
	function pwChange(){
		//console.log('버튼 클릭');
		const pwd = {
			'oldPassword': $('#old_password').val(),
			'newPassword': $('#new_password').val(),
		}
		
		$.ajax({
			url: '../mypage/password_change_ok.jsp', 
			type: 'post',
			dataType: 'json',
			data: pwd,
			success: ( result ) => {
				console.log(result.flag);
				if (result.flag == 1){
					alert('비밀번호 변경에 성공했습니다.');
					location.href='../mypage.jsp';
				} else if(result.flag == 0){
					alert('비밀번호가 변경에 실패했습니다.\n기존 비밀번호를 확인해주세요');
				}else{
					alert("what the....");
				}
			},
			error: () => {
				console.log('서버 에러');
				alert('시스템 에러');
			}
		});
		
	};