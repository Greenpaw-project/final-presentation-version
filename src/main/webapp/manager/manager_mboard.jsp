<%@page import="model1.ManagerTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model1.BoardListTO"%>
<%@page import="model1.ManagerDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
int flag = 1; //비로그인 상태
	if(session.getAttribute("mail") != null){
		flag = 0; //로그인 상태
	}
String authTypead = (String)session.getAttribute("authType");
/* ---------- LOGIN ---------- */
int cpage = 1;
String categorySeq = request.getParameter("categorySeq");
String field = "nickname";
String keyword ="";
String startDate = "";
String endDate = "";

if(request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
	cpage = Integer.parseInt(request.getParameter("cpage"));
}
// 검색 부분의 field와 keyword 가져오기
if(request.getParameter("field") != null && !request.getParameter("field").equals("") ){
	field =request.getParameter("field");
}
if( request.getParameter("keyword") != null && !request.getParameter("keyword").equals("")){
	keyword =request.getParameter("keyword");
}
if(request.getParameter("startDate") != null && !request.getParameter("startDate").equals("") ){
	startDate = request.getParameter("startDate");
}

if(request.getParameter("endDate") != null && !request.getParameter("endDate").equals("") ){
	endDate = request.getParameter("endDate");
}
/* ---------- MANAGER_BOARD ---------- */


ManagerDAO dao = new ManagerDAO();
int recordPerPage = 10; //페이지에 표시할 게시글 개수

BoardListTO listTO = new BoardListTO();
listTO.setCpage( cpage );
listTO = dao.boardList(listTO,field,keyword);

if (!startDate.equals("") && !endDate.equals("")) {
	listTO = dao.getPagingByDate(listTO, field, keyword, startDate, endDate);
}

String type = request.getParameter("type"); //type데이터 가져오기
listTO.setType(type);

int totalRecord = listTO.getTotalRecord();
int totalPage = listTO.getTotalPage();

int blockPerPage = listTO.getBlockPerPage();
int startBlock = listTO.getStartBlock();
int endBlock = listTO.getEndBlock();

StringBuilder sbHtml = new StringBuilder();
//ArrayList<ManagerTO> userLists = listTO.getUserLists();
ArrayList<ManagerTO> userLists = dao.getList(cpage, recordPerPage);

if(!field.equals("") && !keyword.equals("")){ 	// 검색값을 입력했을 경우
	userLists = dao.getSearchList(field, keyword, cpage, recordPerPage);
}
if(!startDate.equals("") && endDate.equals("")){
	out.println("<script>");
	out.println("alert('날짜를 다시 입력해주세요')");
	out.println("history.back()");
	out.println("</script>");
	
}

if(startDate.equals("") && !endDate.equals("")){
	out.println("<script>");
	out.println("alert('날짜를 다시 입력해주세요')");
	out.println("history.back()");
	out.println("</script>");
	
}
if(!startDate.equals("") && !endDate.equals("")){
	userLists = dao.dateSearch(startDate, endDate, cpage, recordPerPage );
}
/* 총 회원수와 검색결과 건수 구하기 */
int countlist = dao.getCount();
String countsearch="";

/* for문 밖에서 선언하는 게 메모리 차지가 덜 한다 */
String nickname = "";
String mail = "";
String createdAt = "";
String authType="";
String count = ""; 


	for( ManagerTO to : userLists ) {
		nickname = to.getNickname();
		mail = to.getMail();
		createdAt = to.getCreatedAt();
		authType = to.getAuthType();
		count = to.getCount();
		
		sbHtml.append("<li class='item'>");
		sbHtml.append("	<div class='select_box'>");
		sbHtml.append("		<span><input type='checkbox' name='selectcheck' value='"+nickname+"'/></span>");
		sbHtml.append("	</div>");
		//sbHtml.append("	<div class='list-wrap'>");
		sbHtml.append("		<div class='list-nickname'>");
		sbHtml.append("			<span class='nickname'>"+nickname+"</span>");
		sbHtml.append("		</div>");
		sbHtml.append("		<div class='list-mail'>");
		sbHtml.append("			<span class='mail'>"+mail+"</span>");
		sbHtml.append("		</div>");
		sbHtml.append("		<div class='list-createdAt'>");
		sbHtml.append("			<span class='createat'>"+createdAt+"</span>");
		sbHtml.append("		</div>");
		sbHtml.append("		<div class='list-authType'>");
		sbHtml.append("			<span class='authType'>"+authType+"</span>");
		sbHtml.append("		</div>");
		sbHtml.append("		<div class='list-count'>");
		sbHtml.append("			<span class='count'>"+count+"</span>");
		sbHtml.append("		</div>");
		//sbHtml.append("	</div>");
		sbHtml.append("</li>");
	}
%>

