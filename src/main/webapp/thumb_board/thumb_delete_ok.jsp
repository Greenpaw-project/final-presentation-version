<%@page import="java.io.File"%>
<%@ page language="java" contentType="plain; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.json.simple.JSONObject"%>

<%@ page import="model1.ThumbDAO"%>
<%@ page import="model1.BoardTO"%>

<%
	String seq = request.getParameter("seq");
	String nickname = (String) session.getAttribute("nickname");
	String categorySeq = "5";
	String author = request.getParameter("author");
	
	String[] imageList = request.getParameterValues("imageList[]");
	System.out.println("imageList : " + imageList);
	if (imageList != null) {
		System.out.println("imageList length : " + imageList.length); 
	
		for (String fileUrl : imageList) {
			String[] splitedFileUrl = fileUrl.split("/");
			String fileName = splitedFileUrl[splitedFileUrl.length - 1];
			System.out.println("fileName : " +fileName);
	
			// 각자 바꿔주기~! (아니면 realPath 쓰기)
			String filePath = request.getRealPath("upload") + "/";
			filePath += fileName;
			//System.out.println("filePath : "+filePath);
	
			File file = new File(filePath);
			if (file.exists()) {
				file.delete(); // 파일이 있으면 삭제 
				System.out.println("파일 삭제 성공");
			} 
		}
	}
	
	System.out.println(seq);
	System.out.println(nickname);
	
	BoardTO to = new BoardTO();
	to.setSeq(seq);
	to.setCategorySeq(categorySeq);
	to.setNickname(nickname);
	
	ThumbDAO dao = new ThumbDAO();
	int flag = dao.thumbdelete(to);
	
	System.out.println(flag);
	
	JSONObject json = new JSONObject();
	json.put("flag", flag);
	
	out.println(json);
%>
    