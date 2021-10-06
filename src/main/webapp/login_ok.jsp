<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="model1.UserTO"%>
<%@page import="model1.UserDAO"%>

<%
	request.setCharacterEncoding("utf-8");

	String email = request.getParameter("email");
	String password = request.getParameter("password");
	
	/* 
	System.out.println("입력 email: "+ email);
	System.out.println("입력 password: "+password);
	*/
	
	//System.out.println("암호화 전: "+password);
	
	UserDAO dao = new UserDAO();
	password = dao.sha256(password); //1. 비밀번호 암호화
	
	//System.out.println("암호화 후: "+password);
	//System.out.println("암호화 후 길이: "+password.length());
	
	
	UserTO to = new UserTO();
	to.setMail(email);
	to.setPassword(password);
	
	int result = dao.logInOk(to); //2. ID, 비밀번호 일치 확인(로그인 검증)
	
	int flag = 2;
	
	if( result == 1 ){
		//로그인 성공
		
		to = dao.getSessionData(to);
		
		
		session.setAttribute("mail", to.getMail());
		session.setAttribute("nickname", to.getNickname());
		session.setAttribute("authType", to.getAuthType());
		
		/*
		System.out.println(session.getAttribute("mail"));
		System.out.println(session.getAttribute("nickname"));
		System.out.println(session.getAttribute("authType"));
		*/
		
		flag = 0;

	}else{
		//로그인 실패
		flag = 1;
	}
	
	String authType = (String)session.getAttribute("authType");
	
	out.println("<script type='text/javascript'>");
	if(flag == 0 && !authType.equals("탈퇴")){
		//성공
		out.println("alert('🐾환영합니다🌿');");
		out.println("location.href='main.jsp';");
	
	}else if(flag == 0 && authType.equals("탈퇴")){
		out.println("alert('탈퇴한 회원입니다.')");
		session.invalidate(); //세션 폐기
		out.println("location.href='main.jsp';");
		
	}else if(flag == 1){
		//비밀번호 오류
		out.println("alert('이메일과 비밀번호를 확인해주세요.')");
		out.println("history.back();");
	
	}else{
		//시스템 에러
		out.println("alert('시스템 에러')");
		out.println("history.back();");
	}
	
	out.println("</script>");
%>