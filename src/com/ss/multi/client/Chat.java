/*�� Ŭ������ �����ͺ��̽� ���̺� Chat��� ���ڵ� 1���� ������� Ŭ�����μ�
 * �̷��� ������ ��ü�� ������ DTO, VO�� �Ѵ�.
 * 
 * �繰�� �����ϴ� �繰�� ���� ǥ��
 * ���                          db
 * class          -->   Entity(����)   (���̺�)
 * instance     -->   ���ڵ�
 * �Ӽ�              -->   �÷�
 * 
 * */

package com.ss.multi.client;

public class Chat {//==table, 
	private int chat_id;
	private String name;
	private String ip;
	
	public int getChat_id() {
		return chat_id;
	}
	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
}
