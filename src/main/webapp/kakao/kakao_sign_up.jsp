<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model1.UserTO"%>
<%@page import="model1.UserDAO"%>
<%
	request.setCharacterEncoding("utf-8");
	String email =  (String) session.getAttribute("kakao_email");
	String nickname =  request.getParameter("nickname");
	//System.out.println("카카오 가입시 닉네임 전달:"+request.getParameter("nickname"));
	
	UserTO to = new UserTO();
	to.setMail(email);
	to.setNickname(nickname);
	
	
	UserDAO dao = new UserDAO();
	int flag = dao.kakaoJoin(to);
	
	//성공
	if(flag == 1){
		session.setAttribute("mail", email);
		session.removeAttribute("kakao_email");
		session.removeAttribute("kakao_nickname");
		
		out.println("<script>");
		out.println("location.href='kakaologin_ok.jsp'");
		out.println("</script>");
		
	} else if(flag == 0){
		out.println("<script>");
		out.println("alert('시스템 에러')");
		out.println("history.back()");
		out.println("</script>");
	}
%>
