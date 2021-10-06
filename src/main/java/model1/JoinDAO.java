package model1;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JoinDAO {
	private DataSource dataSource;
	
	public JoinDAO() {
		// TODO Auto-generated constructor stub
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup( "java:comp/env" );
			this.dataSource = (DataSource)envCtx.lookup( "jdbc/mariadb0" );
			
			System.out.println("DAO 연결 성공");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println( "[에러] " + e.getMessage() );
		}
	}
	
	public int getNickname(UserTO user){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getUser() : "+user.getNickname());
		int flag = 1;
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select * from user where nickname = ?";
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, user.getNickname() );
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// nickname 중복시 flag = 0
				flag = 0;
			}
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return flag;
	}
	
	public int getMail(UserTO user){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getMail() : "+user.getMail());
		int flag = 1;
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select * from user where mail = ?";
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, user.getMail() );
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// nickname 중복시 flag = 0
				flag = 0;
			}
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return flag;
	}
	
	public String sha256(String password) {
		
		StringBuffer EncryptPs = null;
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			EncryptPs = new StringBuffer();
			
			for(int i = 0; i<hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				
				if(hex.length() ==1) {
					EncryptPs.append('0');
					
				}else {
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
	
	public int joinUser(UserTO user) {
		System.out.println("joinUser() 호출 : "+user.getNickname());
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 0;
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "insert into user values("
					+ "?, 0, ?, ?,"
					+ "'회원', ?, ?, ?,"
					+ "now(), now())";
			pstmt = conn.prepareStatement( sql );
			
			// 최종 DB 확정되고나면 컬럼 순서 확인하고 코드 한번 수정해주기 
			pstmt.setString( 1, user.getNickname() );
			pstmt.setString( 2, user.getMail() );
			pstmt.setString( 3, user.getPassword() );
			pstmt.setString( 4, user.getProfileUrl() );
			pstmt.setString( 5, user.getHintPassword() );
			pstmt.setString( 6, user.getAnswerPassword() );
			
			int result = pstmt.executeUpdate();
			if( result == 1 ) {
				flag = 1;
			}
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return flag;
	}
}
