package com.lesson.bean;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.media.MediaPlayer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class Test {
	EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public static void main(String[] args) {
		new Test();
	}
	private Test() {  
        final JFrame frame = new JFrame("vlcj Tutorial");  
        frame.setLayout(new BorderLayout());

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();  

        frame.add(mediaPlayerComponent,BorderLayout.CENTER);  
        

        frame.setLocation(100, 100);  
        frame.setSize(500,500);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        JButton btn = new JButton("a");
        frame.add(btn,BorderLayout.SOUTH);
        final JLabel dd = new JLabel("asdasdsadsadsadasd");
        btn.addMouseListener(new MouseAdapter() {
        	boolean aa = true;
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseEntered(e);
        		frame.removeAll();
        		frame.repaint();
    			frame.validate();
//        		if(aa){
//        			frame.add(dd,BorderLayout.CENTER);
//        		}else{
//        			frame.remove(dd);
//        		}
        		frame.add(mediaPlayerComponent,BorderLayout.CENTER);
        		dd.repaint();
    			dd.validate();
     			mediaPlayerComponent.repaint();
    			mediaPlayerComponent.validate();
        		frame.toFront();
        		frame.repaint();
    			frame.validate();
        		aa = !aa;
        	}
		});
        frame.setVisible(true);  
        mediaPlayerComponent.getMediaPlayer().playMedia("http://192.168.191.1:8080/LessonServer/coursevideo/5/1.mp4");// please change it to an existed media file  
    }  
}
