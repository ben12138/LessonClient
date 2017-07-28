package com.lesson.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.lesson.view.MyJLable;

public class GetImage {

	private static int i = 0;
	private static lunboThread lunbo = null;
	
	public static void showJLabelImageFromUrl(final MyJLable jlabel,final String imageUrl){
		new Thread(){
			public void run() {
				URL url ;
				try {
					url = new URL(imageUrl);
					jlabel.setBackground(url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public static void lunboImage(final MyJLable jlabel,final String[] urls){
		lunbo = new lunboThread(jlabel, urls, 0);
		lunbo.start();
	}
	
	public static void beforeImage(){
		lunbo.beforeImage();
	}
	
	public static void nextImage(){
		lunbo.nextImage();
	}
	
}

class lunboThread extends Thread{
	
	private MyJLable jlabel;
	private String[] urls;
	private int i;
	
	public lunboThread(MyJLable jlabel,String[] urls,int i) {
		// TODO Auto-generated constructor stub
		this.jlabel = jlabel;
		this.urls = urls;
		this.i = i;
	}
	
	public void run() {
		while(true){
			if(this.i == 6){
				this.i = 0;
			}
			URL url ;
			try {
				url = new URL(urls[i]);
				jlabel.setBackground(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.i++;
		}
	};
	
	public void beforeImage(){
		if(this.i == 0){
			this.i = 5;
		}else{
			this.i--;
		}
		URL url ;
		try {
			url = new URL(urls[i]);
			jlabel.setBackground(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void nextImage(){
		if(this.i == 6){
			this.i = 0;
		}else{
			this.i++;
		}
		URL url ;
		try {
			url = new URL(urls[i]);
			jlabel.setBackground(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
