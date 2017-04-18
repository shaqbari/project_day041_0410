/*하위 카테고리와 그 카테고리에 등록된 상품의 수 정보를 제공하는 모델
 * 
 * 
 * */

package com.ss.bread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class UpModel extends AbstractTableModel{
	Connection con;
	
	Vector<String> colName =new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();
	
	public UpModel(Connection con) {
		this.con=con;
		getList();
	}
	
	
	//목록 가져오기 //갱신등의 필요시 호출하기 위해
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
			
		StringBuffer sql=new StringBuffer();
		sql.append("select s.SUB_CATEGORY_ID as 카테고리아이디, s.SUB_CATEGORY_NAME as 카테고리명, count(p.PRODUCT_ID) as 갯수");
		sql.append(" from SUB_CATEGORY s LEFT outer JOIN PRODUCT p");
		sql.append(" on s.SUB_CATEGORY_ID = p.SUB_CATEGORY_ID");
		sql.append(" group by s.SUB_CATEGORY_ID, s.SUB_CATEGORY_NAME");
		sql.append(" order by s.SUB_CATEGORY_ID");	
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//벡터들을 초기화
			colName.removeAll(colName);
			data.removeAll(data);
			
			//컬럼명 추출
			ResultSetMetaData meta=rs.getMetaData();
			int count=meta.getColumnCount();
			for (int i = 1; i <=count; i++) {
				colName.add(meta.getColumnName(i));//컬럼명은 1부터 시작한다.
			}
			
					
			while (rs.next()) {
				//레코드 1건을 벡터에 옮겨심자
				//역기서 벡터는 DTo역할 왜냐하면 table 모델이 dto지원x
				Vector<String> vec=new Vector<String>();
				vec.add(rs.getString("카테고리아이디"));
				vec.add(rs.getString("카테고리명"));
				vec.add(rs.getString("갯수"));
				
				data.add(vec);				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}		
		
	}
	
	public String getColumnName(int col) {
		return colName.get(col);
	}

	public int getColumnCount() {
		return colName.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).elementAt(col);
	}

}
