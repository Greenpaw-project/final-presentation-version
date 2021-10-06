<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model1.UserTO"%>
<%@page import="model1.UserDAO"%>    
<%
    String email = (String) session.getAttribute("mail");
            UserDAO dao = new UserDAO();
        	UserTO userData = dao.loginInformation(email);
        	
        	System.out.println(userData.getNickname());
        	session.setAttribute("nickname",userData.getNickname());
        	session.setAttribute("authType", userData.getAuthType());
        	
        	String authType = (String)session.getAttribute("authType");
        	
        	if(authType.equals("탈퇴")){
        		session.invalidate(); //세션 폐기
        		out.println("<script>");
        		out.println("alert('탈퇴한 회원입니다.')");
        		out.println("location.href='../main.jsp'");
        		out.println("</script>");
        		
        	}else{
	        	out.println("<script>");
    	    	out.println("alert('🐾환영합니다🌿');");
        		out.println("location.href='../main.jsp'");
        		out.println("</script>");
        	}
%>

