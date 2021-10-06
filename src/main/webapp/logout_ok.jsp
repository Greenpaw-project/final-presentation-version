<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	session.invalidate(); //세션 폐기
	out.println("<script src='./js/kakao_logOut.js'></script>");
	out.println("<script type='text/javascript'>");
	out.println("alert('로그아웃 되었습니다');");
	out.println("location.href='main.jsp';");
	out.println("</script>");
%>