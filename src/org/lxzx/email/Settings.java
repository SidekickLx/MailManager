package org.lxzx.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Settings {
	private String path = "D:\\MailManager/settings.set";
	private FileOutputStream out;
	
	public Settings(){
		
	}
	
	public String getSettingPath(){
		return path;
	}
	
	public void saveNewSettings(User user) throws IOException{
		File settingfile = new File("D:\\MailManager/settings.set");
        if(!settingfile.exists())
        	settingfile.createNewFile();
        out = new FileOutputStream(settingfile);
        StringBuffer usr = new StringBuffer(user.GetTraveledUserName()+".msg");
        out.write(usr.toString().getBytes("UTF-8"));
	}
	
}
