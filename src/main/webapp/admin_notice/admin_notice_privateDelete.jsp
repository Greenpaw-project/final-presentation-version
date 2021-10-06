<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="model1.AdminNoticeDAO"%>

<%
/* *******일괄삭제, 일괄공개 버튼 클릭시 *********** */
request.setCharacterEncoding("utf-8");
String category = request.getParameter("category");
String authType = (String) session.getAttribute("authType");
//System.out.println(authType);

/* ******* 로그인 상태 확인 *********** */
int flag = 1; //비로그인 상태
if (session.getAttribute("mail") != null) {
	flag = 0; //로그인 상태
}

if (flag == 1) { //비로그인 시
	out.println("<script type='text/javascript'>");
	out.println("alert('잘못된 접근입니다. 관리자 로그인 후 이용가능합니다.');");
	out.println("location.href='../sign_in.jsp';");
	out.println("</script>");
} else { // 로그인시
	out.println("<script src='./js/menu_logeIn.js' defer></script>");
}

/* ******* 관리자 상태 확인 *********** */
if (authType != null && !authType.equals("관리자")) {
	out.println("<script>");
	out.println("alert('관리자만 사용가능합니다.');");
	out.println("location.href='" + request.getContextPath() + "/community_main.jsp'");
	out.println("</script>");
}

/* ******* 체크한 값이 있을때만 각 배열에 값 넣어주기 *********** */
String[] privateSeqs = null;
String[] deleteSeqs = null;
if (request.getParameterValues("private") != null) {
	String[] privates = request.getParameterValues("private");
	// 받아온 값의 사이즈에 맞는 배열 new 해주기
	privateSeqs = new String[privates.length];
	for (int i = 0; i < privates.length; i++) {
		// 각 인덱스에 값 대입
		privateSeqs[i] = privates[i];
	}
}

if (request.getParameterValues("delete") != null) { //공개할 글seq
	String[] deletes = request.getParameterValues("delete");
	// 받아온 값의 사이즈에 맞는 배열 new 해주기
	deleteSeqs = new String[deletes.length];
	for (int i = 0; i < deletes.length; i++) {
		// 각 인덱스에 값 대입
		deleteSeqs[i] = deletes[i];
	}
}

/* ******* 분기 처리 해주기 전 공통 설정 *********** */
AdminNoticeDAO dao = new AdminNoticeDAO();
String cmd = request.getParameter("cmd"); //일괄공개,일괄삭제 어떤걸 클릭했는지 체크
String seqList = request.getParameter("seqlist"); //전체목록
String[] seqLists = seqList.split(" "); //전체 목록 

/* ******* 삭제 선택 없을 시 *********** */
if (deleteSeqs == null && cmd.equals("일괄삭제")) {
	out.println("<script>");
	out.println("alert('하나 이상 선택해주세요.');");
	out.println("history.back();");
	out.println("</script>");
}

/* ******* 공개 선택 없을 시 *********** */
if (privateSeqs == null && cmd.equals("일괄공개")) {
	out.println("<script>");
	out.println("alert('하나 이상 선택해주세요.');");
	out.println("history.back();");
	out.println("</script>");
}

/* ******* 공개 선택 있을 시 *********** */
if (privateSeqs != null && cmd.equals("일괄공개")) {

	List<String> privateSeqList = Arrays.asList(privateSeqs); //공개 목록

	List<String> allList = new ArrayList(Arrays.asList(seqLists));
	allList.removeAll(privateSeqList);

	//System.out.println("전체 :"+Arrays.asList(seqLists)); //전체 
	//System.out.println("비공개 :"+allList); //비공개 번호
	//System.out.println("공개 :"+privateSeqList); //공개될 번호

	dao.privateNoticeAll(privateSeqList, allList);
	//System.out.println("privateid:"+privateSeq);

	out.println("<script>");
	out.println("location.href='./admin_noticeboard.jsp?category=" + category + "'");
	out.println("</script>");

} else if (deleteSeqs != null && cmd.equals("일괄삭제")) {
	/* ******* 삭제 선택 있을 시 *********** */

	//받아온 값을 다시 저장
	int[] deleteSeq = new int[deleteSeqs.length];

	for (int i = 0; i < deleteSeqs.length; i++) {
		deleteSeq[i] = Integer.parseInt(deleteSeqs[i]);
	}

	//이미지 전부 삭제되도록
	//삭제할 글의 개수만큼 반복
	for (int i = 0; i < deleteSeq.length; i++) {
		//System.out.println(dao.getImgList(deleteSeq[i]));
		ArrayList<String> imglist = new ArrayList<String>();
		imglist = dao.getImgList(deleteSeq[i]);
		//System.out.println(imglist);
		for (String fileUrl : imglist) {
			String[] splitedurl = fileUrl.split("/");
			String fileName = splitedurl[splitedurl.length - 1];

			//경로 지정
			String filePath = request.getRealPath("upload") + "/";
			filePath += fileName;

			File file = new File(filePath); // 위의 경로에 파일 처리 하기위한 파일객체 만들기
			
			if (file.exists()) {
				file.delete(); // 파일이 있으면 지워줘 
				System.out.println("파일 삭제 성공!");
			}
		}
	}
	//글삭제 실행
	int result = dao.deleteAll(deleteSeq);

	out.println("<script>");
	out.println("location.href='./admin_noticeboard.jsp?category=" + category + "'");
	out.println("</script>");

}
%>

