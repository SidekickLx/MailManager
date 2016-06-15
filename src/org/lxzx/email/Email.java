package org.lxzx.email;

public class Email {
	String from;
	String to;
	String subject;
	String content;
	String userName;
	String pwd;

	public Email(String from, String to, String subject, 
			String content, String userName, String pwd) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.userName = this.toBASE64(userName);
		this.pwd = this.toBASE64(pwd);
	}
	
	public Email(String from, String to, String subject, 
			String content){
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.userName = null;
		this.pwd = null;
	}
	
	/**
	 * �� E_Mail ���н����û����������ת�빤��
	 */
	private String toBASE64(String str) {
		return (new sun.misc.BASE64Encoder().encode(str.getBytes()));		
	}
	
	public String getFrom(){
		return from;
	}
	public String getTo(){
		return to;
	}
	public String getSubject(){
		return subject;
	}
	public String getContent(){
		return content;
	}
	/**
	 * ��дtoString �ڵ���ʾ����tostring
	 */
    public String toString() {
        return subject;
    }
	

}
