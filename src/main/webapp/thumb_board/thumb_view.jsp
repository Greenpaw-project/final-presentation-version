<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="model1.ThumbDAO"%>
<%@page import="model1.BoardTO"%>

<%

	int flag = 1; //비로그인 상태
	if (session.getAttribute("mail") != null) {
		flag = 0; //로그인 상태
	}
	/* String contentOwner = (String)session.getAttribute("contentOwner"); */
	String nickname = (String)session.getAttribute("nickname");
	String seq = request.getParameter("seq");
	String cpage = request.getParameter("cpage");
	String categorySeq = "5";
	String sessionNickname = (String)session.getAttribute("nickname");
	
	

	BoardTO to = new BoardTO();
	to.setSeq(seq);
	to.setNickname(nickname);
	to.setCategorySeq(categorySeq);

	
	ThumbDAO dao = new ThumbDAO();
	to = dao.thumbview(to);
	
	seq = to.getSeq();
	nickname = to.getNickname();
	String title = to.getTitle();
	String saleStatus = to.getSaleStatus();
	String content = to.getContent();
	String thumbUrl = to.getThumbUrl();
	String familyMemberType = to.getFamilyMemberType();
	String hit = to.getHit();
	Boolean isPrivate = to.isPrivate();
	String createdAt = to.getCreatedAt();
	String updatedAt = to.getUpdatedAt();


%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>GreenPaw</title>

<!-- toast ui viewer   -->
  <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.min.css" />

<!-- Toast Editor 2.0.0과 의존성
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.48.4/codemirror.css" />
<link rel="stylesheet" href="https://uicdn.toast.com/editor/2.0.0/toastui-editor.min.css" /> -->


<!-- jquery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

<!-- toastr -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css" integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g==" crossorigin="anonymous" referrerpolicy="no-referrer" />

<!-- sweet alert -->
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>


<!-- css, font -->
<link rel="stylesheet" href="../css/reset.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet" />
<link rel="stylesheet" href="../css/menu.css" />
<link rel="stylesheet" href="../css/thumb_view.css" />


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

<!-- body 에 넣어줘야 함! 
	<script src="https://uicdn.toast.com/editor/2.0.0/toastui-editor-all.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
-->


<!-- toast viewer cdn -->
	 <script src="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.js"></script>


    <div class="main">
      <!-- nav bar include -->
 	 <%@ include file="../nav_bar.jsp" %>
 	       <div class="content_wrap">
        <div class="center_wrap">
          <div class="view_box_bg">
            <div class="content_outer">
              <div class="content_title">
              	<div>
              		<div class="saleStatus">
              			<span><%=saleStatus %></span>
              		</div>
              		<div class="title_item">
                		<p><%=title %></p>
                	</div>
               	</div>
                <ul class="other_info">
                	<li>Written by&nbsp;<%= nickname%></li>
                	<li>Hit&nbsp;<%= hit%></li>
                    <li>Created on&nbsp;<%= createdAt%></li>
                    <li>Updated on&nbsp;<%= updatedAt%></li>
                </ul>
              </div>
              <div id="viewer" class="viewer">
                <%=content %>
              </div>            
            	<input type="hidden" id="seq" name="seq" value="<%=seq %>">
            </div>
          </div>
        <!-- 우측 메뉴바 -->
        <div class="album_right">
          <ul class="menu">
          <%if(nickname != null && nickname.equals(sessionNickname)){%>
            <li class="menu_list">
              <a href="./thumb_modify.jsp?cpage=<%=cpage%>&seq=<%=seq %>&type=<%=familyMemberType%>" id="edit">edit</a>
            </li>
            <li class="menu_list">
              <a href="#" id="delete">delete</a>
            </li>
            <% }%>
            <li class="list">
              <a href="./Thumbnail_board.jsp?categorySeq=<%=categorySeq%>&type=<%=familyMemberType%>&cpage=<%=cpage%>">list</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </body>
	<!-- toast viewer module -->
	<script type="text/javascript" src="../js/view_page.js"></script>
	<!-- delete module -->
	<script type="text/javascript" src="../js/thumb_delete.js"></script>


</html>
 	 