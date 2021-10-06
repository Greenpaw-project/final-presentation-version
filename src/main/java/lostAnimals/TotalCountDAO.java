package lostAnimals;

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

public class TotalCountDAO {

	private DataSource dataSource;
	
	 public TotalCountDAO() {
		// TODO Auto-generated constructor stub
		 
		 try {
				Context ic = new InitialContext();
				Context evc = (Context)ic.lookup("java:comp/env");
				this.dataSource = (DataSource)evc.lookup("jdbc/mariadb0");
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 };
	
	public ArrayList<TotalCountTO> getTotalCountFromDB(ArrayList<TotalCountTO> list) {
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<TotalCountTO> datas = new ArrayList<TotalCountTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select sidoShort, gugun, totalCounts, x, y from location_code where sidoShort =? and gugun = ?";
			pstmt = conn.prepareStatement( sql );
			
			for(TotalCountTO to : list) {
				pstmt.setString(1, to.getSidoShort());
				pstmt.setString(2, to.getGugun());
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					to.setSidoShort(rs.getString("sidoShort"));
					to.setGugun(rs.getString("gugun"));
					to.setTotalCounts(rs.getString("totalCounts"));
					to.setX(rs.getString("x"));
					to.setY(rs.getString("y"));
				}
				
				datas.add(to);
			}
			
		} catch( SQLException e ) {
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch(SQLException e) {}
			if( rs != null ) try { rs.close(); } catch(SQLException e) {}
			if( conn != null ) try { conn.close(); } catch(SQLException e) {}
		}
		
		return datas;
		
	}

}
