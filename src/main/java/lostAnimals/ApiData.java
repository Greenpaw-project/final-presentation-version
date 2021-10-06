package lostAnimals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;


public class ApiData {

	// api 요청 서비스키
	private String encServiceKey = "=L62gx5%2F6HyMBJ%2BrA0GNMzjbisl%2F9WAn67zhy4GIHFnRiR0qXOSLA7E7PdY1mraAxSLpZlLDods0z6f4Zdv5smQ%3D%3D";
	private String requestUrl = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/";

	/* api 요청 url 만드는 메서드 */
	// 1. 시도: sido
	// 2. 구군: sigungu
	// 3. 동물: abandonmentPublic
	public String getRequestUrl(String request) {

		// 요청 종류에 따른 url
		StringBuilder url = new StringBuilder(requestUrl);
		url.append(request);
		url.append("?serviceKey" + encServiceKey); /* Service Key */

		return url.toString();
	}
	
	/* 검색 조건 받아서 api 내용 받아오는 메소드 */
	public String getAnimalListXml(SearchCondition search) throws IOException {

		// 동물 리스트용 url 받아오기
		String request = getRequestUrl("abandonmentPublic");
		
		// url 파싱 
		URL url = new URL(request+"&bgnde="+search.getStartDate()
			+"&endde="+search.getToday()
			+"&upr_cd="+search.getSidoCode()
			+"&org_cd="+search.getGugunCode()
			+"&pageNo="+search.getPageNo()
			+"&numOfRows="+search.getNumOfRows()); 
		//System.out.println(url);
		
		// url 주소에서 내용 긁어오기
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
		
		// buffer 를 String 으로 담을 객체 생성
		String xml = br.readLine();
		//System.out.println(xml); // xml 이 String 으로 찍힘
		
		return xml;
	}
	
	/* xml 받으면 JSONArray 로 파싱해주는 메소드 */
	public JSONArray getAnimalListToJson(String xml) {

        // xml 을 json 으로 파싱
        JSONObject all = XML.toJSONObject(xml);
        //System.out.println("json all : "+all);

        if (getTotalCount(xml) == 0) {
            return null;
        }

        if (getTotalCount(xml) == 1) {
            JSONObject filtered = all.getJSONObject("response").getJSONObject("body").getJSONObject("items");
            JSONObject justOneResult = filtered.getJSONObject("item");
            JSONArray result = new JSONArray();
            result.put(justOneResult);
            return result;
        }

        // 원하는 jsonArray 값까지 타고 내려가기 -> 실질적 값은 <item> 에 있음
        // total 값이 0 이 아닐때 == jsonObject 일때
        JSONObject filtered = all.getJSONObject("response").getJSONObject("body").getJSONObject("items");
        JSONArray result = filtered.getJSONArray("item"); 

        return result;
    }
	
	public int getTotalCount(String xml) {
		// xml 을 json 으로 파싱
		JSONObject all = XML.toJSONObject(xml);
		
		// 원하는 jsonArray 값까지 타고 내려가기 -> 실질적 값은 <item> 에 있음 
		JSONObject filtered = all.getJSONObject("response").getJSONObject("body");
		//System.out.println(filtered);
		int totalCount = filtered.getInt("totalCount");
		//System.out.println(totalCount);
		
		return totalCount;
	}

}