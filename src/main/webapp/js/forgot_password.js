/**
 * forgot password
 */

    let pwdOk = false;
    let answerOk = false;

    /* 적격검사 메세지용 메서드 */
    function validNotify({ id, color = 'red', text }) {
    	$(id).css('color', color);
		$(id).text(text);
    }

window.onload = () => {
	
	let btn = document.getElementById("btn_submit");
	btn.addEventListener("click", setNewPassword);
	
	$('#answer_password').keyup(function(){
			debounce( function() {
				const isValid = !$('#answer_password').val() == null || !$('#answer_password').val() == '';
				if ( isValid ){
			    	answerOk = true;
			    	switchDisabled();
			    	//console.log('answer keyup: ',answerOk);
			    } else {
			    	answerOk = false;
			    	switchDisabled();
			    }
			}, 200);
		});
		
	$("#password").keyup(function(){
		originPasswordCheck();
	});
	
}


	/* disable 값 변경 용 메서드 */
    function switchDisabled(){	
	
    	const isValid = pwdOk && answerOk;
    	if ( isValid ){
    		$('#btn_submit').attr('disabled', false);
    	} else {
    		$('#btn_submit').attr('disabled', true);
    	}
    }

    /* debounce 메서드 */
    function debounce( cb , ms){
    	let timer;
    	clearTimeout(timer);
		timer = setTimeout( cb , ms );
    }	
	
	/* 비밀번호 형식 검증 */
    function originPasswordCheck(){
    	const originPassword = $("#password").val();
    	
    	const isValidLength = /^[a-zA-Z0-9]{8,20}$/;
		const isJustNum = originPassword.search(/[0-9]/g);
		const isJustEng = originPassword.search(/[a-z]/ig);
		
		if (originPassword == null || originPassword == "") { 
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


	function setNewPassword(){
		//console.log("click");
		const user = {
			'mail': $('#mail').val(),
			'hint': $('#hint_password').val(),
			'answer': $('#answer_password').val(),
			'password': $('#password').val(),
		}
		
		//console.log(user);
		
		$.ajax({
			url: './forgot_password_ok.jsp', 
			type: 'post',
			dataType: 'json',
			data: user,
			success: ( result ) => {
				console.log('ajax 에서 받아온 result : ', result);
				if (result.flag==1){
					alert('비밀번호 변경에 성공했습니다.\n로그인 화면으로 이동합니다.');
					location.href = './sign_in.jsp';
				} else if(result.flag == 0){
					alert('가입 시 입력한 내용과 다릅니다.');
				}
			},
			error: () => {
				console.log('서버 에러');
				alert('시스템 에러');
			}
		});
		
	};