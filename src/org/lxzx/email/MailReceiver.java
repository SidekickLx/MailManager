package org.lxzx.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;   
import javax.mail.internet.*;




public class MailReceiver {
	private String pop3Server;
	private MimeMessage mimeMessage = null;   
	private StringBuffer bodytext = new StringBuffer();//存放邮件内容  
	private ArrayList<String> fromAddress = new ArrayList<String>();
	private ArrayList<String> toAddress = new ArrayList<String>();	
	private ArrayList<String> subject = new ArrayList<String>();
	private ArrayList<String> mID = new ArrayList<String>();
	private ArrayList<String> content = new ArrayList<String>();
	
	public String getFromAddress(int position){
		return fromAddress.get(position).toString();
	}
	
	public String getSubject(int position){
		return subject.get(position).toString();
	}
	
	public String getToAddress(int position){
		return toAddress.get(position).toString();
	}
	
	public ArrayList<String> getfromAddressList(){
		return fromAddress;
	}
	public ArrayList<String> getsubjectList(){
		return subject;
	}
	public String getContentList(int position){
		return content.get(position).toString();
	}
	
	private void initServer(String from) {
		if(from.contains("@163")) {
			this.pop3Server = "imap.163.com";
		}else if(from.contains("@126")) {
			this.pop3Server = "pop3.126.com";
		}else if(from.contains("@sina")) {
			this.pop3Server = "imap.sina.com";
		}else if(from.contains("@qq")) {
			this.pop3Server = "pop3.qq.com";
		}
	}
	public void recieveMail(User user,String box) throws Exception{
		this.initServer(user.GetUserName());
		Properties props = new Properties(); 
		props.setProperty("mail.store.protocol", "imap");
		props.setProperty("mail.imap.host", this.pop3Server);
		props.setProperty("mail.imap.port", "143"); 
		Session session=Session.getInstance(props); 
		Store store = session.getStore("imap");  
		session.setDebug(true);
		//这个地方应该try catch的
		store.connect(this.pop3Server, user.GetUserName(), user.GetPassWord());
		Folder folder = store.getFolder(box);
		
//		Folder defaultFolder = store.getDefaultFolder();	
//		Folder[] allFolder = defaultFolder.list();
//		for(int i = 0;i<allFolder.length;i++)
//			System.out.println(allFolder[i]);
		
		folder.open(Folder.READ_ONLY);  
		Message messages[] = folder.getMessages();  
		for (int i=0, n=messages.length; i<n; i++) {    
		       //获取邮件具体信息
			 mimeMessage = (MimeMessage) messages[i];  
			 fromAddress.add(getFrom());
			 toAddress.add(getMailAddress("TO"));
			 mID.add(getMessageId());
			 subject.add(getSubject());
//	         System.out.println("第 " + (i+1) + "封邮件的主题：" + getSubject() );  
//	         System.out.println("第 " + (i+1) + "封邮件的发件人地址：" + getFrom() );               
	         getMailContent((Part) messages[i]);  
	         content.add(this.getBodyText());
//	         System.out.println("Message " + i + " bodycontent: \r\n"  
//	                    + getBodyText());
	         //this.writeMailInFiles(String.valueOf(i),this.getBodyText());
	         
		}  
		//关闭连接  
		folder.close(false);
		store.close();  
	}
	
	/**  
     * 获得此邮件的Message-ID  
     */  
    public String getMessageId() throws MessagingException {   
        return mimeMessage.getMessageID();  
    }   
	
	/**  
     * 获得发件人的地址和姓名  
     */  
    public String getFrom() throws Exception {   
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();   
        String from = address[0].getAddress();   
        if (from == null)   
            from = "";   
        String personal = address[0].getPersonal();   
        if (personal == null)   
            personal = "";   
        String fromaddr = personal + "<" + from + ">";   
        return fromaddr;   
    }   
    
    /**  
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址  
     */  
    public String getMailAddress(String type) throws Exception {   
        String mailaddr = "";   
        String addtype = type.toUpperCase();   
        InternetAddress[] address = null;   
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {   
            if (addtype.equals("TO")) {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);   
            } else if (addtype.equals("CC")) {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);   
            } else {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);   
            }   
            if (address != null) {   
                for (int i = 0; i < address.length; i++) {   
                    String email = address[i].getAddress();   
                    if (email == null)   
                        email = "";   
                    else {   
                        email = MimeUtility.decodeText(email);   
                    }   
                    String personal = address[i].getPersonal();   
                    if (personal == null)   
                        personal = "";   
                    else {   
                        personal = MimeUtility.decodeText(personal);   
                    }   
                    String compositeto = personal + "<" + email + ">";   
                    mailaddr += "," + compositeto;   
                }   
                mailaddr = mailaddr.substring(1);   
            }   
        } else {   
            throw new Exception("Error emailaddr type!");   
        }   
        return mailaddr;   
    }   
	
    /**  
     * 获得邮件主题  
     */  
    public String getSubject() throws MessagingException {   
        String subject = "";   
        try {   
            subject = MimeUtility.decodeText(mimeMessage.getSubject());   
            if (subject == null)   
                subject = "";   
        } catch (Exception exce) {}   
        return subject;   
    }   
	
    /**  
     * 获得邮件正文内容  
     */  
    public String getBodyText() {   
        return bodytext.toString();   
    }
    
    /**  
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析  
     */  
	
	 public void getMailContent(Part part) throws Exception {   
	        String contenttype = part.getContentType();   
	        int nameindex = contenttype.indexOf("name");   
	        boolean conname = false;   
	        if (nameindex != -1)   
	            conname = true;   
	        System.out.println("CONTENTTYPE: " + contenttype);   
	        if (part.isMimeType("text/plain") && !conname) { 
	        	bodytext.setLength(0);
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("text/html") && !conname) { 
	        	bodytext.setLength(0);
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("multipart/*")) {   
	            Multipart multipart = (Multipart) part.getContent();   
	            int counts = multipart.getCount();   
	            for (int i = 0; i < counts; i++) {   
	                getMailContent(multipart.getBodyPart(i));   
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	            getMailContent((Part) part.getContent());   
	        } else {}   
	    }
	 
	    /**  
	     * 存储邮件，将邮件正文存储到文件  。
	     * 暂时可以不用
	     */  
	 public void writeMailInFiles(String id,String bodytext) throws IOException{
		 String path = new String("D:\\"+ id +".txt");
		 File file=new File(path);
         if(!file.exists())
             file.createNewFile();
         FileOutputStream out=new FileOutputStream(file,true);
		 out.write(bodytext.getBytes("gb18030"));
		 out.close();
	 }
	 

	 
}
