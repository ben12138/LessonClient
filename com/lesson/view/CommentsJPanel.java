package com.lesson.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lesson.bean.Comments;
import com.lesson.utils.GetComments;
import com.lesson.utils.GetImage;
import com.lesson.utils.MyColor;

public class CommentsJPanel {
	
	private JPanel parent;//父布局
	private Comments comment;//评论
	private MyJLable imageJLabel;//头像JLabel
	private JLabel usernameJLabel;//发送人昵称
	private JLabel sendTimeJLabel;//发送时间JLabel
	private MyJLable praiseJLabel;//点赞图标
	private JLabel praisenumJLabel;//点赞数量
	private JLabel contentJLabel;//消息内容
	
	public CommentsJPanel(Comments comment) {
		// TODO Auto-generated constructor stub
		this.comment = comment;
		init();
	}
	
	private void init(){
		parent = new JPanel();
		parent.removeAll();
		parent.setLayout(new BorderLayout());
		parent.setBackground(MyColor.BACKGROUND_DARK);
		//添加用户名和头像部分
		JPanel titleJPanel = new JPanel();
		titleJPanel.setPreferredSize(new Dimension(0, 30));
		titleJPanel.setLayout(new BorderLayout());
		imageJLabel = new MyJLable("", 30, 30, 10, 10);
		imageJLabel.setPreferredSize(new Dimension(30, 30));
		GetImage.showJLabelImageFromUrl(imageJLabel, comment.getSenderheadImage());
		usernameJLabel = new JLabel("    "+comment.getSenderNickName());
		usernameJLabel.setForeground(MyColor.FONT_GRAY);
		titleJPanel.add(imageJLabel,BorderLayout.WEST);
		titleJPanel.add(usernameJLabel,BorderLayout.CENTER);
		//添加中部评论信息部分
		JPanel centerJPanel = new JPanel();
		contentJLabel = new JLabel(comment.getContent());
		contentJLabel.setForeground(MyColor.FONT_GRAY);
//		centerJPanel.add(new JLabel("  "),BorderLayout.WEST);
//		centerJPanel.add(new JLabel("  "),BorderLayout.EAST);
		centerJPanel.add(contentJLabel,BorderLayout.EAST);
		//添加点赞和点赞数量部分
		JPanel bottomJPanel = new JPanel();
		bottomJPanel.setPreferredSize(new Dimension(0, 20));
		bottomJPanel.setLayout(new BorderLayout());
		//发表时间
		JLabel sendTimeJLabel = new JLabel("    "+comment.getSendtime());
		sendTimeJLabel.setForeground(MyColor.FONT_GRAY);
		//点赞
		JPanel praiseJPanel = new JPanel();
		praiseJPanel.setLayout(new BorderLayout());
		praiseJPanel.setPreferredSize(new Dimension(50, 20));
		praiseJPanel.setBackground(MyColor.BACKGROUND_DARK);
		praisenumJLabel = new JLabel();
		praisenumJLabel.setPreferredSize(new Dimension(10, 20));
		praiseJLabel = new MyJLable("/image/praise_normal.png", 20, 20, 0, 0);
		praiseJLabel.addMouseListener(new MouseAdapter() {
			boolean isclicked = false;
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				praiseJLabel.setBackground("/image/praise_pressed.png");
				if(isclicked){
					
				}else{
					if(GetComments.praise(comment.getId())){
						praisenumJLabel.setText(comment.getPraisenum()+1+"");
					}
				}
				isclicked = !isclicked;
			}
			
			public void mouseEntered(MouseEvent e) {// 鼠标进入
				praiseJLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				praiseJLabel.setCursor(Cursor.getDefaultCursor());
			}
		});
		praisenumJLabel.setText(comment.getPraisenum()+"");
		praisenumJLabel.setForeground(MyColor.FONT_GRAY);
		praiseJPanel.add(praiseJLabel,BorderLayout.CENTER);
		praiseJPanel.add(praisenumJLabel,BorderLayout.WEST);
		
		bottomJPanel.add(sendTimeJLabel,BorderLayout.CENTER);
		bottomJPanel.add(praiseJPanel,BorderLayout.EAST);
		//添加组件
		titleJPanel.setBackground(MyColor.BACKGROUND_DARK);
		centerJPanel.setBackground(MyColor.BACKGROUND_DARK);
		bottomJPanel.setBackground(MyColor.BACKGROUND_DARK);
		parent.add(titleJPanel,BorderLayout.NORTH);
		parent.add(centerJPanel,BorderLayout.CENTER);
		parent.add(bottomJPanel,BorderLayout.SOUTH);
	}
	
	public JPanel getUI(){
		return this.parent;
	}
	
}
