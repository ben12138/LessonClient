package com.lesson.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.lesson.net.NetConnection;

public class MyJLable extends JLabel{
	private ImageIcon icon;
	private Image img;
	private int width,height;
	private int startX,startY;

	public MyJLable(String path) {
		// /img/HomeImg.jpg 是存放在你正在编写的项目的bin文件夹下的img文件夹下的一个图片
		icon = new ImageIcon(getClass().getResource(path));
		img = icon.getImage();
		this.width = this.getWidth();
		this.height = this.getHeight();
		this.startX = 0;
		this.startY = 0;
	}
	
	public MyJLable(String path,int width,int height,int startX,int startY) {
		// /img/HomeImg.jpg 是存放在你正在编写的项目的bin文件夹下的img文件夹下的一个图片
		icon = new ImageIcon(getClass().getResource(path));
		img = icon.getImage();
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.setBounds(this.startX, this.startY, this.width, this.height);
//		System.out.println(width+" "+height+" "+this.startX+" "+this.startY+" "+img);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
//		System.out.println(this.width+" "+this.height+" "+this.startX+" "+this.startY+" "+img);
		g.drawImage(img, this.startX, this.startY, width-this.startX,height-this.startY, this);
		
	}
	
	public void setSize(int width,int height){
		this.width = width;
		this.height = height;
		repaint();
	}
	
	public void setBackground(String path){
		icon = new ImageIcon(getClass().getResource(path));
		img = icon.getImage();
		repaint();
	}
	
	public void setBackground(URL url){
		icon = new ImageIcon(url);
		img = icon.getImage();
		repaint();
	}
	
	public void setBackground(String path,int type1){
		icon = new ImageIcon(path);
		img = icon.getImage();
		repaint();
	}
	
}
