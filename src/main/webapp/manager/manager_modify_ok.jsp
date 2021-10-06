<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="org.json.simple.JSONObject"%>
<%@page import="model1.ManagerDAO"%>
<%@page import="model1.ManagerTO"%>
<%
request.setCharacterEncoding("utf-8");

// 넘어온 파라미터로 디비에 insert 
String nicknames = request.getParameter("nicknames");
String nickname = nicknames.replace("[","").replace("]","").replaceAll("\"","\'");
String status = request.getParameter("status");

System.out.println("닉네임 :"+nickname);
System.out.println("닉네임 :"+status);

ManagerDAO dao = new ManagerDAO();
int flag = dao.modifyOK(nickname,status);

System.out.println(flag);

JSONObject json = new JSONObject();
// 일단 성공한 척 넣기
json.put("flag", flag);

out.println(json);

%>