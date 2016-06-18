package org.lxzx.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import org.lxzx.email.Email;
import org.lxzx.email.MailReceiver;
import org.lxzx.email.User;

public class MainGUI extends JFrame {
	


	private MailSenderGUI mailSenderGui;
    private MailReceiver inBoxMailReciever;
    private MailReceiver outBoxMailReciever;
    private MailReceiver draftMailReciever;
    private MailReceiver junkMailReciever;
	
    private JTree tree;
	private JLabel fromLabel;
	private JTextField fromField;
	private JLabel toLabel;
	private JTextField toField;
	private JLabel subjectLabel;
	private JTextField subjectField;
	private JLabel contentLabel;
	private JEditorPane contentArea;

	
	private JLabel currentUserLabel;
	
	private JButton fogetUserMsg;
	private JButton sendMails;
	private JButton fresh;
	
	
    private JSplitPane jSplitPane;   
    private JPanel jPanel1;   
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    
    
    private String userName;
    private String passWord;

	
	public MainGUI(User user){
		this.userName = user.GetUserName();
		this.passWord = user.GetPassWord();
	 	inBoxMailReciever = new MailReceiver();
	 	outBoxMailReciever = new MailReceiver();
	 	draftMailReciever = new MailReceiver();
	 	junkMailReciever = new MailReceiver();
	 	try {
	 		inBoxMailReciever.recieveMail(user,"INBOX");
	 		outBoxMailReciever.recieveMail(user, "已发送");
	 		draftMailReciever.recieveMail(user, "草稿夹");
	 		junkMailReciever.recieveMail(user, "垃圾邮件");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		init();
	}
	
	 //创建导航的树   
	private JTree createTree() {   
		//创建根节点  
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();   
		//加入各个子节点   
		DefaultMutableTreeNode in_box_node = new DefaultMutableTreeNode("收件箱");
		DefaultMutableTreeNode out_box_node = new DefaultMutableTreeNode("发件箱");
		DefaultMutableTreeNode draft_box_node = new DefaultMutableTreeNode("草稿箱");
		DefaultMutableTreeNode junk_box_node = new DefaultMutableTreeNode("垃圾箱");
		
		root.add(in_box_node);   
		root.add(out_box_node);    
		root.add(draft_box_node);  
		root.add(junk_box_node);
		for(int i=0;i<inBoxMailReciever.getsubjectList().size();i++){
			in_box_node.add(new DefaultMutableTreeNode(new Email(inBoxMailReciever.getFromAddress(i), 
					inBoxMailReciever.getToAddress(i),
					inBoxMailReciever.getSubject(i),
					inBoxMailReciever.getContentList(i)
					)));

		}
		
		for(int i=0;i<outBoxMailReciever.getsubjectList().size();i++){
		out_box_node.add(new DefaultMutableTreeNode(new Email(outBoxMailReciever.getFromAddress(i), 
				outBoxMailReciever.getToAddress(i),
				outBoxMailReciever.getSubject(i),
				outBoxMailReciever.getContentList(i)
				)));
		}
		
		for(int i=0;i<draftMailReciever.getsubjectList().size();i++){
			draft_box_node.add(new DefaultMutableTreeNode(new Email(draftMailReciever.getFromAddress(i), 
				draftMailReciever.getToAddress(i),
				draftMailReciever.getSubject(i),
				draftMailReciever.getContentList(i)
				)));
		}
		
		for(int i=0;i<junkMailReciever.getsubjectList().size();i++){
			junk_box_node.add(new DefaultMutableTreeNode(new Email(junkMailReciever.getFromAddress(i), 
				junkMailReciever.getToAddress(i),
				junkMailReciever.getSubject(i),
				junkMailReciever.getContentList(i)
				)));
		}		
		//创建树
		JTree tree = new JTree(root);
		tree.setRootVisible(false);
		return tree;
	}

	
	private void init(){
		this.setTitle("邮箱客户端");
		this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设定窗体关闭后自动退出进程 
        this.setSize(800,600);//设定窗体的默认尺寸    
        this.setVisible(true);//显示窗体
        
		this.fromLabel = new JLabel("    发件人邮箱地址：");
		this.fromField = new JTextField(25);
		this.toLabel = new JLabel("    收件人邮箱地址：");
		this.toField = new JTextField(25);
		this.subjectLabel = new JLabel("    邮件主题：");
		this.subjectField = new JTextField(20);
		this.contentLabel = new JLabel("    邮件正文： ");
		this.contentArea = new JEditorPane();

		//放到滚动窗格中才能滚动查看所有内容
		JScrollPane scrollPane1 = new JScrollPane(contentArea);
		contentArea.setContentType("text/html");



		fromField.setEditable(false);
		contentArea.setEditable(false);
        toField.setEditable(false);
        subjectField.setEditable(false);
        
        this.currentUserLabel = new JLabel("欢迎你："+ this.userName);
        this.fogetUserMsg = new JButton("清除用户信息");
        this.sendMails = new JButton("写邮件");
        this.fresh = new JButton("刷新页面");
        fogetUserMsg.setPreferredSize(new Dimension(100,30)); 
        sendMails.setPreferredSize(new Dimension(100,30)); 

        
        jSplitPane = new JSplitPane();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel(new BorderLayout());
        jPanel3 = new JPanel(new GridLayout(4, 2, 5, 5));
        jPanel4 = new JPanel();
        
        this.jSplitPane.setDividerLocation(220);//设定分割面板的左右比例
		JScrollPane scrollPane2 = new JScrollPane(jPanel1);
        jSplitPane.add(scrollPane2, JSplitPane.LEFT);
        jSplitPane.add(jPanel2, JSplitPane.RIGHT);
        tree = createTree();
        jPanel1.add(tree);

        
        //grid 布局，在右上
        jPanel3.add(fromLabel);
        jPanel3.add(fromField);
        jPanel3.add(toLabel);
        jPanel3.add(toField);
        jPanel3.add(subjectLabel);
        jPanel3.add(subjectField);
        jPanel3.add(contentLabel);
        jPanel2.add(jPanel3,BorderLayout.NORTH);
        
        //右下邮件正文部分
        jPanel2.add(scrollPane1,BorderLayout.CENTER);
        
        //上方的按钮部分
        jPanel4.add(currentUserLabel);
        jPanel4.add(sendMails);
        jPanel4.add(fogetUserMsg);
        jPanel4.add(fresh);
        
        this.getContentPane().add(jSplitPane, BorderLayout.CENTER);
        this.getContentPane().add(jPanel4,BorderLayout.NORTH);

        //tree 元素触发器
        tree.addMouseListener(new MouseAdapter() {
			 
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
                if (node == null)
                    return;
            	Object object = node.getUserObject();
                if(node.isLeaf()){
                	//System.out.println(tree.getSelectionPath());
                	Email email = (Email)object;
                	fromField.setText(email.getFrom());
                	toField.setText(email.getTo());
                	subjectField.setText(email.getSubject());
                	contentArea.setText(email.getContent());
                }
            }
        });
        
        //设置按钮触发器
        this.sendMails.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e){
        		mailSenderGui = new MailSenderGUI(userName,passWord);

        	}
        });     
        this.fogetUserMsg.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e){   		
        		File settings = new File("D:\\MailManager/settings.set");
        		settings.delete();
        	}
        });
        
        this.fresh.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e){   		
        		//TODO
        		User newUser = new User(userName,passWord);
        	 	inBoxMailReciever = new MailReceiver();
        	 	outBoxMailReciever = new MailReceiver();
        	 	draftMailReciever = new MailReceiver();
        	 	junkMailReciever = new MailReceiver();
        	 	try {
        	 		inBoxMailReciever.recieveMail(newUser,"INBOX");
        	 		outBoxMailReciever.recieveMail(newUser, "已发送");
        	 		draftMailReciever.recieveMail(newUser, "草稿夹");
        	 		junkMailReciever.recieveMail(newUser, "垃圾邮件");
        		} catch (Exception e2) {
        			// TODO Auto-generated catch block
        			e2.printStackTrace();
        		}
        		init();
        		repaint();
        	}
        });
		
	}
	
}
