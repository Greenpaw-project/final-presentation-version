package model1;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ManagerBoardDAO {
	private DataSource dataSource;

	public ManagerBoardDAO() {
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

	public PageTO getPagingByDate(PageTO PageTO, String category, String searchType, String keyword, String startDate,
			String endDate) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int cpage = PageTO.getCpage();
		int recordPerPage = PageTO.getRecordPerPage();
		int blockPerPage = PageTO.getBlockPerPage();

		try {
			conn = this.dataSource.getConnection();

			System.out.println("category " + category);
			System.out.println("searchType " + searchType);
			System.out.println("keyword " + keyword);
			System.out.println("startDate " + startDate);
			System.out.println("endDate " + endDate);

			String sql = "";
			if (category.equals("") || category.equals("null") || category == null) {
				System.out.println("sql1");
				sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board "
						+ "where created_at between ? and ? " + "order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
			} else {
				sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board "
						+ "where category_seq = ? " + "and " + searchType + " like ? "
						+ "and created_at between ? and ? order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, category);
				pstmt.setString(2, "%" + keyword + "%");
				pstmt.setString(3, startDate);
				pstmt.setString(4, endDate);
			}
			rs = pstmt.executeQuery();

			rs.last();
			PageTO.setTotalRecord(rs.getRow());
			System.out.println(rs.getRow());
			rs.beforeFirst();

			PageTO.setTotalPage(((PageTO.getTotalRecord() - 1) / recordPerPage) + 1);

			int skip = (cpage - 1) * recordPerPage;
			if (skip != 0)
				rs.absolute(skip);

			PageTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
			PageTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
			if (PageTO.getEndBlock() >= PageTO.getTotalPage()) {
				PageTO.setEndBlock(PageTO.getTotalPage());
			}
		} catch (SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		return PageTO;
	}

	// 페이지 설정용 리스트 로딩
	public PageTO boardList(PageTO PageTO, String category, String searchType, String keyword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		if (searchType.equals("all")) {
			searchType = "title";
		}

		int cpage = PageTO.getCpage();
		int recordPerPage = PageTO.getRecordPerPage();
		int blockPerPage = PageTO.getBlockPerPage();

		try {
			conn = this.dataSource.getConnection();

			System.out.println("category " + category);
			System.out.println("searchType " + searchType);
			System.out.println("keyword " + keyword);
			System.out.println("cpage " + cpage);
			System.out.println("recordPerPage " + recordPerPage);
			System.out.println("blockPerPage " + blockPerPage);

			// 첫 로딩땐 모든 게시글
			String sql = "";
			if (category.equals("") && keyword != null ){
				// 카테고리 설정 없이 검색할때 
				sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board "
						+ "where " + searchType + " like ? " + "order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, "%" + keyword + "%");
			} else if (category.equals("") || category.equals("null") || category == null) {
				// 모든 게시글 로딩 
				sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} else {
				// 카테고리 설정 & 검색
				sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board "
						+ "where category_seq = ? " + "and " + searchType + " like ? " + "order by seq desc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, category);
				pstmt.setString(2, "%" + keyword + "%");
			}
			//System.out.println("sql3");
			rs = pstmt.executeQuery();

			rs.last();
			PageTO.setTotalRecord(rs.getRow());
			System.out.println(rs.getRow());
			rs.beforeFirst();

			PageTO.setTotalPage(((PageTO.getTotalRecord() - 1) / recordPerPage) + 1);

			int skip = (cpage - 1) * recordPerPage;
			if (skip != 0)
				rs.absolute(skip);

			PageTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
			PageTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
			if (PageTO.getEndBlock() >= PageTO.getTotalPage()) {
				PageTO.setEndBlock(PageTO.getTotalPage());
			}
		} catch (SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		return PageTO;
	}

	// 게시글 리스트 그리기
	public ArrayList<BoardTO> getList(String categorySeq, int page, int perPage) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println("getList()");

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

		try {
			conn = this.dataSource.getConnection();

			String sql = "select seq, category_seq, title, nickname, created_at, hit, is_private from board order by seq desc limit ?, ?";
			pstmt = conn.prepareStatement(sql);

//			pstmt.setString(1, categorySeq);
			pstmt.setInt(1, (page - 1) * 10);
			pstmt.setInt(2, perPage);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setCategorySeq(rs.getString("category_seq"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				datas.add(to);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return datas;
	}

	// 검색
	public ArrayList<BoardTO> getSearchList(String categorySeq, String searchType, String keyword, PageTO pages) {

		System.out.println("getSearchList()");
		System.out.println("검색" + categorySeq + "/" + searchType + "/" + keyword);

		if (searchType.equals("all")) {
			searchType = "title";
		}
		System.out.println("searchtype : " + searchType);

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

		try {
			conn = this.dataSource.getConnection();
			String sql = "";
			
			if (categorySeq.equals("") || categorySeq ==null) {
				// 카테고리 설정 없을때
				sql = "select * from board where " + searchType
						+ " like ? order by seq desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + keyword + "%"); // 검색 값
				pstmt.setInt(2, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(3, pages.getRecordPerPage());
			} else {
				// 카테고리 설정 있을때
				sql = "select * from board where category_seq=? " + "and " + searchType
						+ " like ? order by seq desc limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, categorySeq);
				pstmt.setString(2, "%" + keyword + "%"); // 검색 값
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setCategorySeq(rs.getString("category_seq"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				datas.add(to);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[에러]" + e.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return datas;
	}

	/* ---------- 날짜 계산 ---------- */
	public ArrayList<BoardTO> dateSearch(String category, String searchType, String keyword, String startDate,
			String endDate, PageTO pages) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println("dao.dateSearch 연결 성공!");
		System.out.println(startDate + "/" + endDate);

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();
		try {
			conn = this.dataSource.getConnection();

			String sql = "";

			if (category.equals("") || category.equals("null") || category == null) {
				sql = "select * from board where created_at between ? and ? order by seq desc limit ? , ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				pstmt.setInt(3, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(4, pages.getRecordPerPage());
			} else {
				sql = "select * from board " + "where category_seq = ? " + "and " + searchType + " like ? "
						+ "and created_at between ? and ? order by seq desc limit ? , ?";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, category);
				pstmt.setString(2, "%" + keyword + "%");
				pstmt.setString(3, startDate);
				pstmt.setString(4, endDate);
				pstmt.setInt(5, (pages.getCpage() - 1) * pages.getRecordPerPage());
				pstmt.setInt(6, pages.getRecordPerPage());
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setCategorySeq(rs.getString("category_seq"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				datas.add(to);
			}
		} catch (SQLException e) {
			System.out.println("[dao.dateSearch 에러]" + e.getMessage());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return datas;
	}

	public int deleteOk(BoardTO to, String filePath) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int flag = 0;

		try {
			conn = this.dataSource.getConnection();

			String sql = "select thumb_url from board where seq =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();

			String thumbUrl = null;
			if (rs.next()) {
				thumbUrl = rs.getString("thumb_url");
				// System.out.println(thumbUrl);
				String[] splitedFileUrl = thumbUrl.split("/");
				String fileName = splitedFileUrl[splitedFileUrl.length - 1];
				System.out.println("fileName : " + fileName);

				filePath += fileName;
				System.out.println("filePath : "+filePath);

				File file = new File(filePath);
				if (file.exists()) {
					file.delete(); // 파일이 있으면 삭제
					System.out.println("파일 삭제 성공");
				}
			}

			sql = "delete from board where seq =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq()); // 글 seq

			int result = pstmt.executeUpdate();
			if (result == 1) {
				flag = 1; // 성공
			}

		} catch (

		SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		return flag;
	}
	
	public int hiddenOk(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int flag = 0;

		try {
			conn = this.dataSource.getConnection();

			String sql = "select is_private from board where seq =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				boolean isPrivate = false;
				// 기존 상태가 1 (비공개) 면 0으로, 0(공개) 면 1로 
				isPrivate = rs.getBoolean("is_private") ? false : true;
				System.out.println(isPrivate);
				
				sql = "update board set is_private=? where seq =?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setBoolean(1, isPrivate); // 글 seq
				pstmt.setString(2, to.getSeq()); // 글 seq
			}
			
			int result = pstmt.executeUpdate();
			if (result == 1) {
				flag = 1; // 성공
			}

		} catch (

		SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		return flag;
	}
}
