<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.UserDAO"%>
<%@page import="model1.UserTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
	String mail = (String)request.getParameter("mail");
	String password = (String)request.getParameter("password");
	String hint = (String)request.getParameter("hint");
	String answer = (String)request.getParameter("answer");
	
	System.out.println(mail);
	System.out.println(password);
	System.out.println(hint);
	System.out.println(answer);

	UserTO to = new UserTO();
	UserDAO dao = new UserDAO();
	String EncryptPs = dao.sha256(password);
	
	to.setMail(mail);
	to.setPassword(EncryptPs);
	to.setHintPassword(hint);
	to.setAnswerPassword(answer);

	/*
	System.out.println(to.getMail());
	System.out.println(to.getPassword());
	System.out.println(to.getHintPassword());
	System.out.println(to.getAnswerPassword());
	*/
	
	int flag = dao.forgotPassword(to);
	
	JSONObject json = new JSONObject();
	json.put("flag", flag);
	System.out.println("forgot password ok flag: "+json.get("flag"));
	
	out.println(json);
	
%>