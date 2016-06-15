package org.lxzx.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.Socket;



public class MailSender {
	private String smtpServer;
	private int port = 25;

	private Socket socket;
	BufferedReader br;
	PrintWriter pw;
	
	/**
	 * ���ݷ����˵������ַȷ��SMTP�ʼ������� 
	 */
	private void initServer(String from) {
		if(from.contains("@163")) {
			this.smtpServer = "smtp.163.com";
		}else if(from.contains("@126")) {
			this.smtpServer = "smtp.126.com";
		}else if(from.contains("@sina")) {
			this.smtpServer = "smtp.sina.com";
		}else if(from.contains("@qq")) {
			this.smtpServer = "smtp.qq.com";
		}
	}

	public void sendEmail(Email email) {
		try {
			this.initServer(email.from);
			
			this.socket = new Socket(smtpServer, port);
			this.br = this.getReader(socket);
			this.pw = this.getWriter(socket);
			
			// ��ʼ��װ�����ʼ�����������
			send_Receive(null);		// ��������SMTP�������ɹ�����Ϣ
			send_Receive("ehlo hao");
			send_Receive("auth login");
			send_Receive(email.userName);
			send_Receive(email.pwd);
			send_Receive("mail from:<" + email.from + ">");
			send_Receive("rcpt to:<" + email.to + ">");
			send_Receive("data");
			
			// �ʼ�����
			pw.println("from:" + email.from);
			pw.println("to:" + email.to);
			// ����������֮��һ��Ҫ��һ�У�������"\r\n"
			pw.println("subject:" + email.subject + "\r\n");
			
			// �ڿ���̨��ӡ�ʼ�����
			System.out.println("from:" + email.from);
			System.out.println("to:" + email.to);			
			System.out.println("subject:" + email.subject + "\r\n");
			System.out.println(email.content);
			
			// �ʼ�����
			pw.println(email.content);
			
			// һ���ǵ�������"."����
			send_Receive(".");
			send_Receive("quit");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  ÿ����һ���������������������"\r\n",
	 *  ��ͬʱ��ӡ��smtp�ʼ�����������Ӧ״̬��
	 * @param command
	 */
	private void send_Receive(String command) throws IOException{
		if(command != null) {
			// ��SMTP�ʼ��������������һ��Ҫ�ǵü���"\r\n"
			pw.print(command + "\r\n");
			pw.flush();
			System.out.println("�û� >> " + command);
		}
		
		char [] response = new char[1024];
			br.read(response);
			System.out.println(response);
	}

	/**
	 * ��ȡ Socket �������
	 */
	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	/**
	 * ��ȡ Socket ��������
	 */
	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

}
