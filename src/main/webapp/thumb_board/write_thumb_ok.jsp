<%@page import="model1.BoardDAO"%>
<%@page import="model1.BoardTO"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/json; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
    
<%
request.setCharacterEncoding("utf-8");

// 넘어온 파라미터로 디비에 insert 
String title = request.getParameter("title");
String saleStatus = request.getParameter("saleStatus");
String content = request.getParameter("content");
String thumbUrl = request.getParameter("thumbUrl");
String nickname = (String)session.getAttribute("nickname");
String familyMemberType = request.getParameter("type");
Boolean isPrivate = false;
String categorySeq = "5";

System.out.println("saleStatus: "+saleStatus);
System.out.println("세션 메일: "+session.getAttribute("mail"));
System.out.println("세션 닉네임: "+session.getAttribute("nickname"));
System.out.println("세션 권한: "+session.getAttribute("authType"));

if (request.getParameter("categorySeq") != null && !request.getParameter("categorySeq").equals("") ){
	categorySeq = request.getParameter("categorySeq");
	System.out.println("categorySeq : "+categorySeq);
}
if (request.getParameter("familyMemberType") != null && !request.getParameter("familyMemberType").equals("") ){
	familyMemberType = request.getParameter("familyMemberType");
} 
System.out.println("패밀리타입"+familyMemberType);
 if (request.getParameter("saleStatus") != null && !request.getParameter("saleStatus").equals("") ){
	saleStatus = request.getParameter("saleStatus");
}

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
to.setCategorySeq(categorySeq); 
to.setSaleStatus(saleStatus);
to.setFamilyMemberType(familyMemberType);
to.setTitle(title);
to.setContent(content);
to.setThumbUrl(thumbUrl);
to.setNickname(nickname);
to.setIsPrivate(false);

System.out.println("writeOK"+categorySeq);
System.out.println("writeOK"+saleStatus);
System.out.println("writeOK"+familyMemberType);
System.out.println("writeOK"+title);
System.out.println("writeOK"+content);
System.out.println("writeOK"+thumbUrl);
System.out.println("writeOK"+nickname);


BoardDAO dao = new BoardDAO();
int flag = dao.writeOk(to); 

JSONObject json = new JSONObject();
// 일단 성공한 척 넣기
json.put("flag", flag);

out.println(json);

%>
