<%@page import="model1.MypageTO"%>
<%@page import="model1.MypageDAO"%>
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
	
	if(fromSocial == true){
		out.println("<script type='text/javascript'>");
		out.println("alert('카카오를 통해 생성된 계정은 비밀번호를 변경하실 수 없습니다.');");
		out.println("history.back();");
		out.println("</script>");
	}

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
    <link rel="stylesheet" href="../css/password_change.css" />
  <!-- jquery cdn -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
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
    <script src="../js/password_change.js" defer></script>
  </head>
  <body>
    <div class="main">
      <!-- nav bar include -->
      <%@ include file="../nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="note">비밀번호를 변경하시겠습니까?</div>
        <div class="password_change">
          <p>기존 비밀번호</p>
          <input type="password" id="old_password" name="old" />
          <p>신규 비밀번호</p>
          <input type="password" id="new_password" name="new1" />
          <p id="origin-password-message" class="text-danger"></p>
          <p>신규 비밀번호 확인</p>
          <input type="password" id="new_password_check" name="new2" />
          <p id="check-password-message" class="text-danger"></p>
          <br />
          <button type="submit" id="btn_submit" disabled="true">Submit</button>
        </div>
      </div>
    </div>
  </body>
</html>
