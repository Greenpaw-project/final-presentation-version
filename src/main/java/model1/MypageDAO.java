package model1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MypageDAO {
	
	private DataSource dataSource;

	public MypageDAO() {
		// TODO Auto-generated constructor stub
	
		try {
			Context ic = new InitialContext();
			Context evc = (Context)ic.lookup("java:comp/env");
			this.dataSource = (DataSource)evc.lookup("jdbc/mariadb0");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
public MypageTO getUserInfo(MypageTO to) {
				
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select auth_type, created_at, from_social from user where nickname =? and mail = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			pstmt.setString( 2, to.getMail() );
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				to.setAuthType(rs.getString("auth_type"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setFromSocial(rs.getBoolean("from_social"));
			}
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		return to;
	}
	
public MypageTO getPostCount(MypageTO to) {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		conn = this.dataSource.getConnection();
		
		String sql = "select count(seq) as count from board where nickname = ?";
		
		pstmt = conn.prepareStatement( sql );
		pstmt.setString( 1, to.getNickname() );
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			to.setTotalPostCount(rs.getInt("count"));
		}
		
		ArrayList<Integer> counts = new ArrayList<Integer>();
		
		for(int i = 1; i<=6; i++) {
		
			sql = "select count(seq) as count from board where nickname = ? and category_seq=?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			pstmt.setInt( 2, i );
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				counts.add(rs.getInt("count"));
			}
		
		}
		
		to.setPostCat1(counts.get(0));
		to.setPostCat2(counts.get(1));
		to.setPostCat3(counts.get(2));
		to.setPostCat4(counts.get(3));
		to.setPostCat5(counts.get(4));
		to.setPostCat6(counts.get(5));
		
		
	} catch( SQLException e ) {
		System.out.println( "[에러] " + e.getMessage() );
	} finally {
		if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
		if( rs != null ) try { rs.close(); } catch(SQLException e) {}
		if( conn != null ) try { conn.close(); } catch(SQLException e) {}
	}
	
	return to;
	
	}

public MypageTO getCommentCount(MypageTO to) {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		conn = this.dataSource.getConnection();
		
		String sql = "select count(seq) as count from comment where nickname = ?";
		
		pstmt = conn.prepareStatement( sql );
		pstmt.setString( 1, to.getNickname() );
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			to.setTotalCommentCount(rs.getInt("count"));
		}
		
		ArrayList<Integer> counts = new ArrayList<Integer>();
		
		for(int i = 1; i<=6; i++) {
		
			sql = "select count(seq) as count from comment where nickname = ? and board_seq=?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			pstmt.setInt( 2, i );
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				counts.add(rs.getInt("count"));
			}
		
		}
		
		to.setCommentCount1(counts.get(0));
		to.setCommentCount2(counts.get(1));
		to.setCommentCount3(counts.get(2));
		to.setCommentCount4(counts.get(3));
		to.setCommentCount5(counts.get(4));
		to.setCommentCount6(counts.get(5));
		
		
	} catch( SQLException e ) {
		System.out.println( "[에러] " + e.getMessage() );
	} finally {
		if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
		if( rs != null ) try { rs.close(); } catch(SQLException e) {}
		if( conn != null ) try { conn.close(); } catch(SQLException e) {}
	}
	
	return to;
	
	}
	

	public int changePassword(MypageTO to) {

		int flag = 0;
		
		UserDAO dao = new UserDAO();
		String EncryptOldPs = dao.sha256(to.getOldPassword());
		String EncryptNewPs = dao.sha256(to.getNewPassword());
		
		/*
		 * System.out.println("oldPs: "+to.getOldPassword());
		 * System.out.println("newPs: "+to.getNewPassword());
		 * System.out.println("EncryptOldPs: "+EncryptOldPs);
		 * System.out.println("EncryptNewPs: "+EncryptNewPs);
		 */
				
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "update user set password = ?, updated_at = now() where password = ? and nickname = ? and mail = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, EncryptNewPs );
			pstmt.setString( 2, EncryptOldPs );
			pstmt.setString( 3, to.getNickname() );
			pstmt.setString( 4, to.getMail() );
			
			flag = pstmt.executeUpdate();
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		//System.out.println(flag);
		
		return flag;
	}

	public int deleteAccount(MypageTO to) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 2;
		
		//비밀번호 암호화
		UserDAO dao = new UserDAO();
		String EncryptPs = dao.sha256(to.getOldPassword());
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "update user set auth_type = '탈퇴', updated_at=now() where password = ? and nickname = ? and mail = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, EncryptPs );
			pstmt.setString( 2, to.getNickname() );
			pstmt.setString( 3, to.getMail() );
			
			flag = pstmt.executeUpdate();
			System.out.println("dao flag: "+ flag);
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		return flag;
	}
	
	
	public int deleteKaKaoAccount(MypageTO to) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 2;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "update user set auth_type = '탈퇴', updated_at=now() where from_social = 1 and nickname = ? and mail = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			pstmt.setString( 2, to.getMail() );
			
			flag = pstmt.executeUpdate();
			System.out.println("dao flag: "+ flag);
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		return flag;
	}
	
	public MypageTO getNote(MypageTO to) {
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				conn = this.dataSource.getConnection();
				
				String sql = "select note from note where nickname = ?";
	
				pstmt = conn.prepareStatement( sql );
				pstmt.setString( 1, to.getNickname() );
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					to.setNote(rs.getString("note"));
				}else {
					to.setNote("");
				}
				
			} catch( SQLException e ) {
				System.out.println( "[에러] " + e.getMessage() );
			} finally {
				if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
				if( rs != null ) try { rs.close(); } catch(SQLException e) {}
				if( conn != null ) try { conn.close(); } catch(SQLException e) {}
			}
			
			return to;
		}
	
		
	public int addNote(MypageTO to) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = 2;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "delete from note where nickname = ?";

			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			
			pstmt.executeUpdate();
					
			sql = "insert into note values(?, ?)";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getNickname() );
			pstmt.setString( 2, to.getNote() );
			
			result = pstmt.executeUpdate();
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
			
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		return result;
	}
}
