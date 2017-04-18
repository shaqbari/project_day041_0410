/*1. ������ �Ѱ��� �α�
 * -�����ͺ��̽� ���������� �ߺ��ؼ� �������� �ʱ� ����
 * (db������ �ϴ� ������ Ŭ��������)
 * 
 * 2. �ν��Ͻ��� ������ �Ѱ��� �α�!
 * -���ø����̼� ������ �����Ǵ� connection ��ü�� �ϳ��� �����ϱ� ����
 * */

package com.ss.multi.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager { //���⼭ class�� Ŭ���� �����ϴ� �����
	static private DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@sist108:1521:XE";
	private String user="batman";
	private String password="1234";
	
	private Connection con;
	
	
	private DBManager(){
		/*1.����̹� �ε�
		 *2. ����
		 *3. ������ ����
		 *4. �ݳ�, ��ü*/
		try {
			Class.forName(driver); //Class class�� Ŭ���������� ������ ������ �ִ�. � �޼ҵ�� ������ �����Ǿ� �ִ��� �� �� �ִ�.
			con=DriverManager.getConnection(url, user, password);
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	static public DBManager getInstance() {
		if (instance==null) {
			instance=new DBManager();
		}		
		
		return instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void disConnect(Connection con) {
		if (con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
}
