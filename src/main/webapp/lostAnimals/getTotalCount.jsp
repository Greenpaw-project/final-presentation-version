<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="lostAnimals.TotalCountDAO"%>
<%@ page import="lostAnimals.TotalCountTO"%>
<%@ page import="org.json.simple.JSONObject"%>
    
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="java.util.ArrayList"%>

<%

	request.setCharacterEncoding("utf-8");
	
	String list = request.getParameter("list");
	//System.out.println(list);

	JSONParser parser = new JSONParser();
	JSONArray listJson = (JSONArray)parser.parse(list);

	ArrayList<TotalCountTO> addresses = new ArrayList<TotalCountTO>();
	ArrayList<TotalCountTO> datas = new ArrayList<TotalCountTO>();
	JSONArray datasJson = new JSONArray();
	
	for(int i = 0; i<listJson.size(); i++){
		JSONObject listObj = (JSONObject)listJson.get(i);
		
		TotalCountTO to = new TotalCountTO();
		
		to.setSidoShort(listObj.get("sido").toString());
		to.setGugun(listObj.get("gugun").toString());
		
		addresses.add(to);
	}
	
	TotalCountDAO dao = new TotalCountDAO();
	datas = dao.getTotalCountFromDB(addresses);
	
	for(TotalCountTO to : datas){	
		JSONObject dataObj = new JSONObject();
	
		dataObj.put("sido", to.getSidoShort());
		//dataObj.put("gugun", to.getGugun());
		if(to.getGugun().contains("창원")){			
			switch(to.getGugun()){
				case "창원시 마산합포회원구" :
					dataObj.put("gugun", "마산합포회원구");
					break;
				case "창원시 의창성산구" :
					dataObj.put("gugun", "의창성산구");
					break;
				case "창원시 진해구" :
					dataObj.put("gugun", "진해구");
					break;
				default:
					dataObj.put("gugun", to.getGugun());
			}
		}else{
			dataObj.put("gugun", to.getGugun());
		}
		dataObj.put("totalCounts", to.getTotalCounts());
		dataObj.put("x", to.getX());
		dataObj.put("y", to.getY());
		
		datasJson.add(dataObj);
	}
	
	//보내기
	JSONObject results = new JSONObject();
	results.put("datas", datasJson );
		
	out.println(results);
%>