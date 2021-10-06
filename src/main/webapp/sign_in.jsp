<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
   	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GreenPaw</title>
    <link rel="stylesheet" href="./css/reset.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="./css/signIn.css" />
    <link rel="stylesheet" href="./css/menu.css" />
    <%
    	//로그인되어있는지 확인	
    
		if(flag == 1){ //비로그인 시
			out.println("<script src='./js/menu_logOut.js' defer></script>");
		}else{ // 로그인시
			out.println("<script type='text/javascript'>");
			out.println("alert('잘못된 접근입니다. 이미 로그인 하셨습니다.');");
			out.println("location.href='main.jsp';");
			out.println("</script>");
		}
	
	%>
    <!-- 카카오 cdn 추가 -->
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    <!-- ajax 추가 -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  </head>
  <body>
  	<!-- 카카오 연결 스크립트 추가-->
	<script src="./js/kakao_connect.js"></script>
    <div class="main">
     <!-- nav bar include -->
      <%@ include file="./nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="logo">
          <img src="./images/logo2_resize.png" alt="" />
          <p>Welcome to GreenPaw</p>
        </div>
        <form class="log_in_form" action="login_ok.jsp" method="post">
          <p>Email</p>
          <input type="text" id="email" name="email" />
          <p>Password</p>
          <input type="password" id="password" name="password" />
          <br />
          <button type="submit" id="btn_submit">Log in</button>
          <p class ="fp"><a href='./forgot_password.jsp'>Forgot Password?</a></p>
        </form>
       <!-- 카카오 정보를 담을 것 추가한 부분 -->
        <form action="./kakao/kakao_check.jsp" method="post" name="kakaofrm">
        <input type="hidden" name="kakaomail" value=""/>
        <input type="hidden" name="kakaonickname" value=""/>
       </form>
        <!-- 카카오 정보담는 것 끝  -->
       <div class="divider"><span>Join Us</span></div>
        <div class="join">
          <a href="./join_us.jsp" id="createAccount">Create Account</a>
          <span>or</span>
          <!-- 이걸 클릭하면 카카오 시작-수정함 -->
          <a href="javascript:kakaoLogin()" id="kakaoAccount">&nbsp;</a>
        </div>
      </div>
    </div>
  </body>
</html>