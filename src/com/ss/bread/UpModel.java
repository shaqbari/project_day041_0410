/*���� ī�װ��� �� ī�װ��� ��ϵ� ��ǰ�� �� ������ �����ϴ� ��
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
	
	
	//��� �������� //���ŵ��� �ʿ�� ȣ���ϱ� ����
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
			
		StringBuffer sql=new StringBuffer();
		sql.append("select s.SUB_CATEGORY_ID as ī�װ����̵�, s.SUB_CATEGORY_NAME as ī�װ���, count(p.PRODUCT_ID) as ����");
		sql.append(" from SUB_CATEGORY s LEFT outer JOIN PRODUCT p");
		sql.append(" on s.SUB_CATEGORY_ID = p.SUB_CATEGORY_ID");
		sql.append(" group by s.SUB_CATEGORY_ID, s.SUB_CATEGORY_NAME");
		sql.append(" order by s.SUB_CATEGORY_ID");	
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//���͵��� �ʱ�ȭ
			colName.removeAll(colName);
			data.removeAll(data);
			
			//�÷��� ����
			ResultSetMetaData meta=rs.getMetaData();
			int count=meta.getColumnCount();
			for (int i = 1; i <=count; i++) {
				colName.add(meta.getColumnName(i));//�÷����� 1���� �����Ѵ�.
			}
			
					
			while (rs.next()) {
				//���ڵ� 1���� ���Ϳ� �Űܽ���
				//���⼭ ���ʹ� DTo���� �ֳ��ϸ� table ���� dto����x
				Vector<String> vec=new Vector<String>();
				vec.add(rs.getString("ī�װ����̵�"));
				vec.add(rs.getString("ī�װ���"));
				vec.add(rs.getString("����"));
				
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
