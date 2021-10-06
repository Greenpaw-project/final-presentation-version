package model1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AdminNoticeDAO {
	private DataSource dataSource;

	public AdminNoticeDAO() {
		// TODO Auto-generated constructor stub
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			this.dataSource = (DataSource) envCtx.lookup("jdbc/mariadb0");

			System.out.println("DAO 연결 성공");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("[에러] " + e.getMessage());
		}
	}
	
	
	///일괄 공개체크
	public ArrayList<BoardTO> getList(String categorySeq, int page, int perPage) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

		try {
			conn = this.dataSource.getConnection();

			String sql = "select * from board where category_seq=? order by seq desc limit ?, ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, categorySeq);
			pstmt.setInt(2, (page - 1) * 10);
			pstmt.setInt(3, perPage);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setLikeCount(rs.getString("like_count"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				datas.add(to);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)	try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return datas;
	}
	
	//일괄삭제 메소드
	public int deleteAll(int[] deleteSeq) {
		//System.out.println("삭제할 값"+deleteSeq);
		int result=0;
		
		String params ="";
		for(int i=0; i<deleteSeq.length; i++) {
			params +=deleteSeq[i];
			//마지막이 아닐때만 뒤에 , 넣어줌 
			if(i < deleteSeq.length-1) {
				params +=",";
			}
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql ="delete from board where seq in("+params+")";

			pstmt = conn.prepareStatement(sql);
			
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
			if(conn != null) try {conn.close();} catch(SQLException e) {}
		}

		return result;
	}
	
	
	
	
	//inputbox에 넣을 숫자...
	public ArrayList<String> getNoticeSeq(String categorySeq, int page, int perPage) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<String> datas = new ArrayList<String>();

		try {
			conn = this.dataSource.getConnection();

			String sql = "select seq from board where category_seq=? order by seq desc limit ?, ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, categorySeq);
			pstmt.setInt(2, (page - 1) * 10);
			pstmt.setInt(3, perPage);

			rs = pstmt.executeQuery();

			
			while(rs.next()) {
				String seq = rs.getString("seq");
				datas.add(seq);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)	try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return datas;
	}
	
	
	//일괄공개,일괄비공개
	public int privateNoticeAll(int[] privateSeqs,int[] allList){
		
		List<String> privateList = new ArrayList<String>();
		for(int i=0; i<=privateSeqs.length; i++) {
			privateList.add(String.valueOf(privateSeqs[i]));
		}
		
		List<String> allLists = new ArrayList<String>();
		for(int i=0; i<=allList.length; i++) {
			privateList.add(String.valueOf(allList[i]));
		}
		
		//List<String>으로 감
		return privateNoticeAll(privateList,allLists);
	}
	
	//String으로 감
	public int privateNoticeAll(List<String> privateSeqs,List<String> allList){
			
		String privateSeqsStr = String.join(",", privateSeqs);
		String allListStr = String.join(",", allList);
		
			return privateNoticeAll(privateSeqsStr,allListStr);
		}
	
	public int privateNoticeAll(String privateSeqsStr,String allListStr){
		
		Connection conn = null;
		PreparedStatement pstmtOpen = null;
		PreparedStatement pstmtClose = null;
		ResultSet rs = null;

		int result = 0;
		try {
			conn = this.dataSource.getConnection();
			//공개 sql
			String sqlOpen ="update board set is_private=0 where seq in("+privateSeqsStr+") ";
			
			//비공개 sql
			String sqlClose ="update board set is_private=1 where seq in("+allListStr+")";
			
			//공개 실행
			pstmtOpen = conn.prepareStatement(sqlOpen);
			result += pstmtOpen.executeUpdate();
			
			//비공개 실행
			pstmtClose = conn.prepareStatement(sqlClose);
			result += pstmtClose.executeUpdate();
			

		} catch (SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (pstmtOpen != null) try {pstmtOpen.close();} catch (SQLException e) {}
			if (pstmtClose != null) try {pstmtClose.close();} catch (SQLException e) {}
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	
	//글쓰기
	public int writeOk(BoardTO post) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("writeOk 접근");

		int flag = 0;
		try {
			conn = this.dataSource.getConnection();

			// | seq | 1.category_seq | 2.sub_title | 3.sale_status | 4.family_member_type
			// | 5. title | 6. content | 7. thumb_url | 8. nickname
			// | like_count | hit | created_at | updated_at |
			String sql = "insert into board values (0, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "0, 0, now(), now(), ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getCategorySeq());
			pstmt.setString(2, post.getSubTitle());
			pstmt.setString(3, post.getSaleStatus());
			pstmt.setString(4, post.getFamilyMemberType());
			pstmt.setString(5, post.getTitle());
			pstmt.setString(6, post.getContent());
			pstmt.setString(7, post.getThumbUrl());
			pstmt.setString(8, post.getNickname());
			pstmt.setBoolean(9, post.isPrivate());

			int result = pstmt.executeUpdate();
			if (result == 1) {
				flag = 1;
			}

		} catch (SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
		return flag;
	}
	
	
	//이미지파일명 가져오기
	public ArrayList<String> getImgList(int seq) {
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String content ="";
		ArrayList<String> ImgList=new ArrayList<String>();
		
		try {
			conn = this.dataSource.getConnection();
			String sql ="select content from board where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				content = rs.getString("content");
			}
			//System.out.println(content);

			//이미지 경로 추출하는 정규식
			Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
			Matcher matcher = pattern.matcher(content);
			
			//url추출
			while(matcher.find()) {
				String fileurl = matcher.group(1);
				ImgList.add(fileurl);
				//ImgList.add(matcher.group(1));
			}
			//System.out.println(ImgList);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
		
		return ImgList;
	}
	
	
	// 페이지 개수 /*수정함*/
		public PageTO boardList(PageTO PageTO, String category, String field, String keyword, String subtitle) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			if (field == "type" || field.equals("type")) {
				field = "family_member_type";
			}

			int cpage = PageTO.getCpage();
			int recordPerPage = PageTO.getRecordPerPage();
			int blockPerPage = PageTO.getBlockPerPage();

			try {
				conn = this.dataSource.getConnection();
				/* 수정함 */
				String sql = "select category_seq, sub_title, sale_status, family_member_type, title, content, thumb_url, nickname, like_count, hit, date_format(created_at, '%Y-%m-%d') created_at, updated_at,is_private from board where category_seq=? and "
						+ field + " like ? and sub_title like ? order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, category);// 추가
				pstmt.setString(2, "%" + keyword + "%");
				pstmt.setString(3, "%" + subtitle + "%");

				rs = pstmt.executeQuery();

				rs.last();
				PageTO.setTotalRecord(rs.getRow());
				rs.beforeFirst();

				PageTO.setTotalPage(((PageTO.getTotalRecord() - 1) / recordPerPage) + 1);

				int skip = (cpage - 1) * recordPerPage;
				if (skip != 0)
					rs.absolute(skip);

				ArrayList<BoardTO> boardLists = new ArrayList<BoardTO>();
				for (int i = 0; i < recordPerPage && rs.next(); i++) {
					BoardTO to = new BoardTO();
					to.setCategorySeq(rs.getString("category_seq"));
					to.setSubTitle(rs.getString("sub_title"));
					to.setSaleStatus(rs.getString("sale_status"));
					to.setFamilyMemberType(rs.getString("family_member_type"));
					to.setTitle(rs.getString("title"));
					to.setContent(rs.getString("content"));
					to.setThumbUrl(rs.getString("thumb_url"));
					to.setNickname(rs.getString("nickname"));
					to.setLikeCount(rs.getString("like_count"));
					to.setHit(rs.getString("hit"));
					to.setCreatedAt(rs.getString("created_at"));
					to.setUpdatedAt(rs.getString("updated_at"));
					to.setIsPrivate(rs.getBoolean("is_private"));

					boardLists.add(to);
				}

				PageTO.setPageList(boardLists);

				PageTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
				PageTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
				if (PageTO.getEndBlock() >= PageTO.getTotalPage()) {
					PageTO.setEndBlock(PageTO.getTotalPage());
				}
			} catch (SQLException e) {
				System.out.println("[에러] " + e.getMessage());
			} finally {
				if (rs != null)	try {rs.close();} catch (SQLException e) {}
				if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
				if (conn != null) try {conn.close();} catch (SQLException e) {}
			}

			return PageTO;
		}
		
		
		//페이지개수(날짜 검색 시)
				public PageTO boardList(PageTO PageTO, String category,String startDate,String endDate) {
					Connection conn = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;


					int cpage = PageTO.getCpage();
					int recordPerPage = PageTO.getRecordPerPage();
					int blockPerPage = PageTO.getBlockPerPage();

					try {
						conn = this.dataSource.getConnection();
						/* 수정함 */
						String sql = "select category_seq, sub_title, sale_status, family_member_type, title, content, thumb_url, nickname, like_count, hit, date_format(created_at, '%Y-%m-%d') created_at, updated_at,is_private from board where category_seq=? and "
								+"created_at between ? and ? order by seq desc";
						pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						pstmt.setString(1, category);// 추가
						pstmt.setString(2, startDate);
						pstmt.setString(3, endDate);

						rs = pstmt.executeQuery();

						rs.last();
						PageTO.setTotalRecord(rs.getRow());
						rs.beforeFirst();

						PageTO.setTotalPage(((PageTO.getTotalRecord() - 1) / recordPerPage) + 1);

						int skip = (cpage - 1) * recordPerPage;
						if (skip != 0)
							rs.absolute(skip);

						ArrayList<BoardTO> boardLists = new ArrayList<BoardTO>();
						for (int i = 0; i < recordPerPage && rs.next(); i++) {
							BoardTO to = new BoardTO();
							to.setCategorySeq(rs.getString("category_seq"));
							to.setSubTitle(rs.getString("sub_title"));
							to.setSaleStatus(rs.getString("sale_status"));
							to.setFamilyMemberType(rs.getString("family_member_type"));
							to.setTitle(rs.getString("title"));
							to.setContent(rs.getString("content"));
							to.setThumbUrl(rs.getString("thumb_url"));
							to.setNickname(rs.getString("nickname"));
							to.setLikeCount(rs.getString("like_count"));
							to.setHit(rs.getString("hit"));
							to.setCreatedAt(rs.getString("created_at"));
							to.setUpdatedAt(rs.getString("updated_at"));
							to.setIsPrivate(rs.getBoolean("is_private"));

							boardLists.add(to);
						}

						PageTO.setPageList(boardLists);

						PageTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
						PageTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
						if (PageTO.getEndBlock() >= PageTO.getTotalPage()) {
							PageTO.setEndBlock(PageTO.getTotalPage());
						}
					} catch (SQLException e) {
						System.out.println("[에러] " + e.getMessage());
					} finally {
						if (rs != null)	try {rs.close();} catch (SQLException e) {}
						if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
						if (conn != null) try {conn.close();} catch (SQLException e) {}
					}

					return PageTO;
				}
		
		
		
		

		// 검색
		public ArrayList<BoardTO> getSearchList(String categorySeq, String field, String keyword, PageTO pages) {

			// System.out.println("검색"+categorySeq+"/"+field+"/"+keyword+"/"+page+"/"+perPage);

			if (field == "type" || field.equals("type")) {
				field = "family_member_type";
			}

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

			try {
				conn = this.dataSource.getConnection();

				String sql = "select * from board where category_seq=? and " + field
						+ " like ? order by seq desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, categorySeq);
				pstmt.setString(2, "%" + keyword + "%"); // 검색 값
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());

				// System.out.println("카테고리명:"+categorySeq);
				// System.out.println("필드값:"+field);
				// System.out.println("검색값:"+keyword);
				// System.out.println("각 목록 첫번째:"+page);
				// System.out.println("한페이지 리스트수"+perPage);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardTO to = new BoardTO();
					to.setSeq(rs.getString("seq"));
					to.setSubTitle(rs.getString("sub_title"));
					to.setSaleStatus(rs.getString("sale_status"));
					to.setFamilyMemberType(rs.getString("family_member_type"));
					to.setTitle(rs.getString("title"));
					to.setNickname(rs.getString("nickname"));
					to.setLikeCount(rs.getString("like_count"));
					to.setHit(rs.getString("hit"));
					to.setCreatedAt(rs.getString("created_at"));
					to.setIsPrivate(rs.getBoolean("is_private"));
					datas.add(to);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("[에러]" + e.getMessage());
			} finally {
				if (rs != null)	try {rs.close();} catch (SQLException e) {}
				if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
				if (conn != null) try {conn.close();} catch (SQLException e) {}
			}
			return datas;
		}

		// 정렬기능
		public ArrayList<BoardTO> listSort(String categorySeq, String field, String keyword, String sort, PageTO pages) {

			// System.out.println("검색"+categorySeq+"/"+field+"/"+keyword+"/"+page+"/"+perPage);

			if (sort == null && sort.equals("")) {
				sort = "seq";
			}

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

			try {
				conn = this.dataSource.getConnection();

				String sql = "select * from board where category_seq=? and " + field + " like ? order by "
						+ sort + " desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, categorySeq);
				pstmt.setString(2, "%" + keyword + "%"); // 검색 값
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());

				/*
				 * System.out.println("카테고리명:"+categorySeq); System.out.println("필드값:"+field);
				 * System.out.println("검색값:"+keyword); System.out.println("각 목록 첫번째:"+page);
				 * System.out.println("한페이지 리스트수"+perPage);
				 */

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardTO to = new BoardTO();
					to.setSeq(rs.getString("seq"));
					to.setSubTitle(rs.getString("sub_title"));
					to.setSaleStatus(rs.getString("sale_status"));
					to.setFamilyMemberType(rs.getString("family_member_type"));
					to.setTitle(rs.getString("title"));
					to.setNickname(rs.getString("nickname"));
					to.setLikeCount(rs.getString("like_count"));
					to.setHit(rs.getString("hit"));
					to.setCreatedAt(rs.getString("created_at"));
					to.setIsPrivate(rs.getBoolean("is_private"));
					datas.add(to);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("[에러]" + e.getMessage());
			} finally {
				if (rs != null)	try {rs.close();} catch (SQLException e) {}
				if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
				if (conn != null) try {conn.close();} catch (SQLException e) {}
			}
			return datas;
		}

		// 말머리만 클릭시
		public ArrayList<BoardTO> getSubTitleList(String category, String subtitle, PageTO pages) {

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

			try {
				conn = this.dataSource.getConnection();

				String sql = "select * from board where category_seq=? and sub_title=? order by seq desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, category);
				pstmt.setString(2, subtitle);
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardTO to = new BoardTO();
					to.setSeq(rs.getString("seq"));
					to.setSubTitle(rs.getString("sub_title"));
					to.setSaleStatus(rs.getString("sale_status"));
					to.setFamilyMemberType(rs.getString("family_member_type"));
					to.setTitle(rs.getString("title"));
					to.setNickname(rs.getString("nickname"));
					to.setLikeCount(rs.getString("like_count"));
					to.setHit(rs.getString("hit"));
					to.setCreatedAt(rs.getString("created_at"));
					to.setIsPrivate(rs.getBoolean("is_private"));
					datas.add(to);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rs != null)	try {rs.close();} catch (SQLException e) {}
				if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
				if (conn != null) try {conn.close();} catch (SQLException e) {}
			}

			return datas;
		}
		
		//날짜검색
		public ArrayList<BoardTO> dateSearch(String startDate , String endDate, PageTO pages){

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

			try {
				conn = this.dataSource.getConnection();

				String sql = "select * from board where category_seq='7' and created_at between ? and ? order by seq desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());

				rs = pstmt.executeQuery();
				
				

				while (rs.next()) {
					BoardTO to = new BoardTO();
					to.setSeq(rs.getString("seq"));
					to.setSubTitle(rs.getString("sub_title"));
					to.setSaleStatus(rs.getString("sale_status"));
					to.setFamilyMemberType(rs.getString("family_member_type"));
					to.setTitle(rs.getString("title"));
					to.setNickname(rs.getString("nickname"));
					to.setLikeCount(rs.getString("like_count"));
					to.setHit(rs.getString("hit"));
					to.setCreatedAt(rs.getString("created_at"));
					to.setIsPrivate(rs.getBoolean("is_private"));
					datas.add(to);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rs != null)	try {rs.close();} catch (SQLException e) {}
				if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
				if (conn != null) try {conn.close();} catch (SQLException e) {}
			}

			return datas;
			
		}
		
		//수정
		public int modifyOk(BoardTO to) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			int flag = 2;
			
			try {
				conn =this.dataSource.getConnection();
				
				String sql = "update board set title=?, sub_title=?, family_member_type=?,"
						+ " content=?, updated_at=now(),is_private=? where seq=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1,to.getTitle());
				pstmt.setString(2,to.getSubTitle());
				pstmt.setString(3,to.getFamilyMemberType());
				pstmt.setString(4,to.getContent());
				pstmt.setBoolean(5,to.isPrivate());
				pstmt.setString(6,to.getSeq());
				
				//System.out.println(to.getTitle());
				//System.out.println(to.getSubTitle());
				//System.out.println(to.getFamilyMemberType());
				//System.out.println(to.getContent());
				//System.out.println(to.getSeq());
				
				int result = pstmt.executeUpdate();
				
				System.out.println(result);
				if(result == 1) {
					flag = 1; //성공
				}else if(result == 0) {
					flag = 0; //실패
				}
			} catch (SQLException e) {
				System.out.println("[에러]"+e.getMessage());
			} finally {
				if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
				if(conn!=null) try{conn.close();} catch(SQLException e) {};
			}
			
			return flag;
		}
		
		
		
		//삭제
		public int deleteOk(BoardTO to) {
			
			int flag = 2; //데이터 오류
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				conn = this.dataSource.getConnection();
				// 본인확인용 컬럼 추가 
				String sql = "delete from board where seq=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, to.getSeq());
				
				System.out.println(to.getSeq());
			
				int result = pstmt.executeUpdate();
				
				if(result == 1) {
					flag = 1; //성공
				}
				
				if(result == 0) {
					flag= 0; //실패
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println( "[에러] " + e.getMessage() );
			} finally {
				if(rs != null) try {rs.close();} catch(SQLException e) {}
				if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
				if(conn != null) try {conn.close();} catch(SQLException e) {}
			}
			return flag; 
		}
	
}
