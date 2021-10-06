<%@page import="java.io.File"%>
<%@ page language="java" contentType="plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>

    
<%
// 1. 넘어온 이미지 파일 upload 폴더에 업로드
// 2. 넘어온 이미지 파일을 url 로 반환

/* String uploadPath = request.getRealPath("upload"); */

String uploadPath = request.getSession().getServletContext().getRealPath("upload"); 

File uploadDirectory = new File(uploadPath);

if(!uploadDirectory.exists()){
	uploadDirectory.mkdirs();
} 

int maxFileSize = 1024 * 1024 * 5; // 5메가
String encType = "utf-8";

MultipartRequest multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
/* String originFileName = multi.getOriginalFileName("image"); */
//System.out.println(uploadPath);
String originFileName = multi.getFilesystemName("image");

String wholePath = request.getRequestURL().toString();
String servletPath = request.getServletPath();
String tempUploadPath = wholePath.replace(servletPath, "/upload/");
//System.out.println(wholePath.replace(servletPath, "/upload/"));

JSONObject json = new JSONObject();
json.put("url", tempUploadPath + originFileName);


out.println(json);

%>
