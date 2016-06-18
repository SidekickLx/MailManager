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
	private StringBuffer bodytext = new StringBuffer();//����ʼ�����  
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
		//����ط�Ӧ��try catch��
		store.connect(this.pop3Server, user.GetUserName(), user.GetPassWord());
		Folder folder = store.getFolder(box);
		
//		Folder defaultFolder = store.getDefaultFolder();	
//		Folder[] allFolder = defaultFolder.list();
//		for(int i = 0;i<allFolder.length;i++)
//			System.out.println(allFolder[i]);
		
		folder.open(Folder.READ_ONLY);  
		Message messages[] = folder.getMessages();  
		for (int i=0, n=messages.length; i<n; i++) {    
		       //��ȡ�ʼ�������Ϣ
			 mimeMessage = (MimeMessage) messages[i];  
			 fromAddress.add(getFrom());
			 toAddress.add(getMailAddress("TO"));
			 mID.add(getMessageId());
			 subject.add(getSubject());
//	         System.out.println("�� " + (i+1) + "���ʼ������⣺" + getSubject() );  
//	         System.out.println("�� " + (i+1) + "���ʼ��ķ����˵�ַ��" + getFrom() );               
	         getMailContent((Part) messages[i]);  
	         content.add(this.getBodyText());
//	         System.out.println("Message " + i + " bodycontent: \r\n"  
//	                    + getBodyText());
	         //this.writeMailInFiles(String.valueOf(i),this.getBodyText());
	         
		}  
		//�ر�����  
		folder.close(false);
		store.close();  
	}
	
	/**  
     * ��ô��ʼ���Message-ID  
     */  
    public String getMessageId() throws MessagingException {   
        return mimeMessage.getMessageID();  
    }   
	
	/**  
     * ��÷����˵ĵ�ַ������  
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
     * ����ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ "to"----�ռ��� "cc"---�����˵�ַ "bcc"---�����˵�ַ  
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
     * ����ʼ�����  
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
     * ����ʼ���������  
     */  
    public String getBodyText() {   
        return bodytext.toString();   
    }
    
    /**  
     * �����ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ��Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ���  
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
	     * �洢�ʼ������ʼ����Ĵ洢���ļ�  ��
	     * ��ʱ���Բ���
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
