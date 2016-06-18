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
	 		outBoxMailReciever.recieveMail(user, "�ѷ���");
	 		draftMailReciever.recieveMail(user, "�ݸ��");
	 		junkMailReciever.recieveMail(user, "�����ʼ�");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		init();
	}
	
	 //������������   
	private JTree createTree() {   
		//�������ڵ�  
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();   
		//��������ӽڵ�   
		DefaultMutableTreeNode in_box_node = new DefaultMutableTreeNode("�ռ���");
		DefaultMutableTreeNode out_box_node = new DefaultMutableTreeNode("������");
		DefaultMutableTreeNode draft_box_node = new DefaultMutableTreeNode("�ݸ���");
		DefaultMutableTreeNode junk_box_node = new DefaultMutableTreeNode("������");
		
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
		//������
		JTree tree = new JTree(root);
		tree.setRootVisible(false);
		return tree;
	}

	
	private void init(){
		this.setTitle("����ͻ���");
		this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�趨����رպ��Զ��˳����� 
        this.setSize(800,600);//�趨�����Ĭ�ϳߴ�    
        this.setVisible(true);//��ʾ����
        
		this.fromLabel = new JLabel("    �����������ַ��");
		this.fromField = new JTextField(25);
		this.toLabel = new JLabel("    �ռ��������ַ��");
		this.toField = new JTextField(25);
		this.subjectLabel = new JLabel("    �ʼ����⣺");
		this.subjectField = new JTextField(20);
		this.contentLabel = new JLabel("    �ʼ����ģ� ");
		this.contentArea = new JEditorPane();

		//�ŵ����������в��ܹ����鿴��������
		JScrollPane scrollPane1 = new JScrollPane(contentArea);
		contentArea.setContentType("text/html");



		fromField.setEditable(false);
		contentArea.setEditable(false);
        toField.setEditable(false);
        subjectField.setEditable(false);
        
        this.currentUserLabel = new JLabel("��ӭ�㣺"+ this.userName);
        this.fogetUserMsg = new JButton("����û���Ϣ");
        this.sendMails = new JButton("д�ʼ�");
        this.fresh = new JButton("ˢ��ҳ��");
        fogetUserMsg.setPreferredSize(new Dimension(100,30)); 
        sendMails.setPreferredSize(new Dimension(100,30)); 

        
        jSplitPane = new JSplitPane();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel(new BorderLayout());
        jPanel3 = new JPanel(new GridLayout(4, 2, 5, 5));
        jPanel4 = new JPanel();
        
        this.jSplitPane.setDividerLocation(220);//�趨�ָ��������ұ���
		JScrollPane scrollPane2 = new JScrollPane(jPanel1);
        jSplitPane.add(scrollPane2, JSplitPane.LEFT);
        jSplitPane.add(jPanel2, JSplitPane.RIGHT);
        tree = createTree();
        jPanel1.add(tree);

        
        //grid ���֣�������
        jPanel3.add(fromLabel);
        jPanel3.add(fromField);
        jPanel3.add(toLabel);
        jPanel3.add(toField);
        jPanel3.add(subjectLabel);
        jPanel3.add(subjectField);
        jPanel3.add(contentLabel);
        jPanel2.add(jPanel3,BorderLayout.NORTH);
        
        //�����ʼ����Ĳ���
        jPanel2.add(scrollPane1,BorderLayout.CENTER);
        
        //�Ϸ��İ�ť����
        jPanel4.add(currentUserLabel);
        jPanel4.add(sendMails);
        jPanel4.add(fogetUserMsg);
        jPanel4.add(fresh);
        
        this.getContentPane().add(jSplitPane, BorderLayout.CENTER);
        this.getContentPane().add(jPanel4,BorderLayout.NORTH);

        //tree Ԫ�ش�����
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
        
        //���ð�ť������
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
        	 		outBoxMailReciever.recieveMail(newUser, "�ѷ���");
        	 		draftMailReciever.recieveMail(newUser, "�ݸ��");
        	 		junkMailReciever.recieveMail(newUser, "�����ʼ�");
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
