/**
 * 회원가입
 */
// 필수입력값들 적격 확인용 변수들 
    let nickOk = false;
   
	
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
		$('#nickname').keyup( function(){
			debounce(nickname_check, 400);
		});
	});
    /* window onload 끝 */
    
    /* disable 값 변경 용 메서드 */
    function switchDisabled(){
    	console.log('switchDisabled');
    	const isValid = nickOk;
    	const disabled = $('#join-btn').disabled;
    	if ( isValid ){
    		console.log('정상',disabled);
    		$('#join-btn').attr('disabled', false);
    	} else {
    		console.log('중복',disabled);
    		$('#join-btn').attr('disabled', true);
    	}
    }
    
	/* 닉네임 체크 메서드 시작 */
	function nickname_check(){
		console.log('닉네임 체크');
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
				url: '../getNickname.jsp', 
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
				error: (request,status,error) => {
					console.log("code = "+ request.status);
					console.log(" message = " + request.responseText);
					console.log(" error = " + error);
				}
			});
		}
		switchDisabled();
	};
	/* 닉네임 체크 메서드 끝 */
	
	
   