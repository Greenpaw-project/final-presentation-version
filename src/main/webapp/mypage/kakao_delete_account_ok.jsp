<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="org.json.simple.JSONObject"%>
<%@page import="model1.MypageDAO"%>
<%@page import="model1.MypageTO"%>

<%
	String nickname = (String)session.getAttribute("nickname");
	String mail = (String)session.getAttribute("mail");
	
	MypageTO to = new MypageTO(); //메일, 닉네임 to에 넣기
	to.setMail(mail);
	to.setNickname(nickname);

	MypageDAO dao = new MypageDAO();
	int flag = dao.deleteKaKaoAccount(to);
	
	JSONObject result = new JSONObject();
	result.put("flag", flag);
	
	if(flag == 1){
		session.invalidate(); //세션 폐기
		System.out.println("세션 폐기");
	}
	
	out.println(result);	
	
%>