<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
    
<%@page import="model1.UserTO"%>
<%@page import="model1.JoinDAO"%>
<%@page import="javax.sql.DataSource"%>

<%@ page import="org.json.simple.JSONObject"%>

<%

String nickname = (String)request.getParameter("nickname");
String mail = (String)request.getParameter("mail");
String password = (String)request.getParameter("password");
String hint = (String)request.getParameter("hint");
String answer = (String)request.getParameter("answer");
 

//System.out.println("postUser.jsp : "+mail);

JoinDAO dao = new JoinDAO();
UserTO user = new UserTO();
user.setNickname(nickname);
user.setMail(mail);
user.setPassword( dao.sha256(password)); // 비번 암호화
user.setHintPassword(hint);
user.setAnswerPassword(answer);
  
int flag = dao.joinUser(user);

JSONObject result = new JSONObject();
result.put("flag",flag);

out.println(flag);
%>
