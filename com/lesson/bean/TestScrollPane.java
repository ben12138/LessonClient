package com.lesson.bean;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class TestScrollPane extends JFrame{

    public static void main(String args[]) {

        TestScrollPane theApp = new TestScrollPane(320, 400);
    }
    
    public TestScrollPane(int xPixels, int yPixels){
        super("Add Image");
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelContent);   
        scrollPane.setPreferredSize(new Dimension(xPixels - 5, yPixels));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int height = 55;
        for (int i = 0; i < 10; i++) {
            JLabel iconType = new JLabel();
            panelContent.add(new JLabel(i+" "));
            height += 55;
        }
        panelContent.setPreferredSize(new Dimension(xPixels - 5, height));
        this.add(scrollPane, BorderLayout.CENTER);
        
        setSize(xPixels, yPixels);   
        setVisible(true);   
    }
}
