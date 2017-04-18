/*하위 카테고리에 등록된 상품 정보 제공 모델*/

package com.ss.bread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DownModel extends AbstractTableModel{
	Connection con;
	
	Vector<String> colName=new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();
	
	public DownModel(Connection con) {
		this.con=con;
		
		//최초 생성할때 고정시키자
		colName.add("product_id");
		colName.add("sub_category_id");
		colName.add("product_name");
		colName.add("product_price");
		colName.add("product_img");
		
		
	}
	
	//마우스로 유저가 클릭할때마다 id값이 바뀌므로,
	//아래의 메소드를 그때마다 호출하자.
	public void getList(int sub_category_id){
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
				
		String sql="select * from product";
		sql+=" where sub_category_id=?";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, sub_category_id);//첫번째 물음표는 1
			rs=pstmt.executeQuery();
			
			//먼저 벡터들 초기화
			colName.removeAll(colName);
			data.removeAll(data);
			
		/*	ResultSetMetaData meta=rs.getMetaData();
			int count=meta.getColumnCount();
			for (int i = 1; i <=count; i++) {
				colName.add(meta.getColumnName(i));
			}*/
			System.out.println("getList 컬럼의 크기는 "+colName.size());
			
			while(rs.next()){
				Vector vec= new Vector();
				vec.add(rs.getInt("product_id")); //boxing으로 들어간다. 기본자료형->객체자료형
				vec.add(rs.getString("sub_category_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("product_price"));
				vec.add(rs.getString("product_img"));
				
				data.add(vec);
				System.out.println(data.get(0)+"getList안");
				
			}			
			System.out.println("getList 레코드의 크기는 "+data.size());
			
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
	
	//실험해보면 new할때만 호출된다. list얻오올때마다 테이블모델을 new하거나 고정시키자
	public int getColumnCount() {		
		System.out.println("컬럼의 갯수는 "+colName.size());
		return colName.size();
	}

	public int getRowCount() {
		System.out.println("레코드의 갯수는 "+data.size());
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Object value=data.get(row).get(col);
		System.out.println("getValueAt 호출 "+value); //객체는 출력의 대상이 될 수 없지만, toString()이 자동호출되어 주소값이나 기타 정보가 나올 수 있다.
		return data.get(row).get(col);
	}

}
