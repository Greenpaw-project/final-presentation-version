<%@ page language="java" contentType="plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.File"%>

<%@page import="model1.AlbumDAO"%>
<%@page import="model1.AlbumTO"%>
    
<%
    request.setCharacterEncoding("utf-8");
        	
        	// 넘어온 파라미터로 디비에 update 
        	String title = request.getParameter("title");
        	String content = request.getParameter("content");
        	String thumbUrl = request.getParameter("thumbUrl");
        	String nickname = (String)session.getAttribute("nickname");
        	Boolean isPrivate = false;       	
        	String familyMemberType = request.getParameter("familyMemberType");
        	String seq = request.getParameter("seq");
        	int hit = 0;
        	int likeCount = 0;
        	String catSeq = "3"; //아이자랑 게시판
        	String saleStatus = null;
        	String subTitle = request.getParameter("familyMemberType");
        	

        	// 삭제해야하는 파일 배열
        	String[] removedImg = request.getParameterValues("removedImg[]");
        	if (removedImg != null) {
        		//System.out.println("removedImg length : "+ removedImg.length);

        		for (String fileUrl : removedImg) {
        			String[] splitedFileUrl = fileUrl.split("/");
        			String fileName = splitedFileUrl[splitedFileUrl.length - 1];

        			// 각자 바꿔주기~! (아니면 realPath 쓰기)
        			String filePath = request.getRealPath("upload") + "/";
        			filePath += fileName;
        			//System.out.println("filePath : "+filePath);

        			File file = new File(filePath);
        			if (file.exists()) {
        		file.delete(); // 파일이 있으면 삭제 
        			}
        		}
        	}

        	/* System.out.println(title);
        	System.out.println(content);
        	System.out.println(thumbUrl);
        	System.out.println(nickname);
        	System.out.println(familyMemberType);
        	*/
        	System.out.println("is private Boolean: " + isPrivate);

        	AlbumTO to = new AlbumTO();
        	to.setTitle(title);
        	to.setContent(content);
        	to.setThumbUrl(thumbUrl);
        	to.setNickname(nickname);
        	to.setFamilyMember(familyMemberType);
        	to.setIsPrivate(isPrivate);
        	to.setHit(hit);
        	to.setCatSeq(catSeq);
        	to.setLikeCount(likeCount);
        	to.setSaleStatus(saleStatus);
        	to.setSubTitle(subTitle);
        	to.setSeq(seq);

        	// 글 수정 성공시 location.href 용 url 만들기
        	String type = request.getParameter("type");
        	String cpage = request.getParameter("cpage");

        	String redirectUrl = "./community_album_view.jsp?type=" + type + "&cpage=" + cpage + "&seq=" + seq;
        	System.out.println("redirectUrl : " + redirectUrl);

        	AlbumDAO dao = new AlbumDAO();
        	int flag = dao.modifyOk(to);
        	System.out.println(flag);

        	JSONObject json = new JSONObject();
        	json.put("flag", flag);
        	json.put("seq", to.getSeq());
        	json.put("redirect", redirectUrl);

        	out.println(json);
    %>
