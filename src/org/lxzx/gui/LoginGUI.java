package org.lxzx.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import org.lxzx.email.User;

import org.lxzx.email.Settings;

import org.lxzx.email.MailReceiver;;

public class LoginGUI extends JDialog {
	


	MainGUI mainGui;
	
	private JLabel userNameLabel;
	private JTextField userNameField;
	private JLabel pwdLabel;
	private JPasswordField  pwdField;
	private JLabel serverLabel;
	private JComboBox<String> server;
	JCheckBox save;
	private JButton login,cancel;
    private JPanel jp1,jp2,jp3,jp4,jp5;	
    
    private User user;
    private Settings settings;

    
    
	private void init(){
		this.userNameLabel = new JLabel("�û�����");
		this.userNameField = new JTextField(15);
		this.pwdLabel = new JLabel(" ���룺  ");
		this.pwdField = new JPasswordField(15);
		this.serverLabel = new JLabel("�����������           ");
		server = new JComboBox<String>();
		server.addItem("@163.com");
		server.addItem("@qq.com");
		server.addItem("@gmail.com");
		server.addItem("@sina.cn");
		this.save = new JCheckBox("��ס�˺�");
		this.login = new JButton("��½");
		this.cancel = new JButton("ȡ��");
		this.setTitle("��½����");
	
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
	
	//���ò���
		this.setLayout(new GridLayout(5,1));
	
		jp1.add(userNameLabel);
		jp1.add(userNameField);
	
		jp2.add(pwdLabel);
		jp2.add(pwdField);
	
		jp3.add(serverLabel);
		jp3.add(server);
		
		jp4.add(save);
	
		jp5.add(login);
		jp5.add(cancel);
	
		//�����������ӵ���½������
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		this.add(jp4);
		this.add(jp5);
		this.setSize(300, 250);
		this.setVisible(true);
		this.settings = new Settings();

		
		this.cancel.addActionListener(new ActionListener(){
			@Override
			 public void actionPerformed(ActionEvent e) {
				System.exit(0);
            }
		});
		this.login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				user = new User(userNameField.getText().concat(server.getSelectedItem().toString()),
						new String(pwdField.getPassword()));
					 	mainGui = new MainGUI(user);
				if(save.isSelected()){
					try {
						user.saveLoginMsg();
						settings.saveNewSettings(user);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
					setVisible(false);
			}
				
		});		
	}
	
	
	public LoginGUI() {
		settings = new Settings();
		File settingfile = new File(settings.getSettingPath());
        BufferedReader reader = null;
        try{
        reader = new BufferedReader(new FileReader(settingfile));
        String userfile = reader.readLine().toString();
        reader.close();
        	user = new User();
        	user.loadLoginMsg(userfile);
        	mainGui = new MainGUI(user);      	
        }catch(IOException e){
		this.init();
        }
	}
	

	


}
