package com.lesson.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.lesson.bean.GOLBALVALUE;
import com.lesson.utils.GetImage;
import com.lesson.utils.LoginAndGetIUserInf;
import com.lesson.utils.MyColor;
import com.lesson.view.MyJLable;

public class LoginJFrame extends JFrame{
	
	private Point point;
	private MyJLable exitJLabel;
	private JTextField usernameJLabel;
	private JPasswordField passwordJLabel;
	private JLabel loginJLabel;
	private JLabel errorJLabel;
	
	public LoginJFrame() {
		// TODO Auto-generated constructor stub
		point = new Point();
		this.setUndecorated(true);
		this.setBounds(500, 200, 300, 250);
		JPanel parent = new JPanel();
		parent.setBackground(Color.WHITE);
		parent.setLayout(new BorderLayout());
		errorJLabel = new JLabel();
		errorJLabel.setForeground(Color.RED);
//		this.setResizable(true);
		exitJLabel = new MyJLable("/image/exit_normal.png", 40, 30, 0, 0);
		exitJLabel.setPreferredSize(new Dimension(40, 30));
		exitJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				LoginJFrame.this.setVisible(false);
			}

			public void mouseEntered(MouseEvent e) {// 鼠标进入
				exitJLabel.setBackground("/image/exit_over.png");
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				exitJLabel.setBackground("/image/exit_normal.png");
			}
		});
		//顶部JPanel
		JPanel titleJPanel = new JPanel();
		titleJPanel.setBackground(Color.WHITE);
		titleJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		titleJPanel.setPreferredSize(new Dimension(0, 30));
		titleJPanel.add(exitJLabel);
		//中部组件添加
		JPanel centerJPanel = new JPanel();
		centerJPanel.setBackground(Color.WHITE);
		centerJPanel.setLayout(new GridLayout(6,1));
		usernameJLabel = new JTextField();
		usernameJLabel.setText("用户名");
		usernameJLabel.setForeground(MyColor.FONT_GRAY);
		passwordJLabel = new JPasswordField();
		passwordJLabel.setText("密码");
		passwordJLabel.setForeground(MyColor.FONT_GRAY);
		centerJPanel.add(new JLabel("    "));
		centerJPanel.add(usernameJLabel);
		centerJPanel.add(new JLabel("    "));
		centerJPanel.add(passwordJLabel);
		centerJPanel.add(errorJLabel);
		centerJPanel.add(new JLabel("    "));
		
		//初始化登录按钮
		loginJLabel = new JLabel("登录",JLabel.CENTER);
		loginJLabel.setPreferredSize(new Dimension(0, 30));
		loginJLabel.setForeground(MyColor.FONT_ORAGNE);
		loginJLabel.setFont(new Font("宋体", 1, 20));
		loginJLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if(usernameJLabel.getText().equals("用户名")||usernameJLabel.getText().equals("")){
					errorJLabel.setText("用户名不能为空");
				}
				if(passwordJLabel.getText().equals("密码")||passwordJLabel.getText().equals("")){
					errorJLabel.setText("密码不能为空");
				}
				if(!passwordJLabel.getText().equals("密码")||!passwordJLabel.getText().equals("")
					&&!usernameJLabel.getText().equals("用户名")||!usernameJLabel.getText().equals("")){
					int result = LoginAndGetIUserInf.login(usernameJLabel.getText(), passwordJLabel.getText());
					switch (result) {
					case 0:
						errorJLabel.setText("连接失败");
						break;
					case 1:
						GetImage.showJLabelImageFromUrl(MainJFrame.headImageJLabel, GOLBALVALUE.user.getHeadimagePath());
						LoginJFrame.this.setVisible(false);
						break;
					case 2:
						errorJLabel.setText("用户名不存在");
						break;
					case 3:
						errorJLabel.setText("密码错误");
						break;

					default:
						break;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				loginJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
				loginJLabel.setForeground(MyColor.FONT_ORAGNE);
			}
		});
		//添加组件
		parent.add(titleJPanel,BorderLayout.NORTH);
		parent.add(centerJPanel,BorderLayout.CENTER);
		parent.add(new JLabel("    "),BorderLayout.WEST);
		parent.add(new JLabel("    "),BorderLayout.EAST);
		parent.add(loginJLabel,BorderLayout.SOUTH);
		//JFrame可以移动
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				// TODO Auto-generated method stub
				super.mousePressed(event);
				point.x = event.getX();
				point.y = event.getY();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				// TODO Auto-generated method stub
				super.mouseDragged(event);
				Point p = LoginJFrame.this.getLocation();
				LoginJFrame.this.setLocation(p.x + event.getX() - point.x, p.y
						+ event.getY() - point.y);
			}
		});
		this.add(parent);
		this.setVisible(true);
	}
	
}
