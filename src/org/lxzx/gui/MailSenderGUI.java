package org.lxzx.gui;

import org.lxzx.email.MailSender;
import org.lxzx.email.Email;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MailSenderGUI extends JFrame implements ActionListener {
	
	private JLabel fromLabel;
	private JTextField fromField;
	private JLabel toLabel;
	private JTextField toField;
	private JLabel subjectLabel;
	private JTextField subjectField;
	private JLabel contentLabel;
	private JTextArea contentArea;

	private JButton sendBtn;
	private JButton cancelBtn;

	private Email email;

	private MailSender mailSender;
	
    private String userName;
    private String passWord;

	public MailSenderGUI(String userName,String password) {
		this.userName = userName;
		this.passWord = password;
		this.init();
		this.mailSender = new MailSender();
	}

	private void init() {
		this.fromLabel = new JLabel("    发件人邮箱地址：");
		this.fromField = new JTextField(25);
		fromField.setText(userName);
		fromField.setEditable(false);

		this.toLabel = new JLabel("    收件人邮箱地址：");
		this.toField = new JTextField(25);
		this.subjectLabel = new JLabel("    邮件主题：");
		this.subjectField = new JTextField(20);
		this.contentLabel = new JLabel("    邮件正文：");
		this.contentArea = new JTextArea(15, 20);

		this.setTitle("写邮件");
		this.setBounds(200, 30, 500, 500);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		this.sendBtn = new JButton("发送");
		this.cancelBtn = new JButton("返回");

		this.sendBtn.addActionListener(this);
		this.cancelBtn.addActionListener(this);

		JPanel upPanel = new JPanel(new GridLayout(6, 2, 5, 5));
		upPanel.add(fromLabel);
		upPanel.add(fromField);
		upPanel.add(toLabel);
		upPanel.add(toField);
		upPanel.add(subjectLabel);
		upPanel.add(subjectField);
		upPanel.add(contentLabel);
		
		this.add(upPanel, BorderLayout.NORTH);		
		this.add(contentArea, BorderLayout.CENTER);
		
		JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		downPanel.add(sendBtn, BorderLayout.SOUTH);
		downPanel.add(cancelBtn, BorderLayout.SOUTH);
		this.add(downPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.sendBtn) {
			this.email = new Email(
					this.userName,
					this.toField.getText(), 
					this.subjectField.getText(),
					this.contentArea.getText(),
					this.userName,
					this.passWord
					);
			this.mailSender.sendEmail(this.email);
		} else if (e.getSource() == this.cancelBtn) {
			this.setVisible(false);
		}

	}
	
	public static void main(String[] args){
		new LoginGUI();
	}
}

