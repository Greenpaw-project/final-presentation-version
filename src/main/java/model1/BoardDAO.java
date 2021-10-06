package model1;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataSource;

	public BoardDAO() {
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

	public String sha256(String password) {

		StringBuffer EncryptPs = null;

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			EncryptPs = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);

				if (hex.length() == 1) {
					EncryptPs.append('0');

				} else {
					EncryptPs.append(hex);
				}
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return EncryptPs.toString();
	}

	/*********** 소연님 **************/
	// 일반 게시판 전체리스트(카테고리 별 1페이지에 나오는 것)
	public ArrayList<BoardTO> getList(String categorySeq, int page, int perPage) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

		try {
			conn = this.dataSource.getConnection();

			String sql = "select * from board where category_seq=? and is_private=0 order by seq desc limit ?, ?";
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
			String sql = "select category_seq, sub_title, sale_status, family_member_type, title, content, thumb_url, nickname, like_count, hit, date_format(created_at, '%Y-%m-%d') created_at, updated_at from board where category_seq=? and "
					+ field + " like ? and sub_title like ? and is_private=0  order by seq desc";
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
					+ " like ? and is_private=0  order by seq desc limit ?, ?";
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

			String sql = "select * from board where category_seq=? and " + field + " like ? and is_private=0  order by "
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

	// 말머리만 클릭시
	public ArrayList<BoardTO> getSubTitleList(String category, String subtitle, PageTO pages) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();

		try {
			conn = this.dataSource.getConnection();

			String sql = "select * from board where category_seq=? and sub_title=? and is_private=0  order by seq desc limit ?, ?";
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
}
