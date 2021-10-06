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



public class ThumbDAO {
	private DataSource dataSource;
	
	public ThumbDAO() {
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
	
	/* categorySeq에 따른 전체 조회 메서드 */
	public ArrayList<BoardTO> getList(String categorySeq, int cpage,int recordPerPage) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getList DAO 호출");
		
		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select * from board where category_seq=? and is_private=0 order by seq desc limit ?, ?";
			pstmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			pstmt.setString(1, categorySeq);
			pstmt.setInt(2, (cpage-1)*10);
			pstmt.setInt(3, recordPerPage);
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setSubTitle( rs.getString( "sub_title" ) );
				to.setSaleStatus( rs.getString( "sale_status" ) );
				to.setFamilyMemberType( rs.getString( "family_member_type" ) );
				to.setTitle( rs.getString( "title" ) );
				to.setContent( rs.getString( "content" ) );
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname( rs.getString( "nickname" ) );
				to.setLikeCount(rs.getString( "like_count" ));
				to.setHit( rs.getString( "hit" ) );
				to.setCreatedAt( rs.getString( "created_at" ) );
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
	public BoardListTO boardList(BoardListTO listTO, String categorySeq, String field,String keyword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("boardList DAO 호출");
		System.out.println(categorySeq + "/" + field + "/"+keyword);
		//pet 이나 plant 클릭 시 필요한 field값 가져오기
		//아니면 초기값인 title을 field값으로 가져옴
		if(field =="type" || field.equals("type")) {
			field = "family_member_type";
		}
		
		int cpage = listTO.getCpage();
		int recordPerPage = listTO.getRecordPerPage();
		int blockPerPage = listTO.getBlockPerPage();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select category_seq, sub_title, sale_status, family_member_type, title, content, thumb_url, nickname, hit, date_format(created_at, '%Y.%m.%d') created_at, updated_at from board where category_seq=? and "+field+" like ? and is_private=0 order by seq desc";
			pstmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			pstmt.setString(1, categorySeq);//추가
	        pstmt.setString(2, "%"+keyword+"%"); //키워드도 없으면 초기값인 공란을 가져옴
	         
			rs = pstmt.executeQuery();
			
			rs.last();
			listTO.setTotalRecord( rs.getRow() );
			rs.beforeFirst();
			
			listTO.setTotalPage( ( ( listTO.getTotalRecord() -1 ) / recordPerPage ) + 1 );
			
			int skip = (cpage -1)* recordPerPage;
			if( skip != 0 ) rs.absolute( skip );
			
			ArrayList<BoardTO> boardLists = new ArrayList<BoardTO>();
			for( int i=0 ; i<recordPerPage && rs.next() ; i++ ) {
				BoardTO to = new BoardTO();
				to.setCategorySeq( rs.getString( "category_seq" ) );
				to.setSubTitle( rs.getString( "sub_title" ) );
				to.setSaleStatus( rs.getString( "sale_status" ) );
				to.setFamilyMemberType( rs.getString( "family_member_type" ) );
				to.setTitle( rs.getString( "title" ) );
				to.setContent( rs.getString( "content" ) );
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname( rs.getString( "nickname" ) );
				to.setHit( rs.getString( "hit" ) );
				to.setCreatedAt( rs.getString( "created_at" ) );
				to.setUpdatedAt( rs.getString( "updated_at" ) );
				
				boardLists.add( to );
			}
			
			listTO.setBoardLists( boardLists );
			
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
	public ArrayList<BoardTO> getSearchList(String categorySeq, String field, String keyword, int cpage,int recordPerPage){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("getSearchList DAO 호출");
		
		if(field == "type" || field.equals("type")) {
			field = "family_member_type";
		}
		
		 ArrayList<BoardTO> datas = new  ArrayList<BoardTO>();
		 
		 try {
			 conn = this.dataSource.getConnection();
			 
			 String sql = "select * from board where category_seq=? and "+field+" like ? and is_private=0 order by seq desc limit ?,?";
			 pstmt=conn.prepareStatement(sql);
			 pstmt.setString(1, categorySeq);
			 pstmt.setString(2, "%"+keyword+"%");
			 pstmt.setInt(3, (cpage-1)*10);
			 pstmt.setInt(4, recordPerPage);
			 
			 rs = pstmt.executeQuery();
			 
			 while(rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq( rs.getString( "seq" ) );
				to.setSaleStatus( rs.getString( "sale_status" ) );
				to.setFamilyMemberType( rs.getString( "family_member_type" ) );
				to.setTitle( rs.getString( "title" ) );
				to.setContent( rs.getString( "content" ) );
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname( rs.getString( "nickname" ) );
				to.setHit( rs.getString( "hit" ) );
				to.setCreatedAt( rs.getString( "created_at" ) );
				datas.add(to);
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
	/* 정렬 메서드 */
	public ArrayList<BoardTO> listSort(String categorySeq,String field,String keyword,String sort,int cpage,int recordPerPage){
		
		
		if(sort == null && sort.equals("")) {
			sort = "seq";
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println("listSort DAO 호출");
		
		ArrayList<BoardTO> datas = new ArrayList<BoardTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select * from board where category_seq=? and "+field+" like ? and is_private=0  order by "+sort+" desc limit ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, categorySeq);
			pstmt.setString(2, "%"+keyword+"%");
			pstmt.setInt(3, (cpage-1)*10);
			pstmt.setInt(4, recordPerPage);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setContent( rs.getString( "content" ) );
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setCreatedAt(rs.getString("created_at"));
				datas.add(to);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[dao.listSort 에러]"+e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
			if(conn != null) try {conn.close();} catch(SQLException e) {}
		}
			return datas;
		}
	
		/* ---------- Board view, modify, delete ---------- */
	
	public BoardTO thumbview(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.thumbview 연결 성공");
		
		try {
			conn = this.dataSource.getConnection();
			
			//조회수 증가
			String sql = "update board set hit=hit+1 where seq=?";
			pstmt = conn.prepareStatement( sql );
			pstmt.setString(1, to.getSeq());
					 
			pstmt.executeUpdate();

			sql ="select * from board where seq=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());

			rs = pstmt.executeQuery();

			
			while(rs.next()) {
				to.setSeq(rs.getString("seq"));
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setContent(rs.getString("content"));
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setUpdatedAt(rs.getString("updated_at"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[dao.thumview 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return to;
	}
	
	public int thumbdelete(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 1;
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "delete from board where nickname=? and category_seq=? and seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getNickname());
			pstmt.setString(2, to.getCategorySeq());
			pstmt.setString(3, to.getSeq());
			
			int result = pstmt.executeUpdate();
			if( result ==1 ) {
				// 성공
				flag = 0;
			}
		}catch( SQLException e ) {
			System.out.println( "[dao.thumbdelete 에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return flag;
	}
	
	public BoardTO thumbmodify(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.thumbview 연결 성공");
		
		try {
			conn = this.dataSource.getConnection();

			String sql ="select * from board where nickname=? and seq=? and category_seq=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getNickname());
			pstmt.setString(2, to.getSeq());
			pstmt.setString(3, to.getCategorySeq());

			rs = pstmt.executeQuery();

			
			if(rs.next()) {
				to.setSeq(rs.getString("seq"));
				to.setSubTitle(rs.getString("sub_title"));
				to.setSaleStatus(rs.getString("sale_status"));
				to.setFamilyMemberType(rs.getString("family_member_type"));
				to.setTitle(rs.getString("title"));
				to.setContent(rs.getString("content"));
				to.setThumbUrl( rs.getString( "thumb_url" ) );
				to.setNickname(rs.getString("nickname"));
				to.setHit(rs.getString("hit"));
				to.setIsPrivate(rs.getBoolean("is_private"));
				to.setCreatedAt(rs.getString("created_at"));
				to.setUpdatedAt(rs.getString("updated_at"));
				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[dao.thumview 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return to;
	}
	
	public int thumbmodifyOK(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("dao.thumbmodifyOK 연결 성공");
		
		int flag = 2;
		
		try {
			conn = this.dataSource.getConnection();

			String sql ="update board set sale_status=?,family_member_type=?,"
					+"title=?,content=?,thumb_url=?,nickname=?,updated_at=now() "
					+"where seq=? and nickname=?";
			
			pstmt = conn.prepareStatement(sql);
	
			pstmt.setString(1, to.getSaleStatus());
			pstmt.setString(2, to.getFamilyMemberType());
			pstmt.setString(3, to.getTitle());
			pstmt.setString(4, to.getContent());
			pstmt.setString(5, to.getThumbUrl());
			pstmt.setString(6, to.getNickname());
			pstmt.setString(7, to.getSeq());
			pstmt.setString(8, to.getNickname());

			int result = pstmt.executeUpdate();
			
			System.out.println("to값 :"+to.getSubTitle());
			System.out.println("to값 :"+to.getSaleStatus());
			System.out.println("to값 :"+to.getFamilyMemberType());
			System.out.println("to값 :"+to.getTitle());
			System.out.println("to값 :"+to.getContent());
			System.out.println("to값 :"+to.getThumbUrl());
			System.out.println("to값 :"+to.getNickname());
			System.out.println("to값 :"+to.getSeq());
			System.out.println("to값 :"+to.getNickname());
			System.out.println("to값 :"+to.getCategorySeq());
			
			if(result == 1) {
				flag = 1; //성공
			}else if(result ==0) {
				flag = 0;
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("[dao.thumviewOK 에러]"+e.getMessage());
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		return flag;
	}
}
