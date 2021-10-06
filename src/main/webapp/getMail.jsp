<%@page import="model1.JoinDAO"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
    
<%@page import="model1.UserTO"%>
<%@page import="javax.sql.DataSource"%>

<%@page import="org.json.simple.JSONObject"%>

<%

String mail = (String)request.getParameter("mail");
//System.out.println("getMail.jsp : "+mail);

JoinDAO dao = new JoinDAO();
UserTO user = new UserTO();
user.setMail(mail);
 
int flag = dao.getMail(user);
 
JSONObject result = new JSONObject();
result.put("flag",flag);

out.println(flag);
%>
