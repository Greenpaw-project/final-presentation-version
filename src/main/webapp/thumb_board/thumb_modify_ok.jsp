<%@ page language="java" contentType="plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.File"%>

<%@page import="model1.ThumbDAO"%>
<%@page import="model1.BoardTO"%>

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
String categorySeq = "5";
String saleStatus = request.getParameter("saleStatus");

System.out.println("modifyOKjsp salestatus상태값: "+saleStatus);
        	
        	
// 삭제해야하는 파일 배열
String[] removedImg = request.getParameterValues("removedImg[]") ;
  if ( removedImg != null ){
  //System.out.println("removedImg length : "+ removedImg.length);
    for( String fileUrl : removedImg ){
      String[] splitedFileUrl = fileUrl.split("/");
      String fileName = splitedFileUrl[splitedFileUrl.length-1];
        	
      // 각자 바꿔주기~! (아니면 realPath 쓰기)
      String filePath =  request.getRealPath("upload")+"/";
      filePath += fileName;
      //System.out.println("filePath : "+filePath);
        	
       File file = new File(filePath);
       if ( file.exists()){
        file.delete(); // 파일이 있으면 삭제 
      }
    }
  }
        	


BoardTO to = new BoardTO();
to.setTitle(title);
to.setContent(content);
to.setThumbUrl(thumbUrl);
to.setNickname(nickname);
to.setFamilyMemberType(familyMemberType);
to.setIsPrivate(isPrivate);
to.setCategorySeq(categorySeq);
to.setSaleStatus(saleStatus);
to.setIsPrivate(false);
to.setSeq(seq);

        	
// 글 수정 성공시 location.href 용 url 만들기
String type = request.getParameter("familyMemberType");
String cpage = request.getParameter("cpage");
String redirectUrl ="./thumb_view.jsp?cpage="+cpage+"&seq="+seq+"&type="+type;

System.out.println("redirectUrl : " + redirectUrl);
        	
ThumbDAO dao = new ThumbDAO();
int flag = dao.thumbmodifyOK(to);
System.out.println(flag);
        	
JSONObject json = new JSONObject();
json.put("flag", flag);
json.put("seq", to.getSeq());
json.put("redirect", redirectUrl);
        	
out.println(json);
 %>