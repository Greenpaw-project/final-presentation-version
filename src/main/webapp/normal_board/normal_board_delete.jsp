
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.NormalBoardDAO"%>
<%@page import="model1.BoardTO"%>
<%
request.setCharacterEncoding("utf-8");

String seq = request.getParameter("seq");
String category =request.getParameter("category");
String pages = request.getParameter("page");
String nickname = (String)session.getAttribute("nickname");
		
// ajax 에서 넘겨준 이미지 배열
String[] imageList = request.getParameterValues("imageList[]");

if (imageList != null){
	System.out.println("image List length: "+imageList.length);
	
	for(String fileUrl : imageList){
		// url 을 / 단위로 잘라서, 제일 마지막의 파일명만 가져오기 
		// ex) http://localhost8080/test/test.png 
		String[] splitedUrl = fileUrl.split("/");
		String fileName = splitedUrl[splitedUrl.length-1]; // 배열 길이의 -1 이 마지막 인덱스 == 파일명
		
		// 파일이 들어있는 경로 지정
		String filePath = request.getRealPath("upload")+"/";
		filePath += fileName; // ~~~경로/파일명 
		System.out.println("file path : "+filePath);
		
		File file = new File(filePath); // 위의 경로에 파일 처리 하기위한 파일객체 만들기
		if ( file.exists() ){
			file.delete(); // 파일이 있으면 지워줘 
			System.out.println("파일 삭제 성공!");
		}
	}
}

BoardTO to = new BoardTO();
to.setSeq(seq);
to.setNickname(nickname);

NormalBoardDAO dao = new NormalBoardDAO();
int flag = dao.normalBoardDelete(to);
System.out.println(flag);

JSONObject json = new JSONObject();
json.put("flag",flag);
json.put("category",category);
json.put("page",pages);

out.println(json);



%>