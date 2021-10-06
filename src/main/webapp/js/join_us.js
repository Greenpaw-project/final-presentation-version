/**
 * 회원가입
 */
// 필수입력값들 적격 확인용 변수들 
    let nickOk = false;
    let mailOk = false;
    let pwdOk = false;
    let checkOk = false;
    let answerOk = false;
	
    /* 적격검사 메세지용 메서드 */
    function validNotify({ id, color = 'red', text }) {
    	$(id).css('color', color);
		$(id).text(text);
    }
    
    /* debounce 메서드 */
    function debounce( cb , ms){
    	let timer;
    	clearTimeout(timer);
		timer = setTimeout( cb , ms );
    }
    
    /* window onload */
    $(function(){

		$("#check_password").keyup(function(){
			passwordDoubleCheck();
		});
		
		$("#password").keyup(function(){
			originPasswordCheck();
		});
		
		$('#nickname').keyup( function(){
			debounce(nickname_check, 400);
		});
		$('#mail').keyup (function(){
			debounce(mail_check, 400);
		});
		
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
		
		$('#info_agree').click(function(){
			const checked = $('#info_agree').is(':checked');
			if (checked) {
				checkOk = true;
				switchDisabled();
			} else {
				checkOk = false;
				switchDisabled();
			};
			//console.log(checkOk);
		});
		
		agreeBox();
	
	});
    /* window onload 끝 */
    
    /* disable 값 변경 용 메서드 */
    function switchDisabled(){
    	//console.log('switchDisabled');
    	const isValid = nickOk && mailOk && pwdOk && answerOk && checkOk;
    	const disabled = $('#join_btn')[0].disabled;
    	if ( isValid ){
    		//console.log('정상',disabled);
    		$('#join_btn').attr('disabled', false);
    	} else {
    		//console.log('중복',disabled);
    		$('#join_btn').attr('disabled', true);
    	}
    }
    
    /* 비밀번호 체크 메서드 시작 
    	1. 비밀번호 적격검사 메서드 : originPasswordCheck() 
    	2. 비밀번호 확인 메서드 : passwordDoubleCheck()
    */
    
    /* 1 */
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
    
    /* 2 */
	function passwordDoubleCheck(){
		const originPassword = $("#password").val();
		const doubleCheck = $("#check_password").val();
		
		if(originPassword != doubleCheck){
			// 비번 & 비번 확인창 값 비교
			validNotify({ id: '#check-password-message', text: '패스워드가 일치하지 않습니다.'});
			pwdOk = false;
			switchDisabled();
		} else{
			validNotify({ id: '#check-password-message', color: 'green', text: '패스워드가 일치합니다.'});
			pwdOk = true;
			switchDisabled();
		}	
	};
	/* 비밀번호 체크 메서드 끝 */
	
	/* 닉네임 체크 메서드 시작 */
	function nickname_check(){
		//console.log('닉네임 체크');
		const nickname = $('#nickname').val();
		let nickLength = 0;
		
		const specialCheck = /[`~!@#$%^&*|\\\'\";:\/?]/gi;
		
		for(let i=0; i< nickname.length; i++){ 
			//한글은 2, 영문은 1로 치환 
			const nick = nickname.charAt(i); 
			if( escape( nick ).length > 4){ 
				nickLength += 2; 
			} else { 
				nickLength += 1; 
			}
		};
		
		if (nickname == null || nickname == "") { 
			validNotify({ id: '#nickname-message', text: '필수입력 입니다.'});
			nickOk = false;
			switchDisabled();
		} else if (nickname.search(/\s/) != -1) {
			//닉네임 빈칸 포함 안됨 
			validNotify({ id: '#nickname-message', text: '닉네임은 공백을 포함할 수 없습니다.'});
			nickOk = false;
			switchDisabled();
		} else if (nickLength<2 || nickLength>20) {
			//닉네임 한글 1~10자, 영문 및 숫자 2~20자 
			validNotify({ id: '#nickname-message', text: '닉네임은 한글 1~10자, 영문 및 숫자 2~20자 입니다.'});
			nickOk = false;
			switchDisabled();
		} else if (specialCheck.test(nickname)) {
			//닉네임 특수문자 포함 안됨 
			validNotify({ id: '#nickname-message', text: '닉네임은 특수문자를 포함 할 수 없습니다.'});
			nickOk = false;
			switchDisabled();
		} else {
			//닉네임 중복확인 
			$.ajax({
				url: './getNickname.jsp', 
				type: 'get',
				dataType: 'json',
				data: {
					'nickname': nickname
				},
				success: ( flag ) => {
					//console.log('flag 값 받아오기');
					//console.log('ajax 에서 받아온 flag : ',flag);
					if( flag == 0){
						validNotify({ id: '#nickname-message', text: '중복된 닉네임입니다.'});
						nickOk = false;
						switchDisabled();
					}else{
						validNotify({ id: '#nickname-message', color: 'green', text: '사용가능한 닉네임입니다.'});
						nickOk = true;
						switchDisabled();
					}
				},
				error: () => {
					console.log('서버 에러');
				}
			});
		}
		switchDisabled();
	};
	/* 닉네임 체크 메서드 끝 */
	
	/* 메일 체크 메서드 시작 */
	function mail_check(){
		//console.log('메일 체크');
		const mail = $('#mail').val();
		const mailCheck = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		
		if ( !mailCheck.test(mail) ){
			// 메일 형식 아닐때
			validNotify({ id: '#mail-message', text: '잘못된 메일 형식입니다.'});
			mailOk = false;
			switchDisabled();
		} else if(mail.length>70){
			validNotify({ id: '#mail-message', text: '70글자 이내의 메일을 입력해주세요'});
			mailOk = false;
			switchDisabled();
		}else {
			// 중복검사
			$.ajax({
				url: './getMail.jsp', 
				type: 'get',
				dataType: 'json',
				data: {
					'mail': mail
				},
				success: ( flag ) => {
					//console.log('flag 값 받아오기');
					//console.log('ajax 에서 받아온 flag : ',flag);
					if( flag == 0){
						validNotify({ id: '#mail-message', text: '중복된 메일입니다.'});
						mailOk = false;
						switchDisabled();
					}else{
						validNotify({ id: '#mail-message', color: 'green', text: '사용가능한 메일입니다.'});
						mailOk = true;
						switchDisabled();
					}
				},
				error: () => {
					console.log('서버 에러');
				}
			});
		}
		switchDisabled();
	};
	/* 메일 체크 메서드 끝 */
	
	
    /* 개인정보 모달 관련 메서드 */
	function agreeBox(){
		$('#dialog-form').dialog({
			autoOpen: false,
			modal: true,
			width:350,
			height:300,
			resizable:false,
			buttons:{
				'확인': function(){
					console.log('확인 버튼 클릭');
					//$('#info_agree').attr('checked',true);
					$(this).dialog('close');
				}
			}
		})
		
		$(".ui-dialog-titlebar").css({
			"background":"#658361",
			"color": "#fff",
			"font-family": "Noto Sans KR",
			"font-size": "14px"
			});
			
		$(".ui-button").css({
			"width": 60,
			"height":30,
			"font-size": 12,
			"background":"#658361",
			"color": "#fff",
			"outline":"none"
			
		})
		$(".ui-dialog-titlebar-close").css({
			"display":"none"
		})
		$(".text").css({
			"width":300,
			"height":150,
			"font-size":12,
			"line-height":"18px",
			"padding":"5px",
			
		})
		$(".ui-dialog-buttonpane").css({
			"border":"none"
		})
	};
	
	function openAgreeBox(){
		$('#dialog-form').dialog('open');
	}
    
    
	function join(){
		console.log('조인 버튼 클릭');
		const user = {
			'nickname': $('#nickname').val(),
			'mail': $('#mail').val(),
			'password': $('#password').val(),
			'hint': $('#hint_password').val(),
			'answer': $('#answer_password').val()
		}
		
		console.log(user);
		
		$.ajax({
			url: './postUser.jsp', 
			type: 'post',
			dataType: 'json',
			data: user,
			success: ( flag ) => {
				console.log('flag 값 받아오기');
				console.log('ajax 에서 받아온 flag : ',flag);
				
				if (flag==1){
					alert('회원가입에 성공했습니다.');
					location.replace('./sign_in.jsp');
				} else {
					alert('[서버에러] 회원가입에 실패했습니다.');
					history.back();
				}
			},
			error: () => {
				console.log('서버 에러');
				alert('시스템 에러');
			}
		});
		
	};