<%@ page language="java" contentType="text/json; charset=utf-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@ page import="lostAnimals.SearchCondition"%>
<%@page import="lostAnimals.ApiDAO"%>
<%@page import="java.time.LocalDate"%>

<%@page import="lostAnimals.ApiData"%>

<%
// 지도에서 넘겨준 지역 정보
	String sido = request.getParameter("sido");
	String gugun = request.getParameter("gugun"); 
	String pageNo = "1";
	if (request.getParameter("pageNo") != null) {
		// 기본 페이지 값 : 1
		// 새로 페이지 누를때 페이지값 넘겨주기 
		pageNo = request.getParameter("pageNo");
	}
	
	String numOfRows = "5"; // 한 페이지에 보여줄 리스트 수
	if (request.getParameter("numOfRows") != null) {
		numOfRows = request.getParameter("numOfRows");
	}
	
	// 넘어온 시도를 DB 에서 코드로 받아오기 
	ApiDAO dao = new ApiDAO();
	String sidoCode = null;
	String gugunCode = null;
	if (dao.getCode(sido, gugun) != null) {
		// 정상값 넘어왔을 경우 초기화
		sidoCode = dao.getCode(sido, gugun)[0];
		gugunCode = dao.getCode(sido, gugun)[1];
	}
	
	// 검색 조건 설정할 class 
	SearchCondition search = new SearchCondition();
	// 검색 조건 세팅
	search.setSidoCode(sidoCode);
	search.setGugunCode(gugunCode);
	search.setToday(LocalDate.now()); // 2021-09-08
	search.setStartDate(LocalDate.now().minusMonths(2)); // 오늘로부터 2달전 
	search.setPageNo(pageNo);
	search.setNumOfRows(numOfRows);
	
	// 검색조건 api 로 넘겨서 리스트 뽑아오기 
	ApiData api = new ApiData();
	String xml = api.getAnimalListXml(search); // 먼저 xml 받고
	//JSONArray animalList = api.getAnimalListToJson(xml); // xml 을 jsonArray 로 파싱
	JSONArray animalList = new JSONArray();
    if (api.getAnimalListToJson(xml) != null){
        // xml 을 jsonArray 로 파싱
        animalList = api.getAnimalListToJson(xml);
    }
	
	
	// 페이징에 이용할 totalCount 를 JSONObject 로 만들어서 Array 에 추가
	JSONObject totalCount = new JSONObject();
	totalCount.put("totalCount",api.getTotalCount(xml));
	animalList.put(totalCount);
	
	//System.out.println(animalList);
	
	out.print(animalList);
%>
