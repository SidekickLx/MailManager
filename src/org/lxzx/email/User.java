package org.lxzx.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class User {
	private String username;
	private String password;
	public User(){
	}
	
	public User(String username,String password){
		this.username = username;
		this.password = password;		
	}
	public String GetUserName(){
		return username;
		
	}
	public String GetPassWord(){
		return password;
	}
	
	public String GetTraveledUserName(){
		return this.toBASE64(username);
	}
	
	public String GetTraveledPassWord(){
		return this.toBASE64(password);
	}
	
	private String toBASE64(String str) {		
		return (new sun.misc.BASE64Encoder().encode(str.getBytes()));		
	}
	
	private String BASE64to(String str){
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
        try {  
            byte[] b = decoder.decodeBuffer(str);  
            return new String(b);  
        } catch (Exception e) {  
            return null;  
        }
	}
	
	public void saveLoginMsg()throws IOException{
		String path = new String("D:\\MailManager/users/"+GetTraveledUserName()+".msg");
		File file = new File(path);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file);
        StringBuffer usr = new StringBuffer(GetTraveledUserName()+"\n");
        StringBuffer psw = new StringBuffer(GetTraveledPassWord());
        out.write(usr.toString().getBytes("UTF-8"));
        out.write(psw.toString().getBytes("UTF-8"));
        out.close();
	}
	
	public void loadLoginMsg(String path)throws IOException{
		
		File file = new File("D:\\MailManager/users/"+path);
        if(!file.exists())
            file.createNewFile();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            this.username = BASE64to(reader.readLine().toString());
            this.password = BASE64to(reader.readLine().toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
}
