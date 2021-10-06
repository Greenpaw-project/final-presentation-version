package model1;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;


public class UserDAO {
	
	private DataSource dataSource;
	
	public UserDAO() { //생성자로 DB 연결
		// TODO Auto-generated constructor stub
		try {	
			Context ic = new InitialContext();
			Context evc =(Context)ic.lookup("java:comp/env");
			this.dataSource = (DataSource)evc.lookup("jdbc/mariadb0");
		
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/************************민지님*********************/
	//패스워드 암호화
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
	
	
	//서비스 로그인 ok
	public int logInOk(UserTO to) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result = 2;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "Select count(*) from user where mail =? and password =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getMail());
			pstmt.setString(2, to.getPassword());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("count(*)");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			if(rs!=null) try{rs.close();} catch(SQLException e) {};
			if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
			if(conn!=null) try{conn.close();} catch(SQLException e) {};
		}
		
		//확인
		System.out.println(result+" (1: 로그인 성공 / 0: 로그인 실패)");
		
		return result;
	};
	
	//세션 데이터 -> 로그인 정보 넣기
	public UserTO getSessionData(UserTO to) {
	
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "Select mail, nickname, auth_type from user where mail =? and password =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getMail());
			pstmt.setString(2, to.getPassword());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				to.setMail(rs.getString("mail"));
				to.setNickname(rs.getString("nickname"));
				to.setAuthType(rs.getString("auth_type"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			if(rs!=null) try{rs.close();} catch(SQLException e) {};
			if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
			if(conn!=null) try{conn.close();} catch(SQLException e) {};
		}
		
		return to;
	}
	
	
	public int forgotPassword(UserTO to) {
		
		Connection conn =null;
		PreparedStatement pstmt = null;
		
		int result = 2;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "update user set password = ?, updated_at = now() where mail =? and hint_password =? and answer_password =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getPassword());
			pstmt.setString(2, to.getMail());
			pstmt.setString(3, to.getHintPassword());
			pstmt.setString(4, to.getAnswerPassword());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
			if(conn!=null) try{conn.close();} catch(SQLException e) {};
		}
		
		
		return result;
	}
	
	
	/****************** 소연님 *******************/
	
	//카카오정보가 db에 있는지 확인
	public int kakaoIdCheck(UserTO to) {
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result=0;

		try{
			//데이터 연결
		
		conn = this.dataSource.getConnection();
		
		String sql = "select nickname, mail from user where mail=? and from_social=1";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, to.getMail());
		
		rs = pstmt.executeQuery();
		
		//있으면 1 
		if(rs.next()){
			result=1;
		//없으면 0
		} else {
			result =0;
		}
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
		} finally{
			if(rs != null) try{rs.close();} catch(SQLException e){}
			if(pstmt != null) try{pstmt.close();} catch(SQLException e){}
			if(conn != null) try{conn.close();} catch(SQLException e){}
		}
	
		return result;
		}
	
	//카카오로그인이 처음일 때 가입
	public int kakaoJoin(UserTO to) {
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result =0;	
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "insert into user (from_social,mail,nickname,auth_type,hint_password,answer_password,created_at,updated_at) values(1,?,?,'회원','','',now(),now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getMail());
			pstmt.setString(2, to.getNickname());
			
			result = pstmt.executeUpdate();
			
				//1: 성공
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(rs != null) try{rs.close();} catch(SQLException e){}
			if(pstmt != null) try{pstmt.close();} catch(SQLException e){}
			if(conn != null) try{conn.close();} catch(SQLException e){}
		}
		return result;	
	}
	
	//카카오 닉네임이 중복 되는지 확인
	public int kakaoNicknamecheck(UserTO to) {
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result=0;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select count(*) as count from user where nickname=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getNickname());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("count");
			}
			
			System.out.println(to.getNickname());
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(rs != null) try{rs.close();} catch(SQLException e){}
			if(pstmt != null) try{pstmt.close();} catch(SQLException e){}
			if(conn != null) try{conn.close();} catch(SQLException e){}
		}
			return result;
	}
	
	//카카오 유저 닉네임 가져오기
	public UserTO loginInformation(String email){
		UserTO to = new UserTO();
		
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select nickname,auth_type from user where mail=? and from_social=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				to.setNickname(rs.getString("nickname"));
				to.setAuthType(rs.getString("auth_type"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(rs != null) try{rs.close();} catch(SQLException e){}
			if(pstmt != null) try{pstmt.close();} catch(SQLException e){}
			if(conn != null) try{conn.close();} catch(SQLException e){}
		}
		
		return to;
	}
	
	//탈퇴시 소셜인지 아닌지 확인->이거로.. 판단후에 카카오 로그아웃(아직 구현은 나중에 생각을..)
	public int socialCheck(String nickname) {
		//소셜:1 / 일반: 0
		int result = 0;
		
		UserTO to = new UserTO();
			
			Connection conn =null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			
			try {
				conn = dataSource.getConnection();
				
				String sql = "select from_social from user where mail=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, nickname);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = 1;
					return result;
				} 
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}finally{
				if(rs != null) try{rs.close();} catch(SQLException e){}
				if(pstmt != null) try{pstmt.close();} catch(SQLException e){}
				if(conn != null) try{conn.close();} catch(SQLException e){}
			}
			System.out.println(result);
		return result;
		
	}
}
