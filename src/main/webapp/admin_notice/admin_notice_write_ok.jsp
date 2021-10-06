<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@page import="model1.BoardTO"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.BoardDAO"%>
<%
request.setCharacterEncoding("utf-8");
System.out.println("카테고리 명:"+(String)request.getParameter("category"));
String authType = (String)session.getAttribute("autyType");


// 넘어온 파라미터로 디비에 insert 
String category = request.getParameter("category");
String title = request.getParameter("title");
String content = request.getParameter("content");
String thumbUrl = request.getParameter("thumbUrl");
String nickname = (String)session.getAttribute("nickname");
String familyMemberType = request.getParameter("type");

//String nickname = "닉네임";
 String subTitle = ""; //subTitle있는것 없는것이 있어서, 있으면 나타나도록 수정.. 
 if (request.getParameter("subTitle") != null && !request.getParameter("subTitle").equals("") ){
	 subTitle = request.getParameter("subTitle");
	//System.out.println("카테고리"+category);
} 

 //공개여부
 String isPrivateString = request.getParameter("isPrivate");
	Boolean isPrivate = true;
	if(isPrivateString.equals("false")){
		isPrivate = false;
	}
 

String saleStatus = "";
if (request.getParameter("saleStatus") != null && !request.getParameter("saleStatus").equals("") ){
	saleStatus = request.getParameter("saleStatus");
}

/* System.out.println(title);
System.out.println(subTitle);
System.out.println(content);
System.out.println(thumbUrl);
System.out.println(nickname);
System.out.println(category);
System.out.println(familyMemberType);
System.out.println(saleStatus); */


//사진파일 바뀐부분 적용
	String[] removedImg = request.getParameterValues("removedImg[]");
	if(removedImg != null){
		//System.out.println("removedImg length : "+ removedImg.length);
		
		for(String fileUrl : removedImg){
			String[] splitedFileUrl = fileUrl.split("/");
			String fileName = splitedFileUrl[splitedFileUrl.length -1];
			
			System.out.println(fileName);
			
			String filePath = request.getRealPath("upload")+"/";
			filePath += fileName;
			
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
				System.out.println("삭제 성공");
			}
		}
	}

BoardTO to = new BoardTO();
to.setCategorySeq(category); 
to.setTitle(title);
to.setSubTitle(subTitle);
to.setContent(content);
to.setThumbUrl(thumbUrl);
to.setNickname(nickname);
to.setFamilyMemberType(familyMemberType);
to.setSaleStatus(saleStatus);
to.setIsPrivate(isPrivate); //추가..

BoardDAO dao = new BoardDAO();
int flag = dao.writeOk(to); 

JSONObject json = new JSONObject();
// 일단 성공한 척 넣기
json.put("flag", flag);
json.put("category",category);


out.println(json);

%>