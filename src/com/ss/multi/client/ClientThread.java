/*Ű�Է��� �ؾ� ������ �޽����� �޴� ������ ����� �����Ѵ�.
 * ���ѷ����� ���缭 ������ �޽����� ���� ���簡 �ʿ��ϸ�
 * ���ѷ����� �����ؾ� �ϹǷ� ������� �����Ѵ�.*/
package com.ss.multi.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread {
	ClientMain clientMain;//Ŭ���̾�Ʈ ������ ��ü�� ����
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	
	public ClientThread(ClientMain clientMain) {
		this. clientMain=clientMain;
		this.socket=clientMain.socket;
		this.area=clientMain.area;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void listen(){
		try {
			String msg=buffr.readLine();
			area.append(clientMain.nickName+"�� ��: "+msg+"\n");			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while (true) {
			listen();
		}
	}
}
