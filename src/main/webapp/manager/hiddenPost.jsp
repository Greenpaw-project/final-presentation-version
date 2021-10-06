
<%@page import="model1.ManagerBoardDAO"%>
<%@page import="java.io.File"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@page import="model1.NormalBoardDAO"%>
<%@page import="model1.BoardTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");

// ajax 에서 넘겨준 seq 배열
String[] seqList = request.getParameterValues("seqList[]");

int blockedAll = 0;

if (seqList != null) {
	System.out.println("List length: " + seqList.length);
	ManagerBoardDAO dao = new ManagerBoardDAO();
	
	for (String seq : seqList) {
		// 해당 seq 글 private 값 바꾸기
		BoardTO to = new BoardTO();
		to.setSeq(seq);
		blockedAll += dao.hiddenOk(to);
	}
}

int flag = 0;
if (blockedAll == seqList.length){
	flag = 1;
}

JSONObject json = new JSONObject();
json.put("flag", flag);

out.println(json);
%>