package model1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class NormalBoardDAO {
private DataSource dataSource;
	
	public NormalBoardDAO() { 
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
	//카테고리 이름 가져오는것(쓸지안쓸지 고민)
	public CategoryTO getCategoryName(CategoryTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
			try {
				conn = this.dataSource.getConnection();
				String sql="select seq, name from board_category where seq=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,to.getSeq());
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					to.setSeq(rs.getString("seq"));
					to.setName(rs.getString("name"));
				}
				
				System.out.println("dao name"+to.getName());
				System.out.println("dao seq"+to.getSeq());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("에러:"+e.getMessage());
			}finally {
				if(rs != null) try {rs.close();} catch(SQLException e) {}
				if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
				if(conn != null) try {conn.close();} catch(SQLException e) {}
			}
				
				return to;
			}
	
	
	
	public BoardTO normalBoardView(BoardTO to){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "update board set hit = hit + 1 where seq=?";
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, to.getSeq() );
			
			pstmt.executeUpdate();
			
			//내용 보기
			sql = "select * from board where seq=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setLikeCount(rs.getString("like_count"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setContent(rs.getString("content"));
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("에러:"+e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
			if(conn != null) try {conn.close();} catch(SQLException e) {}
		}
			return to;
		}
	
	
	
	public int normalBoardDelete(BoardTO to) {
		
		int flag = 2; //데이터 오류
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = this.dataSource.getConnection();
			// 본인확인용 컬럼 추가 
			String sql = "delete from board where seq=? and nickname=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			pstmt.setString(2, to.getNickname());
			
			System.out.println(to.getSeq());
			System.out.println(to.getNickname());
		
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
	
	//수정시 쓰기에 뜨도록
	public BoardTO modify(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn =this.dataSource.getConnection();
			
			String sql ="select * from board where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,to.getSeq());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setNickname(rs.getString("nickname"));
				to.setLikeCount(rs.getString("like_count"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setContent(rs.getString("content"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				
			}
			
			
		} catch (SQLException e) {
			System.out.println("[에러]"+e.getMessage());
		} finally {
			if(rs!=null) try{rs.close();} catch(SQLException e) {};
			if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
			if(conn!=null) try{conn.close();} catch(SQLException e) {};
		}
		
		return to;
	}
	
	public int modifyOk(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 2;
		
		try {
			conn =this.dataSource.getConnection();
			
			String sql = "update board set title=?, sub_title=?, family_member_type=?,"
					+ " content=?, updated_at=now(),is_private=? where seq=? and nickname=?";
			pstmt = conn.prepareStatement(sql);
			
			// 본인확인용 컬럼추가
			pstmt.setString(1,to.getTitle());
			pstmt.setString(2,to.getSubTitle());
			pstmt.setString(3,to.getFamilyMemberType());
			pstmt.setString(4,to.getContent());
			pstmt.setBoolean(5,to.isPrivate());
			pstmt.setString(6,to.getSeq());
			pstmt.setString(7,to.getNickname());
			
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
	
	
	//해당 글이 board에 남아있는지 확인
	public int isSeq(String seq) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 0;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select count(*) as count from board where seq=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("count") == 1) {
					flag=1;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[에러]"+e.getMessage());
		} finally {
			if(rs!=null) try{rs.close();} catch(SQLException e) {};
			if(pstmt!=null) try{pstmt.close();} catch(SQLException e) {};
			if(conn!=null) try{conn.close();} catch(SQLException e) {};
		}
		
		return flag;
		
	}
}
