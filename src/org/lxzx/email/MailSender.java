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
	 * 根据发件人的邮箱地址确定SMTP邮件服务器 
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
			
			// 开始组装发送邮件的命令序列
			send_Receive(null);		// 接收连接SMTP服务器成功的信息
			send_Receive("ehlo hao");
			send_Receive("auth login");
			send_Receive(email.userName);
			send_Receive(email.pwd);
			send_Receive("mail from:<" + email.from + ">");
			send_Receive("rcpt to:<" + email.to + ">");
			send_Receive("data");
			
			// 邮件内容
			pw.println("from:" + email.from);
			pw.println("to:" + email.to);
			// 主题与正文之间一定要空一行，即加上"\r\n"
			pw.println("subject:" + email.subject + "\r\n");
			
			// 在控制台打印邮件内容
			System.out.println("from:" + email.from);
			System.out.println("to:" + email.to);			
			System.out.println("subject:" + email.subject + "\r\n");
			System.out.println(email.content);
			
			// 邮件正文
			pw.println(email.content);
			
			// 一定记得正文以"."结束
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
	 *  每发送一条命令，必须在命令后面加上"\r\n",
	 *  则同时打印出smtp邮件服务器的相应状态码
	 * @param command
	 */
	private void send_Receive(String command) throws IOException{
		if(command != null) {
			// 向SMTP邮件服务器发送命令，一定要记得加上"\r\n"
			pw.print(command + "\r\n");
			pw.flush();
			System.out.println("用户 >> " + command);
		}
		
		char [] response = new char[1024];
			br.read(response);
			System.out.println(response);
	}

	/**
	 * 获取 Socket 的输出流
	 */
	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	/**
	 * 获取 Socket 的输入流
	 */
	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

}
