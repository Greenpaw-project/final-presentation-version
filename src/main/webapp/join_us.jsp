<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
   	}
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
    <link rel="stylesheet" href="./css/joinus.css" />
    <link rel="stylesheet" href="./css/menu.css" />
  <!-- jquery cdn -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
  <!-- jquery-ui cdn -->
	<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <%
		if(flag == 1){ //비로그인 시
			out.println("<script src='./js/menu_logOut.js' defer></script>");
		}else{ // 로그인시
			out.println("<script type='text/javascript'>");
			out.println("alert('잘못된 접근입니다. 이미 가입한 회원입니다.');");
			out.println("location.href='main.jsp';");
			out.println("</script>");
		}
	%>
    <script src='./js/join_us.js' defer></script>
  </head>
  <body>
    <div class="main">
      <!-- nav bar include -->
      <%@ include file="./nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="main_text">
          <p>JOIN US</p>
        </div>
        <div class="join_us_form">
          <p for="nickname">닉네임</p>
          <input type="text" id="nickname" />
          <p id="nickname-message" class="form-text text-danger"></p>
          <p for="mail">이메일</p>
          <input type="text" id="mail"/>
          <p id="mail-message" class="form-text text-danger"></p>
          <p for="password">비밀번호</p>
          <input type="password" id="password" />
          <p id="origin-password-message" class="text-danger"></p>
          <p for="password">비밀번호 확인</p>
          <input type="password" id="check_password" />
          <p id="check-password-message" class="text-danger"></p>

          <p>비밀번호 확인 질문</p>
          <select class="hint" id="hint_password" >
            <option value="hint_01" selected>기억에 남는 추억의 장소는?</option>
            <option value="hint_02">자신의 인생 좌우명은?</option>
            <option value="hint_03">자신의 보물 제1호는?</option>
            <option value="hint_04">가장 기억에 남는 선생님 성함은?</option>
            <option value="hint_05">타인이 모르는 자신만의 신체비밀이 있다면?</option>
            <option value="hint_06">추억하고 싶은 날짜가 있다면?</option>
            <option value="hint_07">받았던 선물 중 기억에 남는 독특한 선물은?</option>
            <option value="hint_08">유년시절 가장 생각나는 친구 이름은?</option>
            <option value="hint_09">인상 깊게 읽은 책 이름은?</option>
            <option value="hint_10">읽은 책 중에서 좋아하는 구절이 있다면?</option>
            <option value="hint_11">자신이 두번째로 존경하는 인물은?</option>
            <option value="hint_12">친구들에게 공개하지 않은 어릴 적 별명이 있다면?</option>
            <option value="hint_13">초등학교 때 기억에 남는 짝꿍 이름은?</option>
            <option value="hint_14">다시 태어나면 되고 싶은 것은?</option>
            <option value="hint_15">내가 좋아하는 캐릭터는?</option>
          </select>
          <p>비밀번호 확인 답변</p>
          <input type="text" id="answer_password"/>
          <br />
          <!-- agree box -->
          <div class="agree_box">
            <input type="checkbox" name="info_agree" id="info_agree" />
            <label for="info_agree">개인정보 제공에 동의합니다.</label>
            <span class="agree_text" id="agree_text" onclick="openAgreeBox()">보기</span>
            <div id="dialog-form" class="dialog-form" title="개인정보 동의">
				<div class="text ui-widget-content ui-corner-all" >
						1. 수집 개인정보 항목 : 메일주소 <br />
						2. 개인정보의 수집 및 이용목적 : 서비스 이용에 따른 본인확인<br />
						3. 개인정보의 이용기간 : 모든 검토가 완료된 후 36개월간 이용자의 조회를 위하여 보관하며, 이후 해당정보를 지체 없이 파기합니다. <br />
						4. 그 밖의 사항은 개인정보취급방침을 준수합니다.
				</div>
				<input type="submit" tabindex="-1" style="position:absolute; top:-1000px" >
			</div>
          </div>
          <div class="agree_info"></div>
          <button type="submit" id="join_btn" class="btn_submit" onclick="join()" disabled="true">JOIN US</button>
        </div>
      </div>
    </div>
  </body>
</html>
