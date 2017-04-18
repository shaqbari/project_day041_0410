/*������ ���� Ŭ���̾�Ʈ�� 1:1�� ��ȭ�� ���� ������*/

package com.ss.multi.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ServerThread extends Thread{
	ServerMain main;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	Boolean flag=true;
	
	public ServerThread(Socket socket, JTextArea area, ServerMain main) {
		this.main=main;
		this.socket=socket;
		this.area=area;		
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//Ŭ���̾�Ʈ �޼��� �ޱ�
	public void listen(){
						
		try{
			String msg=buffr.readLine();			
			area.append(msg+"\n");
			send(msg); //�ٽú�����
			
		} catch (IOException e) {//���ڱ� client�� ���������� �� ���ܿ� ��� �ɸ���.
			System.out.println("�б�Ұ�");
			flag=false;//���� ������ ���̱�
			
			//���Ϳ��� �� �����带 ����
			main.list.remove(this);
			
			main.area.append("1�� ������ ���� ������"+main.list.size()+"\n");			
			//e.printStackTrace();
			
			//���⼭ stream�ݾ�?
		}		
		
	}
	
	public void send(String msg){
		try {
			//���� ������ �� ���ο��� ������ �Ѵ�.
			//���������� ��:
			for (int i = 0; i < main.list.size(); i++) {
				ServerThread st=main.list.elementAt(i);
				st.buffw.write(msg+"\n");
				st.buffw.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		
		while (flag) {
			listen();
		}
	
	}
}
