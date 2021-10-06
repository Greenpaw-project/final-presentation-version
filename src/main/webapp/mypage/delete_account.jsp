<%@page import="model1.MypageDAO"%>
<%@page import="model1.MypageTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	
	int flag = 1; //비로그인 상태
	if(session.getAttribute("mail") != null){
		flag = 0; //로그인 상태
		//System.out.println(flag);
	}
	
	String mail = (String)session.getAttribute("mail");
	String nickname = (String)session.getAttribute("nickname");
	
	MypageTO to = new MypageTO();
	to.setMail(mail);
	to.setNickname(nickname);

	MypageDAO dao = new MypageDAO();
	to = dao.getUserInfo(to);
	
	Boolean fromSocial = to.getFromSocial();
	//System.out.println(fromSocial);
	
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GreenPaw</title>
    <link rel="stylesheet" href="../css/reset.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="../css/menu.css" />
    <link rel="stylesheet" href="../css/delet_account.css" />
     <!-- jquery cdn -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
<%
	if(fromSocial == true){
		out.println("<script src='../js/delet_kakao_account.js' defer></script>");
	}else{
		out.println("<script src='../js/delet_account.js' defer></script>");
	}
%>
<%
	if(flag == 1){ //비로그인 시
		out.println("<script type='text/javascript'>");
		out.println("alert('로그인 후 이용 가능합니다.');");
		out.println("location.href='../sign_in.jsp';");
		out.println("</script>");
		out.println("<script src='../js/menu_logOut.js' defer></script>");
	}else{ // 로그인시
		out.println("<script src='../js/menu_logeIn.js' defer></script>");
	}
%>
  </head>
  <body>
    <div class="main">
      <!-- nav bar include -->
 	 <%@ include file="../nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="note">탈퇴하시겠습니까?</div>
        
<% 
	if(fromSocial == true){
        out.print("<div class='password'>");
        out.print("<p class='sub_note'>*탈퇴 시, 동일한 이메일과 닉네임으로<br>재가입하실 수 없습니다.</p>");
        out.print("<button type='submit' id='yes'>네</button>");
        out.print("<button type='submit' id='no'>아니오</button>");
        out.print("</div>");
		
	}else{
        out.print("<div class='password'>");
		out.print("<p>비밀번호를 입력해주세요.</p>");
        out.print("<input type='password' id='password' name='password' />");
        out.print("<br />");
        out.print("<p class='sub_note'>*탈퇴 시, 동일한 이메일과 닉네임으로<br>재가입하실 수 없습니다.</p>");
        out.print("<button type='submit' id='btn_submit'>Submit</button>");
        out.print("</div>");
	}
        
 %>      
      </div>
    </div>
  </body>
</html>
