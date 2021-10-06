<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
    
<%@page import="model1.UserTO"%>
<%@page import="model1.JoinDAO"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="org.json.simple.JSONObject"%>

<%

String nickname = (String)request.getParameter("nickname");
//System.out.println("getNickname.jsp : "+nickname);

JoinDAO dao = new JoinDAO();
UserTO user = new UserTO();
user.setNickname(nickname);
 
int flag = dao.getNickname(user);

JSONObject result = new JSONObject();
result.put("flag",flag);

out.println(flag);
%>
