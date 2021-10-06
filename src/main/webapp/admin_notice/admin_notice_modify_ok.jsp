<%@page import="model1.AdminNoticeDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.BoardTO"%>
   
<%
request.setCharacterEncoding("utf-8");
//System.out.println("카테고리 명:"+(String)request.getParameter("category"));


// 넘어온 파라미터로 디비에 insert 
String category = request.getParameter("category");
String title = request.getParameter("title");
String content = request.getParameter("content");
String thumbUrl = request.getParameter("thumbUrl");
String familyMemberType = request.getParameter("type");
String seq = request.getParameter("seq");

//String nickname = "닉네임";
 String subTitle = ""; //subTitle있는것 없는것이 있어서, 있으면 나타나도록 수정.. 
 if (request.getParameter("subTitle") != null && !request.getParameter("subTitle").equals("") ){
	 subTitle = request.getParameter("subTitle");
	//System.out.println("카테고리"+category);
} 

String saleStatus = "";
if (request.getParameter("saleStatus") != null && !request.getParameter("saleStatus").equals("") ){
	saleStatus = request.getParameter("saleStatus");
}

//이부분 살짝 다름..?
//체크되어있으면 공개
//비체크는 비공개
 String isPrivateString = request.getParameter("isPrivate");
	Boolean isPrivate = true;
	if(isPrivateString.equals("false")){
		isPrivate = false;
	}

/* System.out.println(title);
System.out.println(subTitle);
System.out.println(content);
System.out.println(thumbUrl);
System.out.println(nickname);
System.out.println(category);
System.out.println(familyMemberType);
System.out.println(saleStatus); */

//수정되면서 사라진 파일 삭제
	String[] removedImg = request.getParameterValues("removedImg[]");
	if(removedImg != null){
		//System.out.println("removedImg length : "+ removedImg.length);
		
		for(String fileUrl : removedImg){
			String[] splitedFileUrl = fileUrl.split("/");
			String fileName = splitedFileUrl[splitedFileUrl.length -1];
			
			String filePath = request.getRealPath("upload")+"/";
			filePath += fileName;
			
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
		}
	}


//수정
BoardTO to = new BoardTO();
to.setCategorySeq(category); 
to.setTitle(title);
to.setSubTitle(subTitle);
to.setContent(content);
to.setThumbUrl(thumbUrl);
to.setFamilyMemberType(familyMemberType);
to.setSaleStatus(saleStatus);
to.setIsPrivate(isPrivate); //공개키
to.setSeq(seq);

AdminNoticeDAO dao = new AdminNoticeDAO();
int flag = dao.modifyOk(to);

System.out.println(flag);

JSONObject json = new JSONObject();
// 일단 성공한 척 넣기
json.put("flag", flag);
json.put("category",category);
json.put("seq",seq);


out.println(json);

%>