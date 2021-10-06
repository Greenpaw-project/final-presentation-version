<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
   
<%
request.setCharacterEncoding("utf-8");

// 1. 넘어온 이미지 파일 upload 폴더에 업로드
// 2. 넘어온 이미지 파일을 url 로 반환
String uploadPath = request.getSession().getServletContext().getRealPath("upload"); 

File uploadDirectory = new File(uploadPath);

if(!uploadDirectory.exists()){
	uploadDirectory.mkdirs();
}  

int maxFileSize = 1024 * 1024 * 5; // 5메가
String encType = "utf-8";

MultipartRequest multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
String fileName = multi.getFilesystemName("image");
System.out.println(fileName);

String wholePath = request.getRequestURL().toString();
String servletPath = request.getServletPath();
String tempUploadPath = wholePath.replace(servletPath, "/upload/");


JSONObject json = new JSONObject();

json.put("url", tempUploadPath + fileName);
//response.setCharacterEncoding("utf-8");
out.println(json);

%>
