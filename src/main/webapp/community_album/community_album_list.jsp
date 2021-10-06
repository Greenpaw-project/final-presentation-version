<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.util.ArrayList"%>
<%@page import="model1.AlbumTO"%>
<%@page import="model1.AlbumDAO"%>
<%@page import="model1.AlbumListTO"%>

<%
request.setCharacterEncoding("utf-8");
	
   	int flag = 1; //비로그인 상태
   	if(session.getAttribute("mail") != null){
  		flag = 0; //로그인 상태
  		
  		//System.out.println(flag);
   	}
   	
    String mail = (String)session.getAttribute("mail");
	String nickname = (String)session.getAttribute("nickname");
	String authType = (String)session.getAttribute("authType");
	
	StringBuffer sbHtml = new StringBuffer();
	
	AlbumListTO listTO = new AlbumListTO(); //호출 -> 생성자로 초기화
	
	//listTO에 값 넣기
	listTO.setNickname(nickname);	
	if(request.getParameter("cpage") != null){
		listTO.setCpage(Integer.parseInt(request.getParameter("cpage")));
	}
	String type = request.getParameter("type"); //type데이터 가져오기
	String catSeq = "3"; //성장앨범 게시판
	Boolean isPrivate = false;
	
	listTO.setType(type);
	listTO.setCatSeq(catSeq);
	listTO.setIsPrivate(isPrivate);
	
	AlbumDAO dao = new AlbumDAO();
	//데이터 가져오기	
	listTO = dao.communityAlbumList(listTO);	
	
	int cpage = listTO.getCpage();
	int totalRecord = listTO.getTotalRecord();
	int totalPages = listTO.getTotalPages();
	int recordPerPage = listTO.getRecordPerPage();
	int startBlock = listTO.getStartBlock();
	int endBlock = listTO.getEndBlock();
	int blockPerPage = listTO.getBlockPerPage();
	ArrayList<AlbumTO> lists = listTO.getBoardLists();
	
	//String path = "../images/";
	
	for(int i = 0; i<lists.size(); i++){
		AlbumTO to = new AlbumTO();
		to = lists.get(i);
		
		nickname = to.getNickname();
		String seq = to.getSeq();
		String title = to.getTitle();
		String content = to.getContent();
		String thumbUrl = to.getThumbUrl();
		String familyMember = to.getFamilyMember();
		isPrivate = to.getIsPrivate();
		String wDate = to.getwDate();
		String mDate = to.getmDate();
		int hit = to.getHit();
		
		//System.out.println(path+photoUrl);
		
        sbHtml.append("<li class='album_content'>");
        sbHtml.append("<a href='./community_album_view.jsp?cpage="+cpage+"&seq="+seq+"&type="+type+"'>");
        sbHtml.append("<div class='a_photo' style=\"background-image:url('"+ thumbUrl + "' )\";></div>");
        sbHtml.append("<div class='a_text_box'>");
        sbHtml.append("<p class='a_wdate'>"+wDate+"</p>");
        sbHtml.append("<p class='a_title'>"+title+"</p>");
        sbHtml.append("<p class='a_writer'>by. "+nickname+" | view "+ hit +"</p>");
        sbHtml.append("</div>");
        sbHtml.append("</a>");
        sbHtml.append("</li>");
        
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
    <link rel="stylesheet" href="../css/community_album.css" />
    <script src="../js/personal_album.js" defer></script>

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
      <div class="content_wrap">
        <div class="center_wrap">
          <div class="banner"></div>
          <ul class="album_wrap">
        	<!-- 게시물 -->	
 			 <%= sbHtml %>
        	<!-- /게시물 -->
          </ul>
	       	<!-- paging -->
	        <div class="paging">
<%
	       		//forward
	       		if(startBlock == 1){
	       			out.println("<span class='forward'><a>&lt;&lt;</a></span>");
	       		}else{
	       			out.println("<span class='forward'><a href='./community_album_list.jsp?cpage="
	       						+ (startBlock-blockPerPage) +"&type="+type+"'>&lt;&lt;</a></span>");
	       			
	       		};
	       	
	       		//숫자
	       		for(int i = startBlock; i <= endBlock; i++){
	       			if(cpage == i){
	       				out.println("<span class='numbers'><a style='color:tomato'>"+i+"</a></span>");       			
	       			}else{
		       			out.println("<span class='numbers'><a href='./community_album_list.jsp?cpage="+i+"&type="+type+"'>"
	       						+ i +"</a></span>");   				
	       			};
	       		};
	       		
	       		if(endBlock == totalPages){
	       			out.println("<span class='backward'><a>&gt;&gt;</a></span>");       			
	       		}else{
	       			out.println("<span class='backward'><a href='./community_album_list.jsp?cpage="
	       						+ (startBlock + blockPerPage)+"&type="+type+"'>&gt;&gt;</a></span>");       			
	       		};
	
%>
	        </div>
          </div>
        <!-- 우측 메뉴바 -->
        <div class="album_right">
          <ul class="menu">
            <li class="menu_list">
              <a href="./community_album_list.jsp" id="all">all</a>
            </li>
            <li class="menu_list">
              <a href="./community_album_list.jsp?type=plant" id="plantList">plant</a>
            </li>
            <li class="menu_list">
              <a href="./community_album_list.jsp?type=pet" id="petList">pet</a>
            </li>
            <li class="new">
              <a href="./community_album_write.jsp?cpage=<%=cpage %>">new</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </body>
</html>
