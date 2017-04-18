/*���� ī�װ��� ��ϵ� ��ǰ ���� ���� ��*/

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
		
		//���� �����Ҷ� ������Ű��
		colName.add("product_id");
		colName.add("sub_category_id");
		colName.add("product_name");
		colName.add("product_price");
		colName.add("product_img");
		
		
	}
	
	//���콺�� ������ Ŭ���Ҷ����� id���� �ٲ�Ƿ�,
	//�Ʒ��� �޼ҵ带 �׶����� ȣ������.
	public void getList(int sub_category_id){
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
				
		String sql="select * from product";
		sql+=" where sub_category_id=?";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, sub_category_id);//ù��° ����ǥ�� 1
			rs=pstmt.executeQuery();
			
			//���� ���͵� �ʱ�ȭ
			colName.removeAll(colName);
			data.removeAll(data);
			
		/*	ResultSetMetaData meta=rs.getMetaData();
			int count=meta.getColumnCount();
			for (int i = 1; i <=count; i++) {
				colName.add(meta.getColumnName(i));
			}*/
			System.out.println("getList �÷��� ũ��� "+colName.size());
			
			while(rs.next()){
				Vector vec= new Vector();
				vec.add(rs.getInt("product_id")); //boxing���� ����. �⺻�ڷ���->��ü�ڷ���
				vec.add(rs.getString("sub_category_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("product_price"));
				vec.add(rs.getString("product_img"));
				
				data.add(vec);
				System.out.println(data.get(0)+"getList��");
				
			}			
			System.out.println("getList ���ڵ��� ũ��� "+data.size());
			
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
	
	//�����غ��� new�Ҷ��� ȣ��ȴ�. list����ö����� ���̺���� new�ϰų� ������Ű��
	public int getColumnCount() {		
		System.out.println("�÷��� ������ "+colName.size());
		return colName.size();
	}

	public int getRowCount() {
		System.out.println("���ڵ��� ������ "+data.size());
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Object value=data.get(row).get(col);
		System.out.println("getValueAt ȣ�� "+value); //��ü�� ����� ����� �� �� ������, toString()�� �ڵ�ȣ��Ǿ� �ּҰ��̳� ��Ÿ ������ ���� �� �ִ�.
		return data.get(row).get(col);
	}

}