<!DOCTYPE html>
<html lang="UTF-8">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>GreenPaw</title>
<link rel="stylesheet" href="../css/reset.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap"
	rel="stylesheet" />
  <!-- jquery cdn -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
      crossorigin="anonymous"></script>
<link rel="stylesheet" href="../css/manager.css" />
<link rel="stylesheet" href="../css/menu.css" />
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

	if (authTypead != null && !authTypead.equals("관리자")) {
		out.println("<script>");
		out.println("alert('관리자만 사용가능합니다.')");
		out.println("location.href='" + request.getContextPath() + "/main.jsp'");
		out.println("</script>");
	}
%>
<!-- 아이콘-->
<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<script src='../js/manager_mboard.js' defer></script>
<style>
	/*필터 색*/
	.filter_wrap a:link {
	color:#26a69a;
	}

	.filter_wrap a:visited {
	  color:#26a69a;
	}
	
	.filter_wrap a:active {
	  color: #00766c;
	  font-weight: bolder;
	}
	
	.filter_wrap a:hover {
	  color:#00766c;
	  font-weight: bolder;
	}
	
		

</style>

</head>
<body>
	<main>
		<div class="main">
			<%@ include file="../nav_bar.jsp" %>
			<div class="main-menu">
			</div>
			<div class="main-body">
				<!-- 검색창 -->
				<div class="search_wrap">
					<form action="./manager_mboard.jsp" method="get" class="searchform">
						<select name="field" class="selectbox">
							<option value="u.nickname" name="nickname">닉네임</option>
							<option value="u.mail" name="mail">메일</option>
						</select>
						<input type="text" id="search-input" name="keyword" placeholder="검색" />
						<input type="submit" value="검색" id="search-btn" />
					</form>
				</div>
				<!-- 날짜 검색창 -->
				<div class ="dateSearch">
					<form action="./manager_mboard.jsp" method="get">
						<input type="date" name="startDate" id="startDate" value="${param.startDate}"/>
						~
						<input type="date" name="endDate" id="endDate" value="${param.endDate}"/>
						<input type="submit" value="검색" id="date-btn" />
					</form>
				</div>
				<!-- 테이블 -->

				<div class="inner_content">
					<span>회원 관리</span>
					<p>[총 회원수 <%=countlist%>명] 검색결과 <%=totalRecord%>건</p>
					<div class="filter_wrap">
						<div class="filter_item">
							<div class="getcheckbox">
								<input type="checkbox" name="selectcheckall" value="selectall" onclick="selectAll(this)"/>
							</div>
							<div class="getnickname">
								<span>닉네임</span>
							</div>
							<div class="getmail">
								<span>이메일</span>
							</div>
							<div class="getcreatat">
								<span>가입일자</span>
							</div>
							<div class="getautytype">
								<span>구분</span>
							</div>
							<div class="getcount">
								<span>게시글수</span>
							</div>
							
							<!-- 나중에 댓글 부분 추가하기 -->
							
						</div>
					</div>
					<!------ 리스트 -------->
					<div class="list_wrap">
						<section class="list">
							<ul>
								<%=sbHtml %>
							</ul>
						</section>
						<select name="statusOption" class="selectbox">
							<option value="탈퇴" name="status">탈퇴</option>
							<option value="차단" name="status">차단</option>
							<option value="회원" name="status">회원</option>
							<option value="관리자" name="status">관리자</option>
						</select>
						<a href="#" id="change" class="change" onclick="changeClick()">변경</a>
						
						<!--페이지-->
						<div class="search_pagination">
							<nav class="pagination">
								<ul>
						<%
						//이전
						if(startBlock == 1){
							out.println("");
						}else{
							out.println("<li class='prev'><a href='?field="+field+"&keyword="+keyword+"&cpage="+(startBlock-blockPerPage)+"&startDate="+startDate+"&endDate="+endDate+"'>◀</a></li>");

						};
						//숫자
						for(int i = startBlock; i <= endBlock; i++){
							if(cpage == i){
								out.println("<li><strong>"+i+"</strong></li>");
							}else{
								out.println("<li><a href='?field="+field+"&keyword="+keyword+"&cpage="+i+"&startDate="+startDate+"&endDate="+endDate+"'>"+i+"</a></li>");
							};
						};
												                   
						//다음
						if (endBlock == totalPage) {
							out.println("");
						} else {
							out.println("<li class='next'><a href='?field="+field+"&keyword="+keyword+"&cpage="+ (startBlock + blockPerPage)+"&startDate="+startDate+"&endDate="+endDate+"'>▶</a></li>");
						};
					%>
								</ul>
							</nav>
						</div>
						<!--페이지 끝-->
					</div>
				</div>
			</div>
		</div>
		<!-- 메인-->
	</main>

</body>
</html>