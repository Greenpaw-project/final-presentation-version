package lostAnimals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ApiDAO {

	private DataSource dataSource;

	// 생성자로 연결
	public ApiDAO() {
		// TODO Auto-generated constructor stub
		try {
			Context ic = new InitialContext();
			Context evc = (Context) ic.lookup("java:comp/env");
			this.dataSource = (DataSource) evc.lookup("jdbc/mariadb0");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getCode( String sido, String gugun) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//System.out.println(" DAO sido / gugun : "+ sido +" / "+gugun);
        
		if (gugun.contains(" ")) {
            String[] tmp = gugun.split(" ");
            //System.out.println(tmp[0]);
            switch ( tmp[0]) {
                case "용인시":
                    if (!tmp[1].equals("기흥구")) {
                        gugun = tmp[0];
                    }
                    break;
                case "창원시":
                    if (tmp[1].equals("마산회원구") || tmp[1].equals("마산합포구")) {
                        gugun = "창원시 마산합포회원구";
                    } else if (tmp[1].equals("진해구")) {
                        gugun = "창원시 진해구";
                    } else {
                        // 위의 두 경우가 아닐때 창원은 아래값으로 항상 디폴트
                        gugun = "창원시 의창성산구";
                    }
                    break;
                default:
                    gugun = tmp[0];
            }
            
            //System.out.println(" 띄어쓰기 있는 애들 gugun "+ gugun);
        }
		
		String[] result = new String[2];
		
		try {
			conn = this.dataSource.getConnection();
			
			String sql = "select sidoCode, gugunCode from location_code where sido like ? and gugun=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sido + "%");
			pstmt.setString(2, gugun);
			
			rs = pstmt.executeQuery();
			
			if ( !rs.next()) {
				// 결과값이 없을 때
				return null;
			}
			// 정상 실행했을 때 배열 초기화
			result[0] = rs.getString("sidoCode");
			result[1] = rs.getString("gugunCode");
			
			//System.out.println("sidocode / guguncode : "+result[0]+" / "+result[1]);
			
		} catch (SQLException e) {
			System.out.println("[error] "+e.getMessage());
		} finally {
			if (rs!=null) try { rs.close(); } catch(SQLException e) {}
			if (pstmt!=null) try { pstmt.close(); } catch(SQLException e) {}
			if (conn!=null) try { conn.close(); } catch(SQLException e) {}
		}
		return result;
	}
}
