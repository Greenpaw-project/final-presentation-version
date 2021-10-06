package model1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class ManagerDAO {
	private DataSource dataSource;
	
	public ManagerDAO() {
		// TODO Auto-generated constructor stub
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup( "java:comp/env" );
			this.dataSource = (DataSource)envCtx.lookup( "jdbc/mariadb0" );
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println( "[에러] " + e.getMessage() );
		}
	}
	
	/* 전체 조회 메서드 */
	public ArrayList<ManagerTO> getList(int cpage,int recordPerPage) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getList DAO 호출");
		
		ArrayList<ManagerTO> datas = new ArrayList<ManagerTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "SELECT u.nickname, u.mail, u.created_at, u.auth_type, count(b.seq)"
					+ " from user u left outer join board b"
					+ " on u.nickname = b.nickname group by u.nickname order by u.created_at asc limit ?,?";
			
			pstmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			pstmt.setInt(1, (cpage-1)*10);
			pstmt.setInt(2, recordPerPage);
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				ManagerTO to = new ManagerTO();
				to.setNickname( rs.getString( "u.nickname" ) );
				to.setMail(rs.getString("u.mail"));
				to.setCreatedAt( rs.getString( "u.created_at" ) );
				to.setAuthType(rs.getString("u.auth_type"));
				to.setCount(rs.getString("count(b.seq)"));
				datas.add( to );
			}
		} catch( SQLException e ) {
			System.out.println( "[dao.getList 에러] " + e.getMessage() );
		} finally {
			if( rs != null ) try { rs.close(); } catch( SQLException e ) {}
			if( pstmt != null ) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null ) try { conn.close(); } catch( SQLException e ) {}
		}
		return datas;
	}
	/* 페이지 메서드 */
	public BoardListTO boardList(BoardListTO listTO, String field,String keyword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("boardList DAO 호출");
		System.out.println(field + "/"+keyword);
		if (field.equals("u.mail")) {
			field = "mail";
		}else if(field.equals("u.nickname")) {
			field = "nickname";
		}
		
		int cpage = listTO.getCpage();
		int recordPerPage = listTO.getRecordPerPage();
		int blockPerPage = listTO.getBlockPerPage();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select nickname, mail, date_format(created_at, '%Y.%m.%d') created_at, auth_type from user where "+field+" like ? "
					+ "order by created_at asc";
			pstmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
	        pstmt.setString(1, "%"+keyword+"%"); //키워드도 없으면 초기값인 공란을 가져옴
	         
			rs = pstmt.executeQuery();
			
			rs.last();
			listTO.setTotalRecord( rs.getRow() );
			rs.beforeFirst();
			
			listTO.setTotalPage( ( ( listTO.getTotalRecord() -1 ) / recordPerPage ) + 1 );
			
			int skip = (cpage -1)* recordPerPage;
			if( skip != 0 ) rs.absolute( skip );
			
			ArrayList<ManagerTO> userLists = new ArrayList<ManagerTO>();
			for( int i=0 ; i<recordPerPage && rs.next() ; i++ ) {
				ManagerTO to = new ManagerTO();
				to.setNickname( rs.getString( "nickname" ) );
				to.setMail(rs.getString("mail"));
				to.setCreatedAt( rs.getString( "created_at" ) );
				to.setAuthType(rs.getString("auth_type"));
				
				userLists.add( to );
			}
			
			listTO.setUserLists( userLists );
			
			listTO.setStartBlock( ( ( cpage -1 ) / blockPerPage ) * blockPerPage + 1 );
			listTO.setEndBlock( ( ( cpage -1 ) / blockPerPage ) * blockPerPage + blockPerPage );
			if( listTO.getEndBlock() >= listTO.getTotalPage() ) {
				listTO.setEndBlock( listTO.getTotalPage() );
			}
		} catch(SQLException e) {
			System.out.println( "[dao.boardList 에러] " + e.getMessage() );
		} finally {
			if( rs != null ) try { rs.close(); } catch( SQLException e ) {}
			if( pstmt != null ) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null ) try { conn.close(); } catch( SQLException e ) {}
		}
		
		return listTO;
	}
	/* field와 keyword를 통한 검색 메서드 */
	public ArrayList<ManagerTO> getSearchList(String field, String keyword, int cpage,int recordPerPage){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getSearchList DAO 호출");
		
		 ArrayList<ManagerTO> datas = new  ArrayList<ManagerTO>();
		 
		 try {
			 conn = this.dataSource.getConnection();
			 
			//String sql = "select * from user where "+field+" like ? order by created_at desc limit ?,?";
			 String sql = "SELECT u.nickname, u.mail, u.created_at, u.auth_type, count(b.seq)"
						+ " from user u left outer join board b"
						+ " on u.nickname = b.nickname where "+field+" like ? group by u.nickname order by u.created_at asc limit ?,?";
				
			 pstmt=conn.prepareStatement(sql);
			 pstmt.setString(1, "%"+keyword+"%");
			 pstmt.setInt(2, (cpage-1)*10);
			 pstmt.setInt(3, recordPerPage);
			 
			 rs = pstmt.executeQuery();
			 
			 while(rs.next()) {
				ManagerTO to = new ManagerTO();
				to.setNickname( rs.getString( "u.nickname" ) );
				to.setMail(rs.getString("u.mail"));
				to.setCreatedAt( rs.getString( "u.created_at" ) );
				to.setAuthType(rs.getString("u.auth_type"));
				to.setCount(rs.getString("count(b.seq)"));
				datas.add( to );
			 }
		 }catch (SQLException e) {
			 System.out.println( "[dao.getSearchList 에러] " + e.getMessage() );
			} finally {
				if( rs != null ) try { rs.close(); } catch( SQLException e ) {}
				if( pstmt != null ) try { pstmt.close(); } catch( SQLException e ) {}
				if( conn != null ) try { conn.close(); } catch( SQLException e ) {}
			}
		return datas;
	}
	
		/* ---------- user 회원 구분 변경하기 ---------- */
	
	public int modifyOK(String nickname, String status) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.modifyOK 연결 성공");
		
		int result = 0;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql ="update user set auth_type='"+status+"' where nickname in("+nickname+")";
			
			pstmt = conn.prepareStatement(sql);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[dao.thumviewOK 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return result;
	}
	/* ---------- 날짜 계산 ---------- */
	public ArrayList<ManagerTO> dateSearch(String startDate, String endDate, int cpage,int recordPerPage){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.dateSearch 연결 성공");
		
		ArrayList<ManagerTO> datas = new ArrayList<ManagerTO>();
		try {
			conn = this.dataSource.getConnection();
			
			//String sql = "select * from user where created_at between ? and ? order by created_at desc limit ?,?";
			 String sql = "SELECT u.nickname, u.mail, u.created_at, u.auth_type, count(b.seq)"
						+ " from user u left outer join board b"
						+ " on u.nickname = b.nickname where u.created_at between ? and ? group by u.nickname order by u.created_at asc limit ?,?";
			 
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			pstmt.setInt(3, (cpage-1)*10);
			pstmt.setInt(4, recordPerPage);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ManagerTO to = new ManagerTO();
				to.setNickname( rs.getString( "u.nickname" ) );
				to.setMail(rs.getString("u.mail"));
				to.setCreatedAt( rs.getString( "u.created_at" ) );
				to.setAuthType(rs.getString("u.auth_type"));
				to.setCount(rs.getString("count(b.seq)"));
				datas.add( to );
			}
		}catch(SQLException e) {
			System.out.println("[dao.dateSearch 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return datas;
	}
	/* ---------- 총 회원 계산 ---------- */
	public int getCount(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.getCount 연결 성공");
		int countlist=0;
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select count(*) from user";
			 
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				countlist = rs.getInt("count(*)");
			}
			
		}catch(SQLException e) {
			System.out.println("[dao.getCount 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return countlist;
	}
	/* ---------- 기간 검색 건수 계산 ---------- */
	public BoardListTO getPagingByDate(BoardListTO listTO, String field, String keyword, String startDate,
			String endDate) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int cpage = listTO.getCpage();
		int recordPerPage = listTO.getRecordPerPage();
		int blockPerPage = listTO.getBlockPerPage();

		try {
			conn = this.dataSource.getConnection();
			String sql = "select nickname, mail, created_at, auth_type from user "
						+ "where "+field+" like ? "
						+ "and created_at between ? and ? order by created_at asc";
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();

			rs.last();
			listTO.setTotalRecord(rs.getRow());
			System.out.println(rs.getRow());
			rs.beforeFirst();

			listTO.setTotalPage(((listTO.getTotalRecord() - 1) / recordPerPage) + 1);

			int skip = (cpage - 1) * recordPerPage;
			if (skip != 0)
				rs.absolute(skip);

			listTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
			listTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
			if (listTO.getEndBlock() >= listTO.getTotalPage()) {
				listTO.setEndBlock(listTO.getTotalPage());
			}
		} catch (SQLException e) {
			System.out.println("[에러] " + e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}

		return listTO;
	}
}

