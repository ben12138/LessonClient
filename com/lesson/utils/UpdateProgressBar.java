package com.lesson.utils;

import java.awt.Font;

import javax.swing.*;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class UpdateProgressBar extends Thread {
	
	private JProgressBar progressBar;
	private EmbeddedMediaPlayerComponent play;
	private int value = 0;
	
	public UpdateProgressBar(final EmbeddedMediaPlayerComponent play,final JProgressBar progressBar,final JLabel currentTimeJLabel,final JLabel totalTimeJLabel) {
		// TODO Auto-generated constructor stub
		this.play = play;
		this.progressBar = progressBar;
		new SwingWorker<String, Integer>() {  
			  
            @Override  
            protected String doInBackground() throws Exception {  
                // TODO Auto-generated method stub  
                while (true) {  //获取视频播放进度并且按百分比显示  
                    long total=play.getMediaPlayer().getLength();  
                    long curr=play.getMediaPlayer().getTime();  
                    float percent=(float)curr/total;  
                    MyDate date = getdate(total);
                    Font font = new Font("宋体", 1, 20);
                    totalTimeJLabel.setText(date.hour+":"+date.minute+":"+date.second);
                    totalTimeJLabel.setFont(font);
                    date = getdate(curr);
                    currentTimeJLabel.setText(date.hour+":"+date.minute+":"+date.second);
                    currentTimeJLabel.setFont(font);
                    publish((int)(percent*100));  
                    Thread.sleep(100);  
                }     
                //return null;  
            }  
              
            protected void process(java.util.List<Integer> chunks) {  
                for(int v:chunks){  
                	progressBar.setValue(v);  
                }   
            }  
        }.execute();
	}
	
	class MyDate{
		long hour;
		long minute;
		long second;
	}
	
	public MyDate getdate(long total){
		MyDate date = new MyDate();
		total = total/1000;
		date.hour = total/60/60;
		date.minute = (total-date.hour*60)/60;
		date.second = total-date.hour*60-date.minute*60;
		return date;
	}
	
}
