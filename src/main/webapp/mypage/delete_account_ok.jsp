
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="model1.MypageTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="model1.UserDAO"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.MypageDAO"%>

<%
	String nickname = (String)session.getAttribute("nickname");
	String mail = (String)session.getAttribute("mail");
	String password = request.getParameter("pwd");
	
	MypageTO to = new MypageTO();
	to.setOldPassword(password);
	to.setMail(mail);
	to.setNickname(nickname);
	
	MypageDAO dao = new MypageDAO();
	int flag = dao.deleteAccount(to);
	
	JSONObject json = new JSONObject();
	json.put("flag", flag);
	
	if(flag == 1){
		session.invalidate(); //세션 폐기
		System.out.println("세션 폐기");
	}
	
	out.println(json);
	
	
%>