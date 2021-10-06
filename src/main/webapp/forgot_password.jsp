<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" href="./css/menu.css" />
    <link rel="stylesheet" href="./css/forgot_password.css" />
    <script src='./js/menu_logOut.js' defer></script>
     <!-- jquery cdn -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
    <script src='./js/forgot_password.js' defer></script>
  </head>
  <body>
    <div class="main">
      <!-- nav bar include -->
      <%@ include file="./nav_bar.jsp" %>
      <div class="left"></div>
      <div class="right">
        <div class="note">비밀번호를 변경합니다.</div>
        <div class="password_change">
          <p>가입하신 이메일를 입력해주세요.</p>
          <input type="mail" id="mail" name="mail" />
          <p>비밀번호 확인 질문을 선택해주세요.</p>
          <select class="hint" id="hint_password">
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
          <p>가입시 입력하신 답변을 입력해주세요.</p>
          <input type="text" id="answer_password" />
          <p>새로운 비밀번호를 입력해주세요.</p>
          <input type="password" id="password" />
          <p id="origin-password-message" class="text-danger"></p>
          <br />
          <button type="submit" id="btn_submit" disabled="true">Submit</button>
        </div>
      </div>
    </div>
  </body>
</html>
