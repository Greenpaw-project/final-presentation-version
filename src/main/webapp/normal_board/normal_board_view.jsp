<%@page import="model1.NormalBoardDAO"%>
<%@page import="model1.BoardTO"%>
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
	System.out.println("세션 닉네임: "+session.getAttribute(""));
	System.out.println("세션 권한: "+session.getAttribute("authType"));
	System.out.println("세션 저장 카카오이메일:"+session.getAttribute("kakao_email"));
	System.out.println("세션 저장 카카오이메일:"+session.getAttribute("kakao_nickname"));
	*/
	
	
	String sessionNickname = (String)session.getAttribute("nickname");
	String category = request.getParameter("category");
	String seq = request.getParameter("seq");
	String authType = (String)session.getAttribute("authType");
	
	
		BoardTO to = new BoardTO();
		to.setSeq(seq);
		
		NormalBoardDAO dao = new NormalBoardDAO();
	
		int isSeq = dao.isSeq(seq);
		
		
	//뷰 데이터 가져오기..
	to = dao.normalBoardView(to);
	
	String subTitle = to.getSubTitle();
	String title =to.getTitle();
	String nickname= to.getNickname();
	String createDate =to.getCreatedAt();
	String hit = to.getHit();
	String likeCount = to.getLikeCount();
	String content = to.getContent();
	int comment = 0;
	
	/*System.out.println(to.getSubTitle());
	System.out.println(title);
	System.out.println(nickname);
	System.out.println(createDate);
	System.out.println(hit);
	System.out.println(likeCount);
	System.out.println(content); */
	
	//카테고리명 가져오기
	/* CategoryTO categoryTO = new CategoryTO();
	categoryTO.setSeq(category);
	
	categoryTO = dao.getCategoryName(categoryTO);
	String categoryName = categoryTO.getName(); */
	
	
	
	
	
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
<script
  src="https://code.jquery.com/jquery-3.6.0.js"
  integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
  crossorigin="anonymous"></script>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap"
	rel="stylesheet" />
<!--h1 나눔명조 사용-->
<link rel="stylesheet"
	href="https://fonts.googleapis.com/earlyaccess/nanummyeongjo.css"/>
	<!-- toast ui viewer   -->
  <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.min.css" />
<% 
		if(category.equals("1")){
			out.println("<link rel='stylesheet' href='../css/normal_board_view.css' />");
		}
		
		if(category.equals("6")){
			out.println("<link rel='stylesheet' href='../css/QnA_view.css' />");
		}
		
		if(category.equals("7")){
			out.println("<link rel='stylesheet' href='../css/notice_view.css' />");
		}
%>	
	

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


		if(isSeq == 0){
			out.println("<script>");
			out.println("alert('삭제된 게시글입니다.')");
			out.println("location.href='./normal_board1.jsp?category="+category+"'");
			out.println("</script>");
		}

	
		
	%>

</head>
<body>
	<!-- toast viewer cdn -->
    <script src="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.js"></script>
	<main>
		<div class="main">
		<%@ include file="../nav_bar.jsp" %>
			<div class="main-menu">
			<input type="hidden" id="seq" name="seq" value="${param.seq }"/>
			<input type="hidden" id="page" name="page" value="${param.page }"/>
			</div>
			<div class="main-body">
				<!--데이터 넣기-->
                <div class="view-title">
                <%-- <a>[<%= categoryName%>]</a><br/> --%>
                <input type="hidden" id="category" name="category" value="${param.category }"/>
                
                  <p class="sub_title"><%=subTitle %></p>
                    <h1><%=title %></h1>
                    <div class="author">
                      <div class="autor-info">
                      	<%if(category.equals("7")){ %>
                        <span class="by">by </span><a class="nickname" id="nickname" value="관리자">관리자</a>
                        <%} else{%>
                         <span class="by">by </span><a class="nickname" id="nickname" value="<%=nickname %>"><%=nickname %></a>
                        <%} %>
                        <span class="space"></span>
                        <span class="date"><%=createDate %></span>
                      </div>
                      <div class="content-info">
                        <span>조회수 <%=hit %></span>
                       <%--  <span>좋아요 <%=likeCount %></span> --%>
                        <span>댓글 0</span>
                        
                      </div>
                    </div>

                </div>
                <div id="content" class="view-content">
                    <div id="viewer">
                        <%=content %>
                    </div>
                </div>
                <!-- 댓글 -->
                <%if(category !=null && category.equals("6") ){ %>
                <!-- ajax로 보내기.. -->
                <div class="comment-wrap" id="comment-wrap">
	                	<textarea class="comment" name="comment" id="comment"></textarea>
	                	<input class="commentBtn" type="submit" value="댓글쓰기"/>
                </div>
                <%} %>
			</div>
      <div class="go-link">
        <ul class="link-menu">
          <li><a href="./normal_board1.jsp?category=${param.category }&page=${param.page}">-List</a></li>
          
          <%if(nickname != null && nickname.equals(sessionNickname)){%>
          <li><a href="./subTitle_modify.jsp?category=${param.category }&page=${param.page}&seq=${param.seq }">-Modify</a></li>
          <li><a href="#" id="delete" >-Delete</a></li>
          <% }%>
          
        </ul>
      </div>
		</div>
		<!-- 메인-->
	</main>
	 <script type="text/javascript" src="../js/view_page.js"></script>
	<script type="text/javascript" src="../js/delete_normal_view.js"></script>
</body>
</html>