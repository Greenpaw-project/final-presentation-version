
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="model1.UserTO"%>   
    <%
    //닉네임 입력폼
    //카카오체크에서 불러온 값들
    request.setCharacterEncoding("utf-8");
	String email =  (String) session.getAttribute("kakao_email");
	String nickname =  (String) session.getAttribute("kakao_nickname"); 
    
	System.out.println("nickname 확인창");
    System.out.println(email);
    System.out.println(nickname);
    
    //로그인 상태 검증 flag
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
   	}
    
    %>
    <!-- 카카오로 회원가입시 회원가입 폼 이동(닉네임 값) -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GreenPaw</title>
    <link rel="stylesheet" href="../css/reset.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="../css/menu.css" />
    <link rel="stylesheet" href="../css/joinus.css" />
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
    <script src="../js/kakao_nick_check.js"></script>
	    <%
		if(flag == 1){ //비로그인 시
			out.println("<script src='../js/menu_logOut.js' defer></script>");
		}else{ // 로그인시
			out.println("<script type='text/javascript'>");
			out.println("alert('잘못된 접근입니다. 이미 가입한 회원입니다.');");
			out.println("location.href='main.jsp';");
			out.println("</script>");
		}
	%>
  </head>
  <body>
    <div class="main">
      <!-- nav bar include -->
      <%@ include file="../nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="logo">
          <img src="../images/logo2_resize.png" alt="" />
        </div>
        <div class="margin_box"></div>
        <p class="kakao_join">JOIN US</p>
        <form class="join_us_form" action="./kakao_sign_up.jsp" method="post" name="jfrm">
          <p for="nickname">닉네임</p>
          <input type="text" id="nickname" name="nickname" value="<%=nickname %>" />
          <p id="nickname-message" class="form-text text-danger"></p><!-- 오류창 뜨는 곳 -->
          <div class="margin_box"></div>
          <button id="join-btn" type="submit" class="btn_submit" disabled="true">JOIN US</button>
        </form>
      </div>
    </div>
  </body>

</html>