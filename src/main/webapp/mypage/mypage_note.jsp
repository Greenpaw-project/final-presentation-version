<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.MypageDAO"%>
<%@page import="model1.MypageTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String nickname = (String)session.getAttribute("nickname");
	String note = request.getParameter("note");
	//System.out.println(note);
	
	MypageTO to = new MypageTO();
	to.setNote(note);
	to.setNickname(nickname);
	
	MypageDAO dao = new MypageDAO();
	int flag = dao.addNote(to);
	
	JSONObject json = new JSONObject();
	json.put("flag", flag);
	//System.out.println("mypage note flag: "+json.get("flag"));
	
	out.println(json);
%>