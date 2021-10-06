<%@page import="javax.websocket.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="model1.MypageTO"%>
<%@page import="model1.MypageDAO"%>

<%@ page import="org.json.simple.JSONObject"%>

<%
	String nickname = (String)session.getAttribute("nickname");
	String mail = (String)session.getAttribute("mail");
	
	String oldPassword = request.getParameter("oldPassword");
	String newPassword = request.getParameter("newPassword");
	//System.out.println(oldPassword);
	//System.out.println(newPassword);

	MypageTO to = new MypageTO();
	to.setOldPassword(oldPassword);
	to.setNewPassword(newPassword);
	to.setMail(mail);
	to.setNickname(nickname);
	
	MypageDAO dao = new MypageDAO();
	int flag = dao.changePassword(to);
	//System.out.println(flag);
	
	JSONObject json = new JSONObject();
	json.put("flag", flag);
	System.out.println("password change ok flag: "+json.get("flag"));
	
	out.println(json);
	
%>