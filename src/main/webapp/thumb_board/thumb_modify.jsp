<%@page import="model1.ThumbDAO"%>
<%@page import="model1.BoardTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	int flag = 1; //비로그인 상태
	if (session.getAttribute("mail") != null) {
		flag = 0; //로그인 상태
	
	}
	String contentOwner = (String)session.getAttribute("contentOwner");
	String nickname = (String)session.getAttribute("nickname");

	System.out.println(contentOwner);
	System.out.println(nickname);
	
	/* if(!contentOwner.equals(nickname)){
		out.println("<script type='text/javascript'>");
		out.println("alert('본인의 게시글만 수정할 수 있습니다.');");
		out.println("history.back();");
		out.println("</script>");
	} */
	
	String seq = request.getParameter("seq");
	String cpage = request.getParameter("cpage");
	String categorySeq = "5";
	
	BoardTO to = new BoardTO();
	to.setSeq(seq);
	to.setNickname(nickname);
	to.setCategorySeq(categorySeq);
	
	ThumbDAO dao = new ThumbDAO();
	to = dao.thumbmodify(to);
	
	nickname = to.getNickname();
	String title = to.getTitle();
	String saleStatus = to.getSaleStatus();
	String content = to.getContent();
	String thumbUrl = to.getThumbUrl();
	String hit = to.getHit();
	String familyMemberType = to.getFamilyMemberType();
	String createdAt = to.getCreatedAt();
	String updatedAt = to.getUpdatedAt();
	
	
	System.out.println("modify jsp saleStatus 상태값 : "+ saleStatus);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>GreenPaw</title>

<!-- Toast Editor 2.0.0과 의존성 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.48.4/codemirror.css" />
<link rel="stylesheet" href="https://uicdn.toast.com/editor/2.0.0/toastui-editor.min.css" />

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
<link rel="stylesheet" href="../css/thumb_modify.css" />

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

	<!-- body 에 넣어줘야 함! -->
	<script src="https://uicdn.toast.com/editor/2.0.0/toastui-editor-all.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<!--  끝 -->
	
    <div class="main">
      <!-- nav bar include -->
 	 <%@ include file="../nav_bar.jsp" %>
 	   <div class="content_wrap">
        <div class="center_wrap">
          <div class="view_box_bg">
            <div class="content_outer">
              <div class="content_title">
             		 <select id="sale_Status" class="sale_Status-item">
              			<option <%=saleStatus.equals("판매 중")? "selected":""%>>판매 중</option>
              			<option <%=saleStatus.equals("예약 중")? "selected":""%>>예약 중</option>
              			<option <%=saleStatus.equals("판매 완료")? "selected":""%>>판매 완료</option>
              		 </select>
                <div class="title_outer">
               		 
	                <ul class="type">
	                  <%if(familyMemberType.equals("plant")){ %>
             		   <li class="type_select"><input type="radio" name="type" id="plant" checked value="plant" /><label for="plant">plant</label></li>
            		   <li class="type_select"><input type="radio" name="type" id="pet" value="pet" /><label for="pet">pet</label></li>
               		   <%} else if (familyMemberType.equals("pet")){%>
            		   <li class="type_select"><input type="radio" name="type" id="plant" value="plant" /><label for="plant">plant</label></li>
            		   <li class="type_select"><input type="radio" name="type" id="pet" checked value="pet" /><label for="pet">pet</label></li>
               		 <%} %>
	                  
	                </ul>
	
                	<input class="title_input" id="title" type="text" value="<%=title %>"></input>
              	</div>
                <ul class="other_info">
                	<li>Written by&nbsp;<%= nickname%></li>
                    <li>Created on&nbsp;<%= createdAt%></li>
                    <li>Updated on&nbsp;<%= updatedAt%></li>
                </ul>
              </div>
              <div id="editor" class="viewer">
                <%=content %>
              </div>    
              <input type="hidden" id="seq" name="seq" value="<%=seq %>">
              <input type="hidden" id="cpage" name="cpage" value="<%=cpage %>">
              <input type="hidden" id="type" name="type" value="<%=familyMemberType %>">     
			  <input type="hidden" id="categoryseq" name="categoryseq" value="<%=categorySeq%>">
				 <p class="btn_out">
			      	<button class='btn_save' id='btn_modify' onclick="clickSaveBtn()">Save!</button>
	             </p>            
            </div>
          </div>
        <!-- 우측 메뉴바 -->
        <div class="album_right">
          <ul class="menu">
            <li class="menu_list">
              <a href="#" id="back" onclick="history.back();">back</a>
            </li>
            <li class="menu_list">
              <a href="#" id="delete">delete</a>
            </li>
            <li class="list">
              <a href="./Thumbnail_board.jsp?categorySeq=<%=categorySeq%>type=<%=familyMemberType%>&cpage=<%=cpage%>">list</a>
            </li>
            <li class="new">
              <a href="./thumb_write.jsp">new</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
    </div>
  </body>
  <!-- toast ui editor JS -->
  <!-- 꼭 editor 사용할 div 보다 아래에 넣어줘야 함 -->
  <script src="../js/thumb_modify.js" defer></script>
  
  	<!-- delete module -->
	<script type="text/javascript" src="../js/thumb_delete.js"></script>

  

</html>
 	 