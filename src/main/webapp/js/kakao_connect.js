Kakao.init('4cebd24dfd28cc7ed62024f347ec60d7'); //발급받은 키 중 javascript키를 사용해준다.
  console.log(Kakao.isInitialized()); // sdk초기화여부판단
 //
  function kakaoLogin() {
      Kakao.Auth.login({
        success: function (response) {
      	scope: 'profile,account_email',
          Kakao.API.request({
            url: '/v2/user/me',
            //카카오 로그인성공
            success: function (response) {
          	  const mail = response.kakao_account.email;
          	  const nickname = response.properties.nickname;
          	  
          	  //console.log(response);
          	 	console.log(mail);
          	  	console.log(nickname);
          	  	
          	  
				$('[name=kakaomail]').attr('value', mail);
				$('[name=kakaonickname]').attr('value',nickname);
			
				document.kakaofrm.submit();

							},
							fail : function(error) {
								console.log(error)
							},
						})
					},
					//Kakao.Auth.login 실패
					fail : function(error) {
						console.log(error)
					},

				})
			}