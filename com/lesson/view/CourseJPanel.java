package com.lesson.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lesson.bean.CourseInf;
import com.lesson.ui.MainJFrame;
import com.lesson.utils.GetImage;
import com.lesson.utils.MyColor;
import com.sun.jna.Library.Handler;

public class CourseJPanel {
	JPanel oldJPanel ;
	//整个JPanel布局，显示图片和课程信息
	private JPanel parent;
	//显示课程图片JLabel
	private MyJLable imageJLabel;
	//课程信息
	private CourseInf course;
	//课程名称信息
	private JLabel coursenameJLabel;
	//课程观看人数
	private JLabel watchernumJLabel;
	//课程免费
	private JLabel costJLabel;
	//宽和高
	private int width;
	private int height;
	
	public CourseJPanel(CourseInf course,int width,int height,JPanel rightPictureJPanel) {
		// TODO Auto-generated constructor stub
		this.course = course;
		this.width = width;
		this.height = height;
		initUI(rightPictureJPanel);
	}
	
	public void setImageSize(int width,int height){
		this.width = width;
		this.height = height;
		imageJLabel.setSize(width, height);
	}
	
	private void initUI(final JPanel rightPictureJPanel) {
		//记录下右侧图片界面布局
		oldJPanel = rightPictureJPanel;
		// 初始化parent
		parent = new JPanel();
		parent.removeAll();
		parent.setLayout(new BorderLayout(1,1));
		parent.setBackground(Color.WHITE);
		parent.setPreferredSize(new Dimension(this.width,this.height));
		// 初始化课程信息
		imageJLabel = new MyJLable("/image/pictures_no.png", this.width, this.height, 0, 0);
		imageJLabel.setSize(this.width, this.height);
		imageJLabel.setBackground("/image/pictures_no.png");
		GetImage.showJLabelImageFromUrl(imageJLabel, this.course.getAndroidimage());
		
		//底部课程名和观看人数JPanel
		JPanel bottomJPanel = new JPanel();
		bottomJPanel.setLayout(new GridLayout(3,1));
		bottomJPanel.setBackground(Color.WHITE);
		//初始化课程名称组件
		coursenameJLabel = new JLabel();
		coursenameJLabel.setText("    "+this.course.getCoursename());
		coursenameJLabel.setForeground(Color.BLACK);
		//初始化观看人数组件
		watchernumJLabel = new JLabel();
		watchernumJLabel.setText("    "+this.course.getWatchernum()+"人观看");
		watchernumJLabel.setForeground(MyColor.FONT_GRAY);
		//初始化消费组件
		costJLabel = new JLabel();
		costJLabel.setText("    免费");
		costJLabel.setForeground(MyColor.FONT_ORAGNE);
		//底部课程信息添加组件
		bottomJPanel.add(coursenameJLabel);
		bottomJPanel.add(watchernumJLabel);
		bottomJPanel.add(costJLabel);
		// parent添加组件
		parent.add(new JLabel("    "),BorderLayout.WEST);
		parent.add(new JLabel("    "),BorderLayout.EAST);
		parent.add(imageJLabel,BorderLayout.CENTER);
		parent.add(bottomJPanel,BorderLayout.SOUTH);
	}
	
	public JPanel getUI(){
		return this.parent;
	}
	
	public MyJLable getImageJLabel(){
		return this.imageJLabel;
	}
	
}
