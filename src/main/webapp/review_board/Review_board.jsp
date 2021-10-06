<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardListTO" %>
<%@ page import="model1.BoardTO" %>
<%@ page import="model1.ThumbDAO" %>
<%@ page import="java.util.ArrayList" %>
<%
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
   	}
	/* ---------- LOGIN ---------- */
	
	int cpage = 1;
	String categorySeq = request.getParameter("categorySeq");
	String field = "title";
	String keyword ="";
	String sort="";
	
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
	// 정렬에 필요한 sort 가져오기
	if(request.getParameter("sort") != null && !request.getParameter("sort").equals("")){
		sort = request.getParameter("sort");
	}
	
	/* ---------- REVIEW_BOARD ---------- */
	

	ThumbDAO dao = new ThumbDAO();
	int recordPerPage = 5; //페이지에 표시할 게시글 개수
	
	BoardListTO listTO = new BoardListTO();
	listTO.setCpage( cpage );
	listTO = dao.boardList(listTO,categorySeq,field,keyword);
	String type = request.getParameter("type"); //type데이터 가져오기
	listTO.setType(type);
	
	int totalRecord = listTO.getTotalRecord();
	int totalPage = listTO.getTotalPage();
	
	int blockPerPage = listTO.getBlockPerPage();
	int startBlock = listTO.getStartBlock();
	int endBlock = listTO.getEndBlock();
	
	StringBuilder sbHtml = new StringBuilder();
	ArrayList<BoardTO> boardLists = listTO.getBoardLists();
	
	if(!field.equals("") && !keyword.equals("")){ 	// 검색값을 입력했을 경우
		boardLists = dao.getSearchList(categorySeq, field, keyword, cpage, recordPerPage);
	} else if(!sort.equals("")){ 					// 정렬 기능 버튼을 눌렀을 때
		boardLists = dao.listSort(categorySeq, field, keyword, sort, cpage, recordPerPage);
	} else { 										// categorySeq=5의 전체 게시글 조회
		boardLists = dao.getList(categorySeq, cpage, recordPerPage);
	}
	
	/* for문 밖에서 선언하는 게 메모리 차지가 덜 한다 */
	String seq = "";
	String subTitle = "";
	String saleStatus = "";
	String title = "";
	String familyMemberType = "";
	String content = "";
	String thumbUrl = "";
	String nickname = "";
	String likeCount = "";
	String hit = "";
	String createdAt = "";
	
	for( BoardTO to : boardLists ) {
		seq = to.getSeq();
		subTitle = to.getSubTitle();
		saleStatus = to.getSaleStatus();
		title = to.getTitle();
		familyMemberType = to.getFamilyMemberType();
		content = to.getContent();
		thumbUrl = to.getThumbUrl();
		nickname = to.getNickname();
		likeCount = to.getLikeCount();
		hit = to.getHit();
		createdAt = to.getCreatedAt();
		
		sbHtml.append("<li class='item'>");
		sbHtml.append("			<div class='family-type'>");
		sbHtml.append("				<span>"+familyMemberType+"</span>");
		sbHtml.append("			</div>");
		sbHtml.append("				<div class='thumb'><a href='./review_view.jsp?cpage="+cpage+"&seq="+seq+"&type="+familyMemberType+"'>");
		sbHtml.append( "						<img src='" + thumbUrl + "'>" );
		sbHtml.append("				</a></div>");
		sbHtml.append("			<div class='list-title_wrap'>");
		sbHtml.append("				<div class='list-sale_Status'>");
		sbHtml.append("					<a class='sale_Status-item'>"+saleStatus+"</a>");
		sbHtml.append("				</div>");
		sbHtml.append("				<div class='list-title'><a href='./review_view.jsp?cpage="+cpage+"&seq="+seq+"&type="+familyMemberType+"'>");
		sbHtml.append("					<span class='list-title-title'>"+title+"</span>");
		sbHtml.append("					<span class='list-title-content'>"+content+"</span>");
		sbHtml.append("				</a></div>");
		sbHtml.append("			</div>");
		sbHtml.append("			<div class='list-info'>");
		sbHtml.append("				<span class='meta-item view-count' title='조회수'>");
		sbHtml.append("					<span class='stats-count'>조회수 "+hit+"</span>");
		sbHtml.append("				</span>");
		sbHtml.append("				<span class='meta-item comment-count' title='댓글'>");
		sbHtml.append("					<span class='stats-count'>댓글 10</span>");
		sbHtml.append("				</span>");
		sbHtml.append("			</div>");
		sbHtml.append("			<div class='list-writer'>");
		sbHtml.append("				<div class='writer-info'>");
		sbHtml.append("					<a>" + nickname + "</a>");
		sbHtml.append("					<span>" + createdAt + "</span>");
		sbHtml.append("				</div>");
		sbHtml.append("			 </div>");
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
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="../css/review_board.css" />
    <link rel="stylesheet" href="../css/menu.css" />
    <!-- 아이콘-->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
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
	
%>s
</head>
  <body>
  	<main>
    <div class="main">
    	<%@ include file="../nav_bar.jsp" %>
    <div class="main-menu">
    </div>
    <div class="main-body">
        <!--배너-->
        <div class="main-body-banner">
            <div class="banner" style="cursor:pointer;" onclick="location.href='./Review_board.jsp?categorySeq=${param.categorySeq}'"></div>
        </div>
        <!-- 검색창 -->
        <div class="search_wrap">
            <form action="./Review_board.jsp" method="get" class="searchform">
                 <input type="hidden" name="categorySeq" value="${param.categorySeq }" />
                 <select name="field" class="selectbox">
                    <option value="title" name="title">제목</option>
					<option value="content" name="content">내용</option>
                </select>
                <input type="text" id="search-input" name="keyword" placeholder="검색"/>
                <input type="submit" value="검색" id="search-btn"/>
            </form>
        </div>

        <!--테이블-->
       
        <div class="inner_content">
                <div class="filter_wrap">
               		<div>
                		<a class="search_filter" href="./Review_board.jsp?categorySeq=${param.categorySeq}&field=${param.field}&keyword=${param.keyword}&sort=seq">최근 순</a>

                		<a class="search_filter" href="./Review_board.jsp?categorySeq=${param.categorySeq}&field=${param.field}&keyword=${param.keyword}&sort=hit">조회 순</a>
                		
                		<a class="search_filter" href="./Review_board.jsp?categorySeq=${param.categorySeq}&field=type&keyword=pet">동물</a>
						
						<a class="search_filter" href="./Review_board.jsp?categorySeq=${param.categorySeq}&field=type&keyword=plant">식물</a>
                	</div>
                </div>
                <div class="list_wrap">
                    <!------ 리스트 -------->
                    <section class="list">
                        <ul>
                            <%=sbHtml %>
                        </ul>
                    </section>
                    <a type="button" href="./review_write.jsp?categorySeq=<%=categorySeq %>" id="write-btn">글쓰기</a>
                <!--페이지-->
                <div class="search_pagination">
                    <nav class="pagination">
                  		<ul>
					<%
						//이전
						if(startBlock == 1){
							out.println("");
						}else{
							out.println("<li class='prev'><a href='?categorySeq="+categorySeq+"&field="+field+"&keyword="+keyword+"&sort="+sort+"&cpage="+(startBlock-blockPerPage)+"'>◀</a></li>");

						};
						//숫자
						for(int i = startBlock; i <= endBlock; i++){
							if(cpage == i){
								out.println("<li><strong>"+i+"</strong></li>");
							}else{
								out.println("<li><a href='?categorySeq="+categorySeq+"&field="+field+"&keyword="+keyword+"&sort="+sort+"&cpage="+i+"'>"+i+"</a></li>");
							};
						};
												                   
						//다음
						if (endBlock == totalPage) {
							out.println("");
						} else {
							out.println("<li class='next'><a href='?categorySeq="+categorySeq+"&field="+field+"&keyword="+keyword+"&sort="+sort+"&cpage="+ (startBlock + blockPerPage)+"'>▶</a></li>");
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