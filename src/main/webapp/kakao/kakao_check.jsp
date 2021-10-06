<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model1.UserTO"%>
<%@page import="model1.UserDAO"%>
    <%
    //서소연 담당
            //계정정보가 db에 있는지 확인 + 없으면 추가~
            request.setCharacterEncoding("utf-8");
          	String email = request.getParameter("kakaomail");
            String nickname = request.getParameter("kakaonickname");
            
            //to에 값 전달
            UserTO to = new UserTO();
            to.setMail(email); 
           	to.setNickname(nickname); 
            
           	System.out.println("카카오체크");
            
           UserDAO dao = new UserDAO();
            int kakaocheck = dao.kakaoIdCheck(to);
            
         
            System.out.println("있으면1,없으면0:"+kakaocheck);
            
            //없음->아이디 새로 만듬
            if(kakaocheck == 0){
            	
            	out.println("<script>");
            	session.setAttribute("kakao_email", email);
            	session.setAttribute("kakao_nickname", nickname);
        		out.println("location.href='kakao_nickname.jsp'");
        		out.println("</script>");
        		
        		
        		//가입되어있음
            } else if(kakaocheck == 1){
            	session.setAttribute("mail", email);
            	//페이지 옮기면서 세션에 저장
            	//옮겨진 페이지에서 검사..
            	out.println("<script>");
        		out.println("location.href='kakaologin_ok.jsp'");
        		out.println("</script>");
            }
    %>
