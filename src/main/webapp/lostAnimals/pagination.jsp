<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
    
<%
	
	int cpage = 1;
	int recordPerPage = 5;
	int blockPerPage = 5;
	int totalPage = 1;
	int totalRecord = 0;
	
	if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")){
		cpage = Integer.parseInt(request.getParameter("cpage"));
	}
	if (request.getParameter("totalRecord") != null && !request.getParameter("totalRecord").equals("")){
		totalRecord = Integer.parseInt(request.getParameter("totalRecord"));
	}
	
	totalPage = (((totalRecord - 1) / recordPerPage) + 1);
	
	
	int startBlock = (((cpage - 1) / blockPerPage) * blockPerPage + 1);
	int endBlock = (((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
	if ( endBlock >= totalPage) {
		endBlock = totalPage;
	}
	
	StringBuilder html = new StringBuilder();
	
	html.append("<ul class='page'>");
	/*
	System.out.println("startBlock : "+startBlock);
	System.out.println("(startBlock-1) : "+(startBlock-1));
	System.out.println("(startBlock + blockPerPage) : "+(startBlock + blockPerPage));
	*/
	//이전
	if(startBlock == 1){
		html.append("");
	}else{
		html.append("<li class='prev' value="+(startBlock-1)+">◀</li>");
	};
	//숫자
	for(int i = startBlock; i <= endBlock; i++){
		if(cpage == i){
			html.append("<li><b>"+i+"</b></li>");
		}else{
			html.append("<li>"+i+"</li>");
		};
	};
				                   
	//다음
	if (endBlock == totalPage) {
		html.append("");
	} else {
		html.append("<li class='next' value="+(startBlock + blockPerPage)+">▶</li>");
	};
	
	html.append("</ul>");
	
	//System.out.println(html);
	out.println(html);

%>
