<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
  		
  		//System.out.println(flag);
   	}
   	
   //세션 확인
   /*
    System.out.println("세션 메일: "+session.getAttribute("mail"));
	System.out.println("세션 닉네임: "+session.getAttribute("nickname"));
	System.out.println("세션 권한: "+session.getAttribute("authType"));
	System.out.println("세션 저장 카카오이메일:"+session.getAttribute("kakao_email"));
	System.out.println("세션 저장 카카오이메일:"+session.getAttribute("kakao_nickname"));
	*/

%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GreenPaw</title>
    <link rel="stylesheet" href="./css/reset.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="./css/main.css" />
    <link rel="stylesheet" href="./css/menu.css" />
    	<%
		if(flag == 1){ //비로그인 시
			out.println("<script src='./js/menu_logOut.js' defer></script>");
		}else{ // 로그인시
			out.println("<script src='./js/main_logIn.js' defer></script>");
			out.println("<script src='./js/menu_logeIn.js' defer></script>");
		}
	
	%>
   
  </head>
  <body>
    <div class="main">
      <div class="full_page_visual"></div>
      <!-- nav bar include -->
      <%@ include file="./nav_bar.jsp" %>
      
      <a href="./sign_in.jsp" id="btnSignIn"><div class="btn_sign_in">Log In</div></a>
    </div>
  </body>
</html>