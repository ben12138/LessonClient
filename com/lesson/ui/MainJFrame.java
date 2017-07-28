package com.lesson.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


import com.lesson.bean.Comments;
import com.lesson.bean.CourseInf;
import com.lesson.bean.GOLBALVALUE;
import com.lesson.bean.PlayerMain;
import com.lesson.bean.UserInf;
import com.lesson.bean.Window;
import com.lesson.net.NetConnection;
import com.lesson.utils.GetComments;
import com.lesson.utils.GetCoursesInf;
import com.lesson.utils.GetImage;
import com.lesson.utils.GetUserInf;
import com.lesson.utils.ImageUtil;
import com.lesson.utils.MyColor;
import com.lesson.utils.UpdateProgressBar;
import com.lesson.view.CommentsJPanel;
import com.lesson.view.CourseJPanel;
import com.lesson.view.MyJLable;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class MainJFrame extends JFrame {

	/* 坐标对象，用于存储鼠标拖动窗体的位置 */
	private static Point point;
	private JPanel titleJPanel;//标题栏
	private Container content;//整个容器
	private MyJLable titleLogoJLable;//logo标签
	private MyJLable mainJLable;//头部主界面标签
	private MyJLable myCourseJLabel;//头部我的课程标签
	private MyJLable boFangJLabel;//头部播放标签
	private MyJLable minJLabel;
	private MyJLable maxJLabel;
	private MyJLable exitJLabel;
//	private MyJLable allcoursesJLabel;
	private JTextField searchJTextField;
	private JLabel leftNullJLabel;//searchJTextField左侧JLabel
	private JLabel rightNullJLabel;//searchJTextField右侧JLabel
	private MyJLable searchJLabel;//搜索JLabel
	public static MyJLable headImageJLabel;//头像JLabel
//	private MyJLable refreshJLabel;//刷新JLabel
	
	private JPanel controlJPanel;//右上角的控制
	private JPanel centerJPanel;//中部的控制布局
	private JPanel centerMainJPanel;//中部main界面布局
	private JPanel centerMyCourseJPanel;//中部我的课程界面布局
	private JPanel centerAllCoursesJPanel;//中部所有课程界面布局
	private JPanel centerBoFangJPanel;//中部播放界面布局
	private JPanel centerUserInfJPanel;//中部个人信息界面布局
	private JPanel centerSearchJPanel;//中部搜索信息
	
	private boolean isMainClicked = true;
	private boolean isMyCourseClicked = false;
	private boolean isBoFangClicked = false;
	private static boolean isMaxSize = false;
	private boolean isAllCourseClicked = false;
	private boolean isHideComments = false;
	private boolean isPlaying = false;
	private boolean isBofangOpen = false;
	private boolean isOk = false;
	
	private final int INITCENTERMAIN = 0;
	private final int INITCENTERMyCourse = 1;
	private final int INITCENTERBOFANG = 2;
	private final int INITCENTERSEARCH = 3;
	private final int INITCENTERUSERINF = 4;
	
	private List<EmbeddedMediaPlayerComponent> playList = new ArrayList<>();
	
	private EmbeddedMediaPlayerComponent playerComponent;
	
	private int currentCenterJPanel = 0;

	public MainJFrame() {
		// TODO Auto-generated constructor stub
		super("Lesson");
		point = new Point();
		this.setUndecorated(true);
		this.setBounds(100, 50, 1000, 650);
		this.setBackground(MyColor.FONT_GRAY);
		this.setLayout(new BorderLayout(1,2));
		this.setResizable(true);
		//得到所有课程类别信息
		new GetCoursesInf().getAllTypeInf();
		content = this.getContentPane();
		init();
		setWindowIcon();
		this.setVisible(true);
	}

	private void init() {
		//初始化播放组件
		playerComponent = new EmbeddedMediaPlayerComponent();
		//初始化中部界面
		centerJPanel = new JPanel();
		centerJPanel.setLayout(new BorderLayout());
		//初始化头部标题栏
		titleJPanel = new JPanel();
		titleJPanel.setBackground(Color.WHITE);
		titleJPanel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width, 50));
		titleJPanel.setLayout(new FlowLayout(0));
		titleLogoJLable = new MyJLable("/image/title_logo.png", 165, 60,0,0);
		titleLogoJLable.setPreferredSize(new Dimension(165, 50));
		//初始化mainJLabel
		mainJLable = new MyJLable("/image/main_pressed.png",65,50,0,0);
		mainJLable.setPreferredSize(new Dimension(65, 50));
		mainJLable.addMouseListener(new MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				mainJLable.setBackground("/image/main_pressed.png");
				boFangJLabel.setBackground("/image/bofang_normal.png");
				myCourseJLabel.setBackground("/image/mycourse_normal.png");
//				allcoursesJLabel.setBackground("/image/allcourses_normal.png");
				isMainClicked = true;
				isMyCourseClicked = false;
				isBoFangClicked = false;
				isAllCourseClicked = false;
				currentCenterJPanel = INITCENTERMAIN;
				createCenterJPanel(INITCENTERMAIN);
				clearPlayer();
				isOk = true;
				GOLBALVALUE.coursetype1str = null;
				GOLBALVALUE.coursetype2str = null;
				GOLBALVALUE.coursetype3str = null;
				GOLBALVALUE.id = 0;
			}

			public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if (!isMainClicked) {
					mainJLable.setBackground("/image/main_over.png");
				}
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
				if (!isMainClicked) {
					mainJLable.setBackground("/image/main_normal.png");
				}
			}
		});
		//初始化myCourseJLabel
		myCourseJLabel = new MyJLable("/image/mycourse_normal.png", 65, 50, 0, 0);
		myCourseJLabel.setPreferredSize(new Dimension(60, 50));
		myCourseJLabel.addMouseListener(new MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				myCourseJLabel.setBackground("/image/mycourse_pressed.png");
				boFangJLabel.setBackground("/image/bofang_normal.png");
				mainJLable.setBackground("/image/main_normal.png");
				// allcoursesJLabel.setBackground("/image/allcourses_normal.png");
				isMyCourseClicked = true;
				isMainClicked = false;
				isBoFangClicked = false;
				isAllCourseClicked = false;
				currentCenterJPanel = INITCENTERMyCourse;
				clearPlayer();
				isOk = true;
				createCenterJPanel(currentCenterJPanel);
			}

			public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if (!isMyCourseClicked) {
					myCourseJLabel.setBackground("/image/mycourse_over.png");
				}
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
				if (!isMyCourseClicked) {
					myCourseJLabel.setBackground("/image/mycourse_normal.png");
				}
			}
		});
		//初始化boFangJLabel
		boFangJLabel = new MyJLable("/image/bofang_normal.png",65,50,0,0);
		boFangJLabel.setPreferredSize(new Dimension(60, 50));
		boFangJLabel.addMouseListener(new MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if(GOLBALVALUE.course != null && isOk){
					myCourseJLabel.setBackground("/image/mycourse_normal.png");
					boFangJLabel.setBackground("/image/bofang_pressed.png");
					mainJLable.setBackground("/image/main_normal.png");
//					allcoursesJLabel.setBackground("/image/allcourses_normal.png");
					isMainClicked = false;
					isMyCourseClicked = false;
					isBoFangClicked = true;
					isAllCourseClicked = false;
					currentCenterJPanel = INITCENTERBOFANG;
					createCenterJPanel(currentCenterJPanel);
					playerComponent.repaint();
					playerComponent.validate();
					playerComponent.revalidate();
					playerComponent.getMediaPlayer().play();
				}
			}

			public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if (!isBoFangClicked) {
					boFangJLabel.setBackground("/image/bofang_over.png");
				}
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
				if (!isBoFangClicked) {
					boFangJLabel.setBackground("/image/bofang_normal.png");
				}
			}
		});
		//初始化所有课程JLabel
//		allcoursesJLabel = new MyJLable("/image/allcourses_normal.png",65,50,0,0);
//		allcoursesJLabel.setPreferredSize(new Dimension(60, 50));
//		allcoursesJLabel.addMouseListener(new MouseAdapter() {
//			
//			public void mouseClicked(java.awt.event.MouseEvent evt) {  
//				allcoursesJLabel.setBackground("/image/allcourses_pressed.png");
//				myCourseJLabel.setBackground("/image/mycourse_normal.png");
//				boFangJLabel.setBackground("/image/bofang_normal.png");
//				mainJLable.setBackground("/image/main_normal.png");
//				isMainClicked = false;
//				isMyCourseClicked = false;
//				isBoFangClicked = false;
//				isAllCourseClicked = true;
//			}
//			
//			 public void mouseEntered(MouseEvent e){//鼠标进入  
//				 setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
//			      if(!isAllCourseClicked){
//			    	  allcoursesJLabel.setBackground("/image/allcourses_over.png");
//			      }
//			 }
//			 
//			 public  void mouseExited(MouseEvent e){//鼠标移除  
//				 setCursor(Cursor.getDefaultCursor());  
//			      if(!isAllCourseClicked){
//			    	  allcoursesJLabel.setBackground("/image/allcourses_normal.png");
//			      }
//			 }
//			 
//		});
		//初始化右上方的JPanel
		controlJPanel = new JPanel();
		controlJPanel.setLayout(new GridLayout(1, 3));
		controlJPanel.setBackground(Color.WHITE);
		controlJPanel.setPreferredSize(new Dimension(180, 50));
		//初始化最小化窗口
		minJLabel = new MyJLable("/image/min_normal.png",60,50,0,0);
		minJLabel.setPreferredSize(new Dimension(60, 50));
		minJLabel.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent evt) {  
				MainJFrame.this.setExtendedState(JFrame.ICONIFIED);
			    }  
			    public void mouseEntered(MouseEvent e){//鼠标进入  
			    	minJLabel.setBackground("/image/min_over.png");
			     }  
			   public  void mouseExited(MouseEvent e){//鼠标移除  
			      minJLabel.setBackground("/image/min_normal.png");
			    }  
		});
		controlJPanel.add(minJLabel);
		//初始化最大化窗口
		maxJLabel = new MyJLable("/image/max_normal.png",60,50,0,0);
		maxJLabel.setPreferredSize(new Dimension(60, 50));
		maxJLabel.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent evt) { 
				if (isMaxSize) {
					isMaxSize = false;
					MainJFrame.this.setBounds(100, 50, 1000, 650);
					leftNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/14, 50));
					rightNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/12+80, 50));
				}else{
					isMaxSize = true;
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
					MainJFrame.this.setExtendedState(Frame.MAXIMIZED_BOTH);
					leftNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/8, 50));
					rightNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/4+80, 50));
				}
				if(currentCenterJPanel != INITCENTERBOFANG){
					createCenterJPanel(currentCenterJPanel);
				}
				MainJFrame.this.repaint();
				MainJFrame.this.validate();
				centerJPanel.repaint();
				centerJPanel.validate();
				centerMainJPanel.repaint();
				centerMainJPanel.validate();
			    }  
			    public void mouseEntered(MouseEvent e){//鼠标进入  
			      maxJLabel.setBackground("/image/max_over.png");
			     }  
			   public  void mouseExited(MouseEvent e){//鼠标移除  
				   maxJLabel.setBackground("/image/max_normal.png");
			    }  
		});
		controlJPanel.add(maxJLabel);
		//初始化退出按钮
		exitJLabel = new MyJLable("/image/exit_normal.png",60,50,0,0);
		exitJLabel.setPreferredSize(new Dimension(60, 50));
		exitJLabel.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent evt) {  
				System.exit(0);
			    }  
			    public void mouseEntered(MouseEvent e){//鼠标进入  
			    	exitJLabel.setBackground("/image/exit_over.png");
			     }  
			   public  void mouseExited(MouseEvent e){//鼠标移除  
				   exitJLabel.setBackground("/image/exit_normal.png");
			    }  
		});
		controlJPanel.add(exitJLabel);
		//初始化searchJTextField
		searchJTextField = new JTextField("请输入你想搜索的东西");
		searchJTextField.setForeground(MyColor.FONT_GRAY);
		searchJTextField.setPreferredSize(new Dimension(125, 30));
		searchJTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {  
				if (searchJTextField.getText().equals("请输入你想搜索的东西")) {
					searchJTextField.setText("");
				}
			}

			public  void mouseExited(MouseEvent e){//鼠标移除 
				if (searchJTextField.getText().equals("")) {
					searchJTextField.setText("请输入你想搜索的东西");
				}
			}
		});
		//初始化空格JLabel
		leftNullJLabel = new JLabel();
		rightNullJLabel = new JLabel();
		leftNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/14, 50));
		rightNullJLabel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width/12+80, 50));
		//初始化搜索JLabel
		searchJLabel = new MyJLable("/image/search.png", 20, 20, 0, 0);
		searchJLabel.setPreferredSize(new Dimension(20, 20));
		searchJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {  
				currentCenterJPanel = INITCENTERSEARCH;
				createCenterJPanel(currentCenterJPanel);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				searchJLabel.setBackground("/image/search_over.png");
			}
			public  void mouseExited(MouseEvent e){//鼠标移除 
				searchJLabel.setBackground("/image/search.png");
			}
		});
		//初始化头像JLabel
		headImageJLabel = new MyJLable("/image/default_personal_image.png", 30, 30, 0, 0);
		headImageJLabel.setPreferredSize(new Dimension(30, 30));
		if(GOLBALVALUE.user.getHeadimagePath() != null){
			new GetImage().showJLabelImageFromUrl(headImageJLabel, GOLBALVALUE.user.getHeadimagePath());
		}
		headImageJLabel.addMouseListener(new MouseAdapter() {
			JPopupMenu popup = new JPopupMenu();  
			boolean isClicked = false;
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				popup.setVisible(!isClicked);
				isClicked = !isClicked;
				popup.repaint();
                popup.validate();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
                popup.setLayout(new BorderLayout());  
                JPanel infoPanel = createUserInfPopUpJPanel();  
                infoPanel.repaint();
                infoPanel.validate();
                popup.removeAll();
                popup.add(infoPanel, BorderLayout.CENTER);  
                popup.setVisible(true);
                int x = headImageJLabel.getX();
                int y = headImageJLabel.getY();
                popup.repaint();
                popup.validate();
                popup.show(MainJFrame.this, x-120, y+35);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				popup.repaint();
                popup.validate();
				popup.setVisible(isClicked);
			}
		});
		//初始化刷新JLabel
//		refreshJLabel = new MyJLable("/image/refresh_normal.png", 20, 20, 0, 0);
//		refreshJLabel.setPreferredSize(new Dimension(20, 20));
//		refreshJLabel.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				openURL("www.baidu.com");
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				super.mouseEntered(e);
//				refreshJLabel.setBackground("/image/refresh_over.png");
//			}
//
//			public void mouseExited(MouseEvent e) {// 鼠标移除
//				refreshJLabel.setBackground("/image/refresh_normal.png");
//			}
//		});
		//初始化中间布局
		createCenterJPanel(currentCenterJPanel);
		//添加组件
		titleJPanel.add(titleLogoJLable);
		titleJPanel.add(mainJLable);
//		titleJPanel.add(allcoursesJLabel);
		titleJPanel.add(myCourseJLabel);
		titleJPanel.add(boFangJLabel);
		titleJPanel.add(leftNullJLabel);
		titleJPanel.add(searchJTextField);
		titleJPanel.add(searchJLabel);
		
		titleJPanel.add(rightNullJLabel);
		titleJPanel.add(headImageJLabel);
//		titleJPanel.add(refreshJLabel);
		titleJPanel.add(controlJPanel);
		this.add(titleJPanel,BorderLayout.NORTH);
		this.add(centerJPanel,BorderLayout.CENTER);
	}

	@Override
	public void setExtendedState(int state) {
	 
	    if ((state & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH) {
	        Rectangle bounds = getGraphicsConfiguration().getBounds();
	        Rectangle maxBounds = null;
	        // Check to see if this is the 'primary' monitor
	        // The primary monitor should have screen coordinates of (0,0)
	        if (bounds.x == 0 && bounds.y == 0) {
	            Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
	            maxBounds = new Rectangle(screenInsets.left, screenInsets.top, bounds.width - screenInsets.right - screenInsets.left
	                    , bounds.height - screenInsets.bottom - screenInsets.top);
	        } else {
	            // Not the primary monitor, reset the maximized bounds...
	            maxBounds = null;
	        }
	        super.setMaximizedBounds(maxBounds);
	    }
	    super.setExtendedState(state);
	}
	
	private void createCenterJPanel(int type){
		// 初始化中部布局
		centerJPanel.removeAll();
		switch (type) {
		case 0:
			//初始化中部main界面布局	
			centerMainJPanel = new JPanel();
			centerMainJPanel.setBackground(Color.LIGHT_GRAY);
			createCenterMainJPanel(GOLBALVALUE.id);
			centerJPanel.add(centerMainJPanel,BorderLayout.CENTER);
			break;
		case 1:
			//初始化中部我的课程界面布局
			centerMyCourseJPanel = new JPanel();
			centerMyCourseJPanel.setBackground(Color.LIGHT_GRAY);
			createCenterMyCourseJPanel();
			centerJPanel.add(centerMyCourseJPanel,BorderLayout.CENTER);
			break;
		case 2:
			//初始化中部播放界面布局
			if(playList.size() == 0){
				playList.add(new EmbeddedMediaPlayerComponent());
			}
			EmbeddedMediaPlayerComponent play = playList.get(playList.size()-1);
//			if(GOLBALVALUE.course !=null && !isBofangOpen){
				centerBoFangJPanel = new JPanel();
				centerBoFangJPanel.setBackground(Color.LIGHT_GRAY);
//				playerComponent.getMediaPlayer().playMedia("http://192.168.191.1:8080/LessonServer/coursevideo/5/1.mp4");
				
//				centerJPanel.setVisible(true);
				
//			}
			createCenterBoFangJPanel(play);
			centerJPanel.add(centerBoFangJPanel,BorderLayout.CENTER);
			MainJFrame.this.repaint();
			MainJFrame.this.validate();
			centerMainJPanel.repaint();
			centerMainJPanel.validate();
			centerJPanel.repaint();
			centerJPanel.validate();
			centerBoFangJPanel.repaint();
			centerBoFangJPanel.validate();
			play.repaint();
			play.validate();
			play.getMediaPlayer().playMedia(GOLBALVALUE.videoURLS.get(0).getCourseurl());
			break;
		case 3:
			//初始搜索结果界面
			centerSearchJPanel = new JPanel();
			centerSearchJPanel.setBackground(Color.LIGHT_GRAY);
			createCenterSearchJPanel();
			centerJPanel.add(centerSearchJPanel,BorderLayout.CENTER);
			break;
		case 4:
			centerUserInfJPanel = new JPanel();
			centerUserInfJPanel.setBackground(Color.WHITE);
			createCenterUserInfJPanel();
			centerJPanel.add(centerUserInfJPanel,BorderLayout.CENTER);
			break;
		default:
			break;
		}
		//初始化中部所有课程界面布局
//		centerAllCoursesJPanel = new JPanel();
//		centerAllCoursesJPanel.setBackground(Color.LIGHT_GRAY);
//		createCenterAllCoursesJPanel();
//		centerJPanel.add(centerAllCoursesJPanel);
		centerJPanel.repaint();
		centerJPanel.validate();
	}
	
	private void createCenterMainJPanel(int id){
		centerMainJPanel.removeAll();
		centerMainJPanel.setLayout(new BorderLayout(1,1));
		//初始化右侧的主界面
		JPanel centerRightJPanel = new JPanel();
		int width = MainJFrame.this.getSize().width-90;
		centerRightJPanel.setPreferredSize(new Dimension(width, 0));
		centerRightJPanel.setBackground(MyColor.FONT_GRAY); 
		centerRightJPanel.setLayout(new BorderLayout(1,1));
		//初始化右侧显示图片界面
		final JPanel rightPictureJpanel = new JPanel();
		rightPictureJpanel.setPreferredSize(new Dimension(width-210, 0));
		rightPictureJpanel.setBackground(Color.WHITE);
		createMainRightPictureJPanel(rightPictureJpanel,id,null,null,null);
		//增加二级三级菜单
		final JPanel coursetype2JPanel = new JPanel();
		coursetype2JPanel.setPreferredSize(new Dimension(200, 0));
		coursetype2JPanel.setBackground(Color.WHITE);
		//初始化左侧导航菜单栏
		JPanel centerLeftMenu = new JPanel();
		centerLeftMenu.setPreferredSize(new Dimension(90, 0));
		centerLeftMenu.setBackground(Color.WHITE);
		GridLayout layout = new GridLayout(10, 1,1,1);
		centerLeftMenu.setLayout(layout);
		//左侧添加六个一级菜单栏
		final JLabel[] menuJLabel = new JLabel[6];
		final String[] menuText = {"语言*留学","IT*互联网","设计*创作","职业*考证","升学*考研","兴趣*生活"};
		for(int i = 0;i < 6;i++){
			final int j = i;
			menuJLabel[i] =  new JLabel();
			menuJLabel[i].setText("    "+menuText[i]);
			menuJLabel[j].setForeground(MyColor.FONT_GRAY);
			if (i == 0) {
				menuJLabel[i].setForeground(MyColor.FONT_ORAGNE);
				HashMap<String, HashMap<String,String>> map2 = GOLBALVALUE.map.get(menuText[0]);
				coursetype2JPanel.setLayout(new GridLayout(map2.size()+2,1 ));
				//循环初始化二级标签和三级标签
				for(final String key2:map2.keySet()){
					//二级标签和三级标签小模块
					JPanel coursetype3JPanel = new JPanel();
					coursetype3JPanel.setLayout(new BorderLayout());
					coursetype3JPanel.setBackground(Color.WHITE);
					JLabel jbabel = new JLabel("   "+key2);
					jbabel.setForeground(MyColor.FONT_ORANGE_OVER);
					final HashMap<String,String> map3 = map2.get(key2);
					//三级标签模块
					JPanel coursetypeJPanel = new JPanel();
					coursetypeJPanel.setBackground(Color.WHITE);
					coursetypeJPanel.setLayout(new GridLayout(map3.size()/3+1,3));
					for(final String key3:map3.keySet()){
						final JLabel jbabel3 = new JLabel("   "+map3.get(key3));
						jbabel3.setForeground(MyColor.FONT_GRAY);
						jbabel3.addMouseListener(new MouseAdapter() {
							
							public void mouseClicked(MouseEvent e) {
								jbabel3.setForeground(MyColor.FONT_ORAGNE);
								GOLBALVALUE.coursetype1str = menuText[j];
								GOLBALVALUE.coursetype2str = key2;
								GOLBALVALUE.coursetype3str = map3.get(key3);
								GOLBALVALUE.id = Integer.parseInt(key3);
								System.out.println(GOLBALVALUE.id);
								createMainRightPictureJPanel(rightPictureJpanel,Integer.parseInt(key3),menuText[j],key2,map3.get(key3));
							};
							
							public void mouseEntered(MouseEvent e) {
								setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
								jbabel3.setForeground(MyColor.FONT_ORANGE_OVER);
							};
							
							public void mouseExited(MouseEvent e) {
								setCursor(Cursor.getDefaultCursor());  
								jbabel3.setForeground(MyColor.FONT_GRAY);
							};
							
						});
						coursetypeJPanel.add(jbabel3);
					}
					coursetype3JPanel.add(jbabel,BorderLayout.NORTH);
					coursetype3JPanel.add(coursetypeJPanel,BorderLayout.CENTER);
					coursetype2JPanel.add(coursetype3JPanel);
				}
			}
			
			menuJLabel[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					for(int t = 0;t<6;t++){
						if(menuJLabel[t].getForeground() == MyColor.FONT_ORANGE_OVER){
							menuJLabel[t].setForeground(MyColor.FONT_ORAGNE);
						}else{
							menuJLabel[t].setForeground(MyColor.FONT_GRAY);
						}
					}
					HashMap<String, HashMap<String,String>> map2 = GOLBALVALUE.map.get(menuText[j]);
					coursetype2JPanel.setLayout(new GridLayout(map2.size()+3,1 ));
					coursetype2JPanel.removeAll();
					//循环初始化二级标签和三级标签
					for(final String key2:map2.keySet()){
						//二级标签和三级标签小模块
						JPanel coursetype3JPanel = new JPanel();
						coursetype3JPanel.setLayout(new BorderLayout());
						coursetype3JPanel.setBackground(Color.WHITE);
						coursetype3JPanel.removeAll();
						JLabel jbabel = new JLabel("   "+key2);
						jbabel.setForeground(MyColor.FONT_ORANGE_OVER);
						final HashMap<String,String> map3 = map2.get(key2);
						//三级标签模块
						JPanel coursetypeJPanel = new JPanel();
						coursetypeJPanel.setBackground(Color.WHITE);
						coursetypeJPanel.setLayout(new GridLayout(map3.size()/3+1,3));
						coursetypeJPanel.removeAll();
						for(final String key3:map3.keySet()){
							final JLabel jbabel3 = new JLabel("   "+map3.get(key3));
							jbabel3.setForeground(MyColor.FONT_GRAY);
							jbabel3.addMouseListener(new MouseAdapter() {
								
								public void mouseClicked(MouseEvent e) {
									jbabel3.setForeground(MyColor.FONT_ORAGNE);
									GOLBALVALUE.coursetype1str = menuText[j];
									GOLBALVALUE.coursetype2str = key2;
									GOLBALVALUE.coursetype3str = map3.get(key3);
									GOLBALVALUE.id = Integer.parseInt(key3);
									System.out.println(GOLBALVALUE.id);
									createMainRightPictureJPanel(rightPictureJpanel,Integer.parseInt(key3),menuText[j],key2,map3.get(key3));
								};
								
								public void mouseEntered(MouseEvent e) {
									setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
									jbabel3.setForeground(MyColor.FONT_ORANGE_OVER);
								};
								
								public void mouseExited(MouseEvent e) {
									setCursor(Cursor.getDefaultCursor());  
									jbabel3.setForeground(MyColor.FONT_GRAY);
								};
								
							});
							coursetypeJPanel.add(jbabel3);
						}
						coursetype3JPanel.add(jbabel,BorderLayout.NORTH);
						coursetype3JPanel.add(coursetypeJPanel,BorderLayout.CENTER);
						coursetype2JPanel.add(coursetype3JPanel);
						MainJFrame.this.validate();
						MainJFrame.this.repaint();
						coursetype2JPanel.validate();
						coursetype3JPanel.validate();
						coursetypeJPanel.validate();
					}
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseEntered(e);
					for(int t = 0;t<6;t++){
						if(menuJLabel[t].getForeground() == MyColor.FONT_GRAY){
							menuJLabel[j].setForeground(MyColor.FONT_ORANGE_OVER);
						}
					}
					
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseExited(e);
					for(int t=0;t<6;t++){
						if(menuJLabel[t].getForeground() == MyColor.FONT_ORAGNE){
							menuJLabel[t].setForeground(MyColor.FONT_ORAGNE);
						}else{
							menuJLabel[t].setForeground(MyColor.FONT_GRAY);
						}
					}
				}
			});
			centerLeftMenu.add(menuJLabel[i]);
		}
		
		//具体课程JPanel
		//增加三个JPanel
		centerMainJPanel.add(centerLeftMenu,BorderLayout.WEST);
		centerMainJPanel.add(centerRightJPanel,BorderLayout.CENTER);
		centerRightJPanel.add(coursetype2JPanel,BorderLayout.WEST);
		centerRightJPanel.add(rightPictureJpanel,BorderLayout.CENTER);
	}
	
	//Main主界面的右侧显示图片界面添加控件
	private void createMainRightPictureJPanel(JPanel parent,int id,String coursetype1,String coursetype2,String coursetype3){
		coursetype1 = GOLBALVALUE.coursetype1str;
		coursetype2 = GOLBALVALUE.coursetype2str;
		coursetype3 = GOLBALVALUE.coursetype3str;
		System.out.println("id:"+id);
		if(id == 0 || coursetype1 == null 
			|| coursetype2 == null || coursetype3 == null){
			parent.removeAll();
			parent.setLayout(new BorderLayout(1,1));
			//初始化头部显示轮播图片JPanel
			int width = MainJFrame.this.getSize().width-90-210;
			JPanel headLunBoPictureJPane = new JPanel();
			headLunBoPictureJPane.setPreferredSize(new Dimension(width, 270));
			headLunBoPictureJPane.setLayout(new BorderLayout());
			headLunBoPictureJPane.setBackground(Color.WHITE);
			String[] lunboURLS = new String[]{
					NetConnection.url+"tuisongImage/1.jpg",
					NetConnection.url+"tuisongImage/2.png",
					NetConnection.url+"tuisongImage/3.jpg",
					NetConnection.url+"tuisongImage/4.jpg",
					NetConnection.url+"tuisongImage/5.jpg",
					NetConnection.url+"tuisongImage/6.jpg",
					NetConnection.url+"tuisongImage/7.jpg",
			};
			MyJLable pictureJLabel = new MyJLable("/image/pictures_no.png", width, 270, 0, 0);
			pictureJLabel.setPreferredSize(new Dimension(width, 270));
			GetImage.lunboImage(pictureJLabel, lunboURLS);
			headLunBoPictureJPane.add(pictureJLabel,BorderLayout.NORTH);
			//轮播图片加左右点击JLabel
			pictureJLabel.setLayout(new BorderLayout());
			MyJLable leftJLabel = new MyJLable("/image/left.png", 40, 40, 0, 50);
			leftJLabel.setPreferredSize(new Dimension(40, 40));
			leftJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					GetImage.beforeImage();
				}

				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
				}
			});
			MyJLable rightJLabel = new MyJLable("/image/right.png", 40, 40, 0, 50);
			rightJLabel.setPreferredSize(new Dimension(40, 40));
			rightJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					GetImage.nextImage();
				}
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
				}
			});
			pictureJLabel.add(leftJLabel,BorderLayout.WEST);
			pictureJLabel.add(rightJLabel,BorderLayout.EAST);
			//初始化右侧下部课程信息
			JPanel coursesJPanel = new JPanel();
			coursesJPanel.removeAll();
			coursesJPanel.setPreferredSize(new Dimension(width, 500));
			coursesJPanel.setBackground(Color.WHITE);
			HashMap<String, ArrayList<CourseInf>> coursesmap = GetCoursesInf.getTuiSongCourseInf();
			coursesJPanel.setLayout(new GridLayout(2,1,10,20));
			for(String key:coursesmap.keySet()){
				//推送初始化
				JPanel courses1JPanel = new JPanel();
				courses1JPanel.removeAll();
				courses1JPanel.setLayout(new BorderLayout());
				courses1JPanel.setBackground(Color.WHITE);
				//顶部类别JLabel
				JPanel titleJPanel = new JPanel();
				titleJPanel.setBackground(Color.WHITE);
				JLabel coursetype1JLabel = new JLabel();
				coursetype1JLabel.setText(key);
				coursetype1JLabel.setFont(new Font("Dialog",   1,   15));
				coursetype1JLabel.setForeground(MyColor.FONT_ORAGNE);
				titleJPanel.add(coursetype1JLabel);
				//底部八个课程初始化
				JPanel coursesJPanel2 = new JPanel();
				coursesJPanel2.removeAll();
				coursesJPanel2.setLayout(new GridLayout(1,4));
				List<CourseInf> courses = coursesmap.get(key);
				for(final CourseInf course:courses){
					final int x = width/4;
					final int y = (MainJFrame.this.getSize().height-340)/4;
					CourseJPanel temp = new CourseJPanel(course, x,y ,parent);
					temp.setImageSize(x,y);
					//课程图片设置点击事件
					final MyJLable imageJLabel = temp.getImageJLabel();
					imageJLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseClicked(e);
							currentCenterJPanel = INITCENTERBOFANG;
							boFangJLabel.setBackground("/image/bofang_pressed.png");
							mainJLable.setBackground("/image/main_normal.png");
							isBoFangClicked = true;
							isMainClicked = false;
							GOLBALVALUE.videoURLS = null;
							GetCoursesInf.getCourseVideoURLS(course);
							GOLBALVALUE.course = course;
							createCenterJPanel(currentCenterJPanel);
							isBofangOpen = true;
						}
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseEntered(e);
							imageJLabel.setSize(x+10, y+10);
							imageJLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseExited(e);
							imageJLabel.setSize(x, y);
							imageJLabel.setCursor(Cursor.getDefaultCursor());
						}
					});
					coursesJPanel2.add(temp.getUI());
				}
				courses1JPanel.add(titleJPanel,BorderLayout.NORTH);
				courses1JPanel.add(coursesJPanel2,BorderLayout.CENTER);
				//添加组件
				coursesJPanel.add(courses1JPanel);
			}
			//增加组件
			parent.add(headLunBoPictureJPane,BorderLayout.NORTH);
			parent.add(coursesJPanel,BorderLayout.CENTER);
		}else{
			parent.removeAll();
			List<CourseInf> courses = GetCoursesInf.getCourses(coursetype1, coursetype2, coursetype3);
			if(courses.size() == 0){
				MyJLable backgroundJLabel = new MyJLable("/image/noLesson.png",MainJFrame.this.getSize().width-300,MainJFrame.this.getSize().height-100,0,0);
				backgroundJLabel.setSize(MainJFrame.this.getSize().width-300,MainJFrame.this.getSize().height-100);
				parent.setLayout(new BorderLayout());
				parent.add(backgroundJLabel,BorderLayout.CENTER);
				backgroundJLabel.repaint();
				backgroundJLabel.validate();
				parent.repaint();
				parent.validate();
			}else{
				parent.setBackground(Color.WHITE);
				parent.setLayout(new BorderLayout());
				JPanel showCoursesJPanel = new JPanel();
				showCoursesJPanel.setBackground(Color.WHITE);
				int rows = 0;
				if(courses.size()/4<4){
					rows = 4;
				} else {
					if (courses.size() % 4 == 0) {
						rows = courses.size() / 4;
					} else {
						rows = courses.size() / 4 + 1;
					}
				}
				showCoursesJPanel.setLayout(new FlowLayout(0));
				JLabel titleJPanel = new JLabel();
				titleJPanel.setText("  "+GOLBALVALUE.coursetype1str+">>"+GOLBALVALUE.coursetype2str+">>"+GOLBALVALUE.coursetype3str);
				titleJPanel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width-320, 30));
				titleJPanel.setFont(new Font("宋体", 1, 16));
				titleJPanel.setForeground(MyColor.FONT_ORANGE_OVER);
				JScrollPane showcoursesJScrollPane = new JScrollPane(showCoursesJPanel);
				showcoursesJScrollPane.setPreferredSize(new Dimension(MainJFrame.this.getSize().width, MainJFrame.this.getSize().height));
				showcoursesJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				showcoursesJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				showCoursesJPanel.add(titleJPanel);
				int height = rows * (MainJFrame.this.getSize().height/rows);
				showCoursesJPanel.setPreferredSize(new Dimension(0,height));
				int width = (MainJFrame.this.getSize().width-340)/4;
				final int x = width;
				final int y = MainJFrame.this.getSize().height/(rows+1);
				for (final CourseInf course:courses) {
					CourseJPanel courseJPanel = new CourseJPanel(course, x, y, showCoursesJPanel);
					courseJPanel.setImageSize(x, y);
					//课程图片设置点击事件
					final MyJLable imageJLabel = courseJPanel.getImageJLabel();
					imageJLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseClicked(e);
							currentCenterJPanel = INITCENTERBOFANG;
							boFangJLabel.setBackground("/image/bofang_pressed.png");
							mainJLable.setBackground("/image/main_normal.png");
							isBoFangClicked = true;
							isMainClicked = false;
							GOLBALVALUE.videoURLS = null;
							GetCoursesInf.getCourseVideoURLS(course);
							GOLBALVALUE.course = course;
							createCenterJPanel(currentCenterJPanel);
							isBofangOpen = true;
						}
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseEntered(e);
							imageJLabel.setSize(x+10, y+10);
							imageJLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseExited(e);
							imageJLabel.setSize(x, y);
							imageJLabel.setCursor(Cursor.getDefaultCursor());
						}
					});
					showCoursesJPanel.add(courseJPanel.getUI());
		        }
				showCoursesJPanel.repaint();
				showCoursesJPanel.validate();
				
				parent.add(showcoursesJScrollPane,BorderLayout.CENTER);
			}
			
			parent.repaint();
			parent.validate();
		}
	}
	
	private void createCenterMyCourseJPanel(){
		centerMyCourseJPanel.removeAll();
		centerMyCourseJPanel.setBackground(Color.WHITE);
		if(GOLBALVALUE.user.getUsername() == null){
			centerMyCourseJPanel.setLayout(new BorderLayout());
			JLabel titleJLabel = new JLabel("请先登录",JLabel.CENTER);
			titleJLabel.setForeground(Color.RED);
			titleJLabel.setFont(new Font("宋体", 1, 20));
			centerMyCourseJPanel.add(titleJLabel,BorderLayout.NORTH);
			centerMyCourseJPanel.add(new MyJLable("/image/noLesson.png", MainJFrame.this.getSize().width, MainJFrame.this.getSize().height-60, 0, 0));
		}else{
			List<CourseInf> courses = GetCoursesInf.getmyCourse();
			if(courses.size() == 0){
				centerMyCourseJPanel.setLayout(new BorderLayout());
				JLabel titleJLabel = new JLabel("暂时没有课程哦，快去添加吧",JLabel.CENTER);
				titleJLabel.setForeground(Color.RED);
				titleJLabel.setFont(new Font("宋体", 1, 20));
				centerMyCourseJPanel.add(titleJLabel,BorderLayout.NORTH);
				centerMyCourseJPanel.add(new MyJLable("/image/noLesson.png", MainJFrame.this.getSize().width, MainJFrame.this.getSize().height-60, 0, 0));
			}else{
				centerMyCourseJPanel.removeAll();
				centerMyCourseJPanel.setBackground(Color.WHITE);
				centerMyCourseJPanel.setLayout(new BorderLayout());
				JPanel showCoursesJPanel = new JPanel();
				showCoursesJPanel.setBackground(Color.WHITE);
				int rows = 0;
				if(courses.size()/4<5){
					rows = 5;
				} else {
					if (courses.size() % 5 == 0) {
						rows = courses.size() / 5;
					} else {
						rows = courses.size() / 5 + 1;
					}
				}
				showCoursesJPanel.setLayout(new FlowLayout(0));
				JScrollPane showcoursesJScrollPane = new JScrollPane(showCoursesJPanel);
				showcoursesJScrollPane.setPreferredSize(new Dimension(MainJFrame.this.getSize().width, MainJFrame.this.getSize().height));
				showcoursesJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				showcoursesJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				int height = rows * (MainJFrame.this.getSize().height/rows);
				showCoursesJPanel.setPreferredSize(new Dimension(0,height));
				int width = (MainJFrame.this.getSize().width)/5;
				final int x = width;
				final int y = MainJFrame.this.getSize().height/(rows);
				for (final CourseInf course:courses) {
					CourseJPanel courseJPanel = new CourseJPanel(course, x, y, showCoursesJPanel);
					courseJPanel.setImageSize(x, y);
					//课程图片设置点击事件
					final MyJLable imageJLabel = courseJPanel.getImageJLabel();
					imageJLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseClicked(e);
							currentCenterJPanel = INITCENTERBOFANG;
							boFangJLabel.setBackground("/image/bofang_pressed.png");
							mainJLable.setBackground("/image/main_normal.png");
							isBoFangClicked = true;
							isMainClicked = false;
							GOLBALVALUE.videoURLS = null;
							GetCoursesInf.getCourseVideoURLS(course);
							GOLBALVALUE.course = course;
							createCenterJPanel(currentCenterJPanel);
							isBofangOpen = true;
						}
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseEntered(e);
							imageJLabel.setSize(x+10, y+10);
							imageJLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseExited(e);
							imageJLabel.setSize(x, y);
							imageJLabel.setCursor(Cursor.getDefaultCursor());
						}
					});
					showCoursesJPanel.add(courseJPanel.getUI());
					centerMyCourseJPanel.add(showcoursesJScrollPane);
		        }
			}
		}
	}
	
	private void createCenterAllCoursesJPanel(){
		
	}
	
	private void createCenterBoFangJPanel(final EmbeddedMediaPlayerComponent play){
		//顶部课程名称
		final JLabel courseInfTitleJLabel = new JLabel();
		courseInfTitleJLabel.setText("    "+GOLBALVALUE.videoURLS.get(0).getCoursename());
		courseInfTitleJLabel.setForeground(MyColor.FONT_GRAY);
		courseInfTitleJLabel.setFont(new Font("宋体", 1, 20));
		courseInfTitleJLabel.setBackground(MyColor.BACKGROUND_DARK);
		centerBoFangJPanel.removeAll();
//		centerBoFangJPanel = new JPanel();
		centerBoFangJPanel.setLayout(new BorderLayout());
		centerBoFangJPanel.setBackground(MyColor.BACKGROUND_DARK);
		if(GOLBALVALUE.course != null){
			//初始化左侧视频集数
			final JPanel courseVideoMenuJPanel = new JPanel();
			courseVideoMenuJPanel.setPreferredSize(new Dimension(215,0));
			courseVideoMenuJPanel.setBackground(MyColor.BACKGROUND_DARK);
			courseVideoMenuJPanel.setLayout(new BorderLayout());
			//左侧上方课程信息
			JLabel courseNameJLabel = new JLabel();
			courseNameJLabel.setText("    "+GOLBALVALUE.course.getCoursename());
			courseNameJLabel.setForeground(MyColor.FONT_GRAY);
			courseNameJLabel.setFont(new Font("Dialog", 1, 20));
			//左侧课程集数信息
			JPanel courseVideoJPanel = new JPanel();
			courseVideoJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			courseVideoJPanel.setBackground(MyColor.BACKGROUND_LIGHT);
			for(int i=0;i<GOLBALVALUE.videoURLS.size();i++){
				final int j = i;
				final JLabel jlabel = new JLabel();
				jlabel.setText("  "+(i+1)+"  ");
				jlabel.setForeground(MyColor.FONT_GRAY);
				jlabel.setFont(new Font("Dialog", 1, 20));
				jlabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						super.mouseClicked(e);
						courseInfTitleJLabel.setText("    "+GOLBALVALUE.videoURLS.get(j).getCoursename());
						play.getMediaPlayer().playMedia(GOLBALVALUE.videoURLS.get(j).getCourseurl());
					}
					@Override
					public void mouseEntered(MouseEvent e) {//鼠标进入
						// TODO Auto-generated method stub
						super.mouseEntered(e);
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						jlabel.setForeground(MyColor.FONT_ORANGE_OVER);
					}

					public void mouseExited(MouseEvent e) {// 鼠标移除
						setCursor(Cursor.getDefaultCursor());
						jlabel.setForeground(MyColor.FONT_GRAY);
					}
				});
				courseVideoJPanel.add(jlabel);
			}
			//报名按钮和报名结果显示
			final JLabel applyJLabel = new JLabel("",JLabel.CENTER);
			final JLabel resultJLabel = new JLabel("");
			resultJLabel.setPreferredSize(new Dimension(200, 30));
			resultJLabel.setForeground(Color.RED);
			applyJLabel.setPreferredSize(new Dimension(200, 30));
			applyJLabel.setFont(new Font("宋体", 1, 20));
			if(GetCoursesInf.hascourse()){
				//已经有这门课,点击取消报名
				applyJLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						super.mouseClicked(e);
						if(GOLBALVALUE.user.getUsername() != null){
							if(GetCoursesInf.hascourse()){
								if(GetCoursesInf.deleteCourse()){
									applyJLabel.setForeground(MyColor.FONT_ORAGNE);
									applyJLabel.setText("立即报名");
									courseVideoMenuJPanel.repaint();
									courseVideoMenuJPanel.validate();
								}
							}
						}else{
							resultJLabel.setText("请先登录");
						}
						
					}
					
					public void mouseEntered(MouseEvent e) {// 鼠标进入
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						applyJLabel.setForeground(MyColor.LIGHT_GREEN);
					}

					public void mouseExited(MouseEvent e) {// 鼠标移除
						setCursor(Cursor.getDefaultCursor());
						applyJLabel.setForeground(MyColor.GREEN);
					}
				});
				applyJLabel.setText("取消报名");
				applyJLabel.setForeground(MyColor.GREEN);
			}else{
				//没有这门课，点击报名
				applyJLabel.setText("立即报名");
				applyJLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						super.mouseClicked(e);
						if(GOLBALVALUE.user.getUsername() != null){
							if (GetCoursesInf.hascourse()) {
								
							} else {
								if(GetCoursesInf.addCourse()){
									applyJLabel.setForeground(MyColor.GREEN);
									applyJLabel.setText("取消报名");
									courseVideoMenuJPanel.repaint();
									courseVideoMenuJPanel.validate();
								}
							}
						}else{
							resultJLabel.setText("请先登录");
						}
						
					}
					
					public void mouseEntered(MouseEvent e) {// 鼠标进入
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						applyJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
					}

					public void mouseExited(MouseEvent e) {// 鼠标移除
						setCursor(Cursor.getDefaultCursor());
						applyJLabel.setForeground(MyColor.FONT_ORAGNE);
					}
				});
				applyJLabel.setForeground(MyColor.FONT_ORAGNE);
			}
			courseVideoJPanel.add(applyJLabel);
			courseVideoJPanel.add(resultJLabel);
			//左侧添加组件
			courseVideoMenuJPanel.add(courseNameJLabel,BorderLayout.NORTH);
			courseVideoMenuJPanel.add(courseVideoJPanel,BorderLayout.CENTER);
//			courseVideoMenuJPanel.add(courseVideoBottomJPanel,BorderLayout.SOUTH);
			//初始化右侧评论界面
			final JPanel courseVideoCommentJPanel = new JPanel();
			courseVideoCommentJPanel.setBackground(MyColor.BACKGROUND_DARK);
			courseVideoCommentJPanel.setPreferredSize(new Dimension(270,0));
			GetCoursesInf.getComments(GOLBALVALUE.course);
			courseVideoCommentJPanel.setLayout(new BorderLayout(2,2));
			//右侧顶部的评论和课程信息
			JPanel courseVideoTitleJPanel = new JPanel();
			courseVideoTitleJPanel.setPreferredSize(new Dimension(200,60));
			courseVideoTitleJPanel.setBackground(MyColor.BACKGROUND_DARK);
			courseVideoTitleJPanel.setLayout(new GridLayout(1, 2));
			//右侧中部信息
			final JPanel courseVideoCommentsJPanel = new JPanel();
			courseVideoCommentsJPanel.setBackground(MyColor.BACKGROUND_DARK);
			courseVideoCommentsJPanel.setLayout(new BorderLayout());
			//初始化为评论信息
			JPanel panelContent = new JPanel();
			panelContent.setBackground(MyColor.BACKGROUND_DARK);
	        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.Y_AXIS));
			JScrollPane scrollPane = new JScrollPane(panelContent);  
			scrollPane.setBackground(MyColor.BACKGROUND_DARK);
			panelContent.setBackground(MyColor.BACKGROUND_DARK);
	        scrollPane.setPreferredSize(new Dimension(220 - 5, 0));
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	        int height = 100;
	        for (Comments comment:GOLBALVALUE.comments) {
	            panelContent.add(new CommentsJPanel(comment).getUI());
	            height += 100;
	        }
	        panelContent.setPreferredSize(new Dimension(270 - 5, height));
	        courseVideoCommentsJPanel.add(scrollPane, BorderLayout.CENTER);
	        
			//发布评论JPanel
			final JPanel sendCommentJPanel = new JPanel();
			sendCommentJPanel.setLayout(new BorderLayout());
			final JLabel isLoginJLabel = new JLabel();
			isLoginJLabel.setForeground(Color.RED);
			JPanel sendCommentBottomJPanel = new JPanel();
			sendCommentBottomJPanel.setLayout(new BorderLayout());
			sendCommentBottomJPanel.setBackground(MyColor.BACKGROUND_DARK);
			sendCommentBottomJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			final JTextArea commentJTextArea = new JTextArea();
			commentJTextArea.setForeground(MyColor.BACKGROUND_DARK);
			commentJTextArea.setPreferredSize(new Dimension(200,50));
			commentJTextArea.setBackground(Color.LIGHT_GRAY);
			final JLabel sendCommentJLabel = new JLabel("发表评论",SwingConstants.CENTER);
			sendCommentJLabel.setPreferredSize(new Dimension(80,40));
			sendCommentJLabel.setFont(new Font("宋体", 1, 15));
			sendCommentJLabel.setForeground(Color.WHITE);
			sendCommentJLabel.setBackground(MyColor.FONT_ORAGNE);
			sendCommentJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					if(GOLBALVALUE.user.getUsername() == null){
						isLoginJLabel.setText("请先登录");
					}else{
						if (commentJTextArea.getText().equals("")) {
							isLoginJLabel.setText("输入不能为空");
						}else{
							//发送信息
							Comments comment = new Comments();
							comment.setContent(commentJTextArea.getText());
							comment.setId(GOLBALVALUE.course.getId());
							comment.setSender(GOLBALVALUE.user.getUsername());
							comment.setSenderNickName(GOLBALVALUE.user.getNickname());
							comment.setSenderheadImage(GOLBALVALUE.user.getHeadimagePath());
							comment.setPraisenum(0);
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							String sendtime = sf.format(new Date());
							comment.setSendtime(sendtime);
							if(GetComments.addComment(comment)){
								GOLBALVALUE.comments.add(0,comment);
							}
							courseVideoCommentsJPanel.repaint();
							courseVideoCommentsJPanel.validate();
							commentJTextArea.setText("");
							isLoginJLabel.setText("");
						}
					}
					
				}
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					sendCommentJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
					sendCommentJLabel.setForeground(MyColor.FONT_GRAY);
				}
			});
			
			sendCommentBottomJPanel.add(isLoginJLabel,BorderLayout.CENTER);
			sendCommentBottomJPanel.add(sendCommentJLabel,BorderLayout.EAST);
			
			sendCommentJPanel.add(commentJTextArea,BorderLayout.NORTH);
			sendCommentJPanel.add(sendCommentBottomJPanel,BorderLayout.SOUTH);
			//右侧中部添加组件
			courseVideoCommentsJPanel.add(sendCommentJPanel,BorderLayout.NORTH);
			//评论按钮，课程信息按钮
			final JLabel commentsJLabel = new JLabel("评论",SwingConstants.CENTER);
			commentsJLabel.setFont(new Font("宋体", 1, 20));
			commentsJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
			final JLabel courseInfJLabel = new JLabel("课程");
			courseInfJLabel.setFont(new Font("宋体", 1, 20));
			courseInfJLabel.setForeground(MyColor.FONT_GRAY);
			commentsJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					courseVideoCommentsJPanel.removeAll();
					commentsJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
					courseInfJLabel.setForeground(MyColor.FONT_GRAY);
					JPanel panelContent = new JPanel();
					panelContent.setBackground(MyColor.BACKGROUND_DARK);
			        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.Y_AXIS));
					JScrollPane scrollPane = new JScrollPane(panelContent);  
					scrollPane.setBackground(MyColor.BACKGROUND_DARK);
					panelContent.setBackground(MyColor.BACKGROUND_DARK);
			        scrollPane.setPreferredSize(new Dimension(220 - 5, 0));
			        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			        int height = 100;
			        for (Comments comment:GOLBALVALUE.comments) {
			            panelContent.add(new CommentsJPanel(comment).getUI());
			            height += 100;
			        }
			        panelContent.setPreferredSize(new Dimension(270 - 5, height));
			        courseVideoCommentsJPanel.add(sendCommentJPanel,BorderLayout.NORTH);
			        courseVideoCommentsJPanel.add(scrollPane, BorderLayout.CENTER);
			        courseVideoCommentsJPanel.repaint();
					courseVideoCommentsJPanel.validate();
				}
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					commentsJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
					commentsJLabel.setForeground(MyColor.FONT_GRAY);
				}
			});
			courseInfJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					commentsJLabel.setForeground(MyColor.FONT_GRAY);
					courseInfJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
					courseVideoCommentsJPanel.removeAll();
					//初始化教师信息
					GetUserInf.getTeacher(GOLBALVALUE.course);
					//教师信息整体JPanel
					JPanel teacherJPanel = new JPanel();
					teacherJPanel.setBackground(MyColor.BACKGROUND_DARK);
					teacherJPanel.setLayout(new BorderLayout());
					//教师头像信息
					JPanel teacherImageJPanel = new JPanel();
					teacherImageJPanel.setLayout(new BorderLayout());
					teacherImageJPanel.setPreferredSize(new Dimension(50,70));
					teacherImageJPanel.setBackground(MyColor.BACKGROUND_DARK);
					MyJLable teacherImageJLabel = new MyJLable("",50,50,0,0);
					teacherImageJLabel.setPreferredSize(new Dimension(50, 50));
					GetImage.showJLabelImageFromUrl(teacherImageJLabel, GOLBALVALUE.teacherInf.getTeacherimage());
					//教师名称
					JLabel teachernameJLabel = new JLabel();
					teachernameJLabel.setText("    "+GOLBALVALUE.teacherInf.getTeachername());
					teachernameJLabel.setForeground(MyColor.FONT_GRAY);
					//教师头像添加组件
					teacherImageJPanel.add(teacherImageJLabel,BorderLayout.CENTER);
					teacherImageJPanel.add(teachernameJLabel,BorderLayout.SOUTH);
					teacherImageJPanel.add(new JLabel("    "),BorderLayout.EAST);
					teacherImageJPanel.add(new JLabel("    "),BorderLayout.WEST);
					
					//教师简介组件
					JTextArea teacherIntroductionJLabel = new JTextArea();
					teacherIntroductionJLabel.setEditable(false);
					teacherIntroductionJLabel.setLineWrap(true);
					teacherIntroductionJLabel.setWrapStyleWord(true);
					teacherIntroductionJLabel.setBackground(MyColor.BACKGROUND_DARK);
					teacherIntroductionJLabel.setText("教师介绍："+GOLBALVALUE.teacherInf.getTeacherintroduction());
					teacherIntroductionJLabel.setForeground(MyColor.FONT_GRAY);
					//课程详情
					JPanel courseInfJPanel = new JPanel();
					courseInfJPanel.setPreferredSize(new Dimension(200,80));
					courseInfJPanel.setBackground(MyColor.BACKGROUND_DARK);
					courseInfJPanel.setLayout(new BorderLayout());
					JLabel courseInfTitleJLabel = new JLabel();
					courseInfTitleJLabel.setText("    课程详情：");
					JLabel courseInfJLabel = new JLabel();
					courseInfJLabel.setText(GOLBALVALUE.course.getCourseintroduction());
					courseInfJPanel.add(courseInfTitleJLabel,BorderLayout.NORTH);
					courseInfTitleJLabel.setForeground(MyColor.FONT_GRAY);
					courseInfJLabel.setForeground(MyColor.FONT_GRAY);
					courseInfJPanel.add(courseInfJLabel,BorderLayout.CENTER);
					courseInfJPanel.add(new JLabel("    "),BorderLayout.EAST);
					courseInfJPanel.add(new JLabel("    "),BorderLayout.WEST);
					
					courseVideoCommentsJPanel.setPreferredSize(new Dimension(0,200));
					courseVideoCommentsJPanel.add(teacherImageJPanel,BorderLayout.NORTH);
					courseVideoCommentsJPanel.add(teacherIntroductionJLabel,BorderLayout.CENTER);
					courseVideoCommentsJPanel.add(new JLabel("    "),BorderLayout.EAST);
					courseVideoCommentsJPanel.add(new JLabel("    "),BorderLayout.WEST);
					courseVideoCommentsJPanel.add(courseInfJPanel,BorderLayout.SOUTH);
					courseVideoCommentsJPanel.repaint();
					courseVideoCommentsJPanel.validate();
				}
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					courseInfJLabel.setForeground(MyColor.FONT_ORANGE_OVER);
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
					courseInfJLabel.setForeground(MyColor.FONT_GRAY);
				}
			});
			courseVideoTitleJPanel.add(commentsJLabel);
			courseVideoTitleJPanel.add(courseInfJLabel);
			
			//右侧添加组件
			courseVideoCommentJPanel.add(courseVideoTitleJPanel,BorderLayout.NORTH);
			courseVideoCommentJPanel.add(courseVideoCommentsJPanel,BorderLayout.CENTER);
			//初始化中部播放界面
			JPanel courseVideoBoFangJPanel = new JPanel();
			courseVideoBoFangJPanel.setBackground(MyColor.BACKGROUND_DARK);
			courseVideoBoFangJPanel.setLayout(new BorderLayout());
//			courseVideoBoFangJPanel.setVisible(true);
			//视频控制部分
			JPanel coursevideoControlJPanel = new JPanel();
			coursevideoControlJPanel.setPreferredSize(new Dimension(0, 50));
			coursevideoControlJPanel.setBackground(MyColor.BACKGROUND_DARK);
			coursevideoControlJPanel.setLayout(new BorderLayout());
			final JProgressBar progressBar = new JProgressBar();
			progressBar.addMouseListener(new MouseAdapter() {  
	            @Override  
	            public void mouseClicked(MouseEvent e){     //点击进度条调整视屏播放进度  
	                int x=e.getX();  
	                play.getMediaPlayer().setTime((long)((float)x/progressBar.getWidth()*play.getMediaPlayer().getLength()));  
	            }  
	        });
	        //播放控制中部组件
			JPanel controlCenter = new JPanel();
			controlCenter.setLayout(new FlowLayout(FlowLayout.LEFT));
			controlCenter.setBackground(MyColor.BACKGROUND_DARK);
			//暂停按钮
			final MyJLable pauseJLanel = new MyJLable("/image/stop.png",30,30,2,2);
			pauseJLanel.setPreferredSize(new Dimension(30, 30));
			if(play.getMediaPlayer().isPlaying()){
				pauseJLanel.setBackground("/image/stop.png");
			}else{
				pauseJLanel.setBackground("/image/start.png");
			}
			pauseJLanel.addMouseListener(new MouseAdapter() {
				boolean isClicked = true;
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					if(isClicked){
						pauseJLanel.setBackground("/image/start.png");
						play.getMediaPlayer().pause();
					}else{
						pauseJLanel.setBackground("/image/stop.png");
						play.getMediaPlayer().play();
					}
					isClicked = !isClicked;
				}
				
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
				}
			});
			//当前时间和总时间
			JLabel currentTimeJLabel = new JLabel();
			currentTimeJLabel.setForeground(MyColor.FONT_GRAY);
			JLabel jiangeJLabel = new JLabel();
			jiangeJLabel.setText("/");
			jiangeJLabel.setForeground(MyColor.FONT_GRAY);
			jiangeJLabel.setFont(new Font("宋体", 1, 20));
			JLabel totalTimeJLabel = new JLabel();
			totalTimeJLabel.setForeground(MyColor.FONT_GRAY);
			
			UpdateProgressBar thread = new UpdateProgressBar(play, progressBar,currentTimeJLabel,totalTimeJLabel);
			controlCenter.add(pauseJLanel);
			controlCenter.add(currentTimeJLabel);
			controlCenter.add(jiangeJLabel);
			controlCenter.add(totalTimeJLabel);
			//音量条和放大按钮布局
			JPanel controlEastJPanel = new JPanel();
			controlEastJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			controlEastJPanel.setBackground(MyColor.BACKGROUND_DARK);
			final JSlider slider=new JSlider();  
			slider.setBackground(MyColor.BACKGROUND_DARK);
	        slider.setValue(80);  
	        slider.setMaximum(100);  
	        slider.addChangeListener(new ChangeListener() {  
	              
	            @Override  
	            public void stateChanged(ChangeEvent e) {  
	                // TODO Auto-generated method stub  
	                play.getMediaPlayer().setVolume(slider.getValue());
	            }  
	        });
	        final MyJLable screenJLabel = new MyJLable("/image/min.png", 20, 20, 0, 0);
	        screenJLabel.setPreferredSize(new Dimension(20, 20));
	        screenJLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					if(isHideComments){
						centerBoFangJPanel.remove(courseVideoMenuJPanel);
						centerBoFangJPanel.remove(courseVideoCommentJPanel);
						screenJLabel.setBackground("/image/min.png");
					}else{
						screenJLabel.setBackground("/image/screen.png");
						centerBoFangJPanel.add(courseVideoMenuJPanel,BorderLayout.WEST);
						centerBoFangJPanel.add(courseVideoCommentJPanel,BorderLayout.EAST);
					}
					isHideComments = !isHideComments;
					centerBoFangJPanel.repaint();
					centerBoFangJPanel.validate();
				}
				public void mouseEntered(MouseEvent e) {// 鼠标进入
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {// 鼠标移除
					setCursor(Cursor.getDefaultCursor());
				}
			});
	        controlEastJPanel.add(screenJLabel);
	        controlEastJPanel.add(slider);
	        controlEastJPanel.add(new JLabel("    "));
			//播放视频控制栏添加组件
			coursevideoControlJPanel.add(progressBar,BorderLayout.NORTH);
			coursevideoControlJPanel.add(controlCenter,BorderLayout.CENTER);
			coursevideoControlJPanel.add(new JLabel("  "),BorderLayout.WEST);
			coursevideoControlJPanel.add(controlEastJPanel,BorderLayout.EAST);
			//视频组件添加
			courseVideoBoFangJPanel.add(courseInfTitleJLabel,BorderLayout.NORTH);
			courseVideoBoFangJPanel.add(play,BorderLayout.CENTER);
			courseVideoBoFangJPanel.add(coursevideoControlJPanel,BorderLayout.SOUTH);
//			playerComponent.getMediaPlayer().playMedia("http://192.168.191.1:8080/LessonServer/coursevideo/5/1.mp4");
			
			//添加组件
			if(isHideComments){
				centerBoFangJPanel.add(courseVideoMenuJPanel,BorderLayout.WEST);
				centerBoFangJPanel.add(courseVideoCommentJPanel,BorderLayout.EAST);
			}
			centerBoFangJPanel.add(courseVideoBoFangJPanel,BorderLayout.CENTER);
		}
	}
	
	private void createCenterSearchJPanel(){
//		centerSearchJPanel
		centerSearchJPanel.setBackground(Color.WHITE);
		if(searchJTextField.getText().toString().trim().equals("")||searchJTextField.getText().toString().trim().equals("请输入你想搜索的东西")){
			
		}else{
			List<CourseInf> courses = GetCoursesInf.searchcourses(searchJTextField.getText());
			if(courses.size() == 0){
				centerSearchJPanel.setLayout(new BorderLayout());
				//头部显示信息
				JLabel titleJLabel = new JLabel("",JLabel.CENTER);
				titleJLabel.setText("暂无搜索结果，换个关键词试试");
				titleJLabel.setForeground(Color.RED);
				titleJLabel.setFont(new Font("宋体",1,16));
				//中部显示图片
				MyJLable centerPictureJPabel = new MyJLable("/image/noLesson.png",MainJFrame.this.getSize().width-10,MainJFrame.this.getSize().height-60,0,0);
				centerSearchJPanel.add(titleJLabel,BorderLayout.NORTH);
				centerSearchJPanel.add(centerPictureJPabel,BorderLayout.CENTER);
				centerSearchJPanel.repaint();
				centerSearchJPanel.validate();
			}else{
				centerSearchJPanel.removeAll();
				centerSearchJPanel.setBackground(Color.WHITE);
				centerSearchJPanel.setLayout(new BorderLayout());
				JPanel showCoursesJPanel = new JPanel();
				showCoursesJPanel.setBackground(Color.WHITE);
				int rows = 0;
				if(courses.size()/4<5){
					rows = 5;
				} else {
					if (courses.size() % 5 == 0) {
						rows = courses.size() / 5;
					} else {
						rows = courses.size() / 5 + 1;
					}
				}
				showCoursesJPanel.setLayout(new FlowLayout(0));
				JScrollPane showcoursesJScrollPane = new JScrollPane(showCoursesJPanel);
				showcoursesJScrollPane.setPreferredSize(new Dimension(MainJFrame.this.getSize().width, MainJFrame.this.getSize().height));
				showcoursesJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				showcoursesJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				int height = rows * (MainJFrame.this.getSize().height/rows);
				showCoursesJPanel.setPreferredSize(new Dimension(0,height));
				int width = (MainJFrame.this.getSize().width)/5;
				final int x = width;
				final int y = MainJFrame.this.getSize().height/(rows);
				for (final CourseInf course:courses) {
					CourseJPanel courseJPanel = new CourseJPanel(course, x, y, showCoursesJPanel);
					courseJPanel.setImageSize(x, y);
					//课程图片设置点击事件
					final MyJLable imageJLabel = courseJPanel.getImageJLabel();
					imageJLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseClicked(e);
							currentCenterJPanel = INITCENTERBOFANG;
							boFangJLabel.setBackground("/image/bofang_pressed.png");
							mainJLable.setBackground("/image/main_normal.png");
							isBoFangClicked = true;
							isMainClicked = false;
							GOLBALVALUE.videoURLS = null;
							GetCoursesInf.getCourseVideoURLS(course);
							GOLBALVALUE.course = course;
							createCenterJPanel(currentCenterJPanel);
							isBofangOpen = true;
						}
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseEntered(e);
							imageJLabel.setSize(x+10, y+10);
							imageJLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							super.mouseExited(e);
							imageJLabel.setSize(x, y);
							imageJLabel.setCursor(Cursor.getDefaultCursor());
						}
					});
					showCoursesJPanel.add(courseJPanel.getUI());
					centerSearchJPanel.add(showcoursesJScrollPane);
				}
			}
		}
	}
	
	private void createCenterUserInfJPanel(){
		centerUserInfJPanel.removeAll();
		centerUserInfJPanel.setLayout(new BorderLayout());
		final UserInf user = new UserInf();
		user.setBirthday(GOLBALVALUE.user.getBirthday());
		user.setEmail(GOLBALVALUE.user.getEmail());
		user.setHeadimagePath(GOLBALVALUE.user.getHeadimagePath());
		user.setIntroduction(GOLBALVALUE.user.getIntroduction());
		user.setNickname(GOLBALVALUE.user.getNickname());
		user.setSchool(GOLBALVALUE.user.getSchool());
		user.setSex(GOLBALVALUE.user.getSex());
		user.setUsername(GOLBALVALUE.user.getUsername());
		//初始化头部头像，上传头像按钮
		JPanel titleJPanel = new JPanel();
		titleJPanel.setPreferredSize(new Dimension(MainJFrame.this.getSize().width, 100));
		titleJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		titleJPanel.setBackground(Color.WHITE);
		final MyJLable headImageJLabel1 = new MyJLable("/image/default_personal_image.png", 100, 100, 0, 0);
		headImageJLabel1.setPreferredSize(new Dimension(90,90));
		new GetImage().showJLabelImageFromUrl(headImageJLabel1, GOLBALVALUE.user.getHeadimagePath());
		JButton changeHeadImageButton = new JButton("更改头像");
		changeHeadImageButton.setBackground(Color.LIGHT_GRAY);
		changeHeadImageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				JFileChooser chooser=new JFileChooser();  
		        int v=chooser.showOpenDialog(null);  
		        if(v==JFileChooser.APPROVE_OPTION){  
		            File file=chooser.getSelectedFile();  
		            System.out.println(file);
		            if(file.getAbsolutePath().endsWith(".png")||file.getAbsolutePath().endsWith(".jpg")
		            	||file.getAbsolutePath().endsWith(".bmp")){
		            	if(ImageUtil.changeImage(file.getAbsolutePath())){
		            		ImageUtil.uploadFile("d://"+GOLBALVALUE.user.getEmail()+".png");
		            		headImageJLabel.setBackground("d://"+GOLBALVALUE.user.getEmail()+".png",1);
			            	headImageJLabel1.setBackground("d://"+GOLBALVALUE.user.getEmail()+".png",1);
			            	MainJFrame.this.repaint();
			            	MainJFrame.this.validate();
		            	}
		            }else{
		            	
		            }
		        }
			}
		});
		titleJPanel.add(new JLabel("    "));
		titleJPanel.add(headImageJLabel1);
		titleJPanel.add(changeHeadImageButton);
		//初始化中部界面
		JPanel centerUserInfJPanel1 = new JPanel();
		centerUserInfJPanel1.setLayout(new BorderLayout());
		//中部其他信息界面
		int width = (MainJFrame.this.getSize().width - 200)/2;
		JPanel otherInfJpanel = new JPanel();
		otherInfJpanel.setLayout(new GridLayout(3,2));
		otherInfJpanel.setBackground(Color.WHITE);
		JPanel tempJPanel = new JPanel();
		tempJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//nicknameJTextField
		JPanel tempJPanel1 = new JPanel();
		tempJPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempJPanel1.setBackground(Color.WHITE);
		final JTextField nicknameJTextField = new JTextField();
		nicknameJTextField.setText(GOLBALVALUE.user.getNickname());
		nicknameJTextField.setForeground(MyColor.FONT_GRAY);
		nicknameJTextField.setPreferredSize(new Dimension(width, 30));
		tempJPanel1.add(new JLabel("昵称"));
		tempJPanel1.add(nicknameJTextField);
		otherInfJpanel.add(tempJPanel1);
		//birthdayJTextField
		JPanel tempJPanel2 = new JPanel();
		tempJPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempJPanel2.setBackground(Color.WHITE);;
		JTextField birthdayJTextField = new JTextField();
		birthdayJTextField.setText(GOLBALVALUE.user.getBirthday());
		birthdayJTextField.setForeground(MyColor.FONT_GRAY);
		birthdayJTextField.setPreferredSize(new Dimension(width, 30));
		birthdayJTextField.setEditable(false);
		tempJPanel2.add(new JLabel("生日"));
		tempJPanel2.add(birthdayJTextField);
		otherInfJpanel.add(tempJPanel2);
		//sexJTextField
		JPanel tempJPanel3 = new JPanel();
		tempJPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempJPanel3.setBackground(Color.WHITE);;
		final JTextField sexJTextField = new JTextField();
		if(GOLBALVALUE.user.getSex() == 0){
			sexJTextField.setText("男");
		}else{
			sexJTextField.setText("女");
		}
		sexJTextField.setEditable(false);
		sexJTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if(sexJTextField.getText().equals("男")){
					sexJTextField.setText("女");
				}else{
					sexJTextField.setText("男");
				}
			}
		});
		sexJTextField.setForeground(MyColor.FONT_GRAY);
		sexJTextField.setPreferredSize(new Dimension(width, 30));
		tempJPanel3.add(new JLabel("性别"));
		tempJPanel3.add(sexJTextField);
		otherInfJpanel.add(tempJPanel3);
		//schoolJTextField
		JPanel tempJPanel4 = new JPanel();
		tempJPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempJPanel4.setBackground(Color.WHITE);
		final JTextField schoolJTextField = new JTextField();
		schoolJTextField.setText(GOLBALVALUE.user.getSchool());
		schoolJTextField.setForeground(MyColor.FONT_GRAY);
		schoolJTextField.setPreferredSize(new Dimension(width, 30));
		tempJPanel4.add(new JLabel("学校"));
		tempJPanel4.add(schoolJTextField);
		otherInfJpanel.add(tempJPanel4);
		//emailJTextField
		JPanel tempJPanel5 = new JPanel();
		tempJPanel5.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempJPanel5.setBackground(Color.WHITE);
		JTextField emailJTextField = new JTextField();
		emailJTextField.setText(GOLBALVALUE.user.getEmail());
		emailJTextField.setForeground(MyColor.FONT_GRAY);
		emailJTextField.setPreferredSize(new Dimension(width, 30));
		emailJTextField.setEditable(false);
		tempJPanel5.add(new JLabel("邮箱"));
		tempJPanel5.add(emailJTextField);
		otherInfJpanel.add(tempJPanel5);
		//介绍JPanel
		JPanel introductionJPanel = new JPanel();
		introductionJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		introductionJPanel.setBackground(Color.WHITE);
		introductionJPanel.add(new JLabel("介绍："));
		final JTextArea introductionJTextArea = new JTextArea();
		introductionJTextArea.setBackground(MyColor.LIGHT_GRAY);
		introductionJTextArea.setPreferredSize(new Dimension(MainJFrame.this.getSize().width-40, 100));
		introductionJTextArea.setText(GOLBALVALUE.user.getIntroduction());
		introductionJPanel.add(introductionJTextArea);
		//提交按钮
		JLabel submitJLabel = new JLabel("提交",JLabel.CENTER);
		submitJLabel.setFont(new Font("宋体", 1, 20));
		submitJLabel.setForeground(MyColor.FONT_ORAGNE);
		submitJLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				user.setIntroduction(introductionJTextArea.getText().toString().trim());
				user.setNickname(nicknameJTextField.getText().toString().trim());
				user.setSchool(schoolJTextField.getText().toString().trim());
				if(sexJTextField.getText().equals("男")){
					user.setSex(0);
				}else{
					user.setSex(1);
				}
				if(GetUserInf.submitUser(user)){
					MainJFrame.this.repaint();
					MainJFrame.this.validate();
				}
			}
			
			public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
			}
		});
		introductionJPanel.add(submitJLabel);
		centerUserInfJPanel1.add(otherInfJpanel,BorderLayout.NORTH);
		centerUserInfJPanel1.add(introductionJPanel,BorderLayout.CENTER);
		//添加组件
		centerUserInfJPanel.add(titleJPanel,BorderLayout.NORTH);
		centerUserInfJPanel.add(new JLabel("        "),BorderLayout.WEST);
		centerUserInfJPanel.add(new JLabel("        "),BorderLayout.EAST);
		centerUserInfJPanel.add(centerUserInfJPanel1,BorderLayout.CENTER);
	}
	
	private JPanel initCenterRightPanel(int id){
		System.out.println(id);
		
		return null;
	}
	
	public void setWindowIcon() {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(
				"/image/welcome_logo.png"));
		this.setIconImage(imageIcon.getImage());
	}
	
	public JPanel createUserInfPopUpJPanel(){
		//整个弹窗的布局
		JPanel infoPanel = new JPanel();  
		infoPanel.removeAll();
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setPreferredSize(new Dimension(280, 100));
        infoPanel.setLayout(new BorderLayout());
		//弹窗右部的布局
		JPanel infoRightJPanel = new JPanel();
		infoRightJPanel.setBackground(Color.WHITE);
		infoRightJPanel.setLayout(new GridLayout(2, 1));
		//弹窗下部的布局
		JPanel infoBottomJPanel = new JPanel();
		infoBottomJPanel.setLayout(new FlowLayout(0));
		infoBottomJPanel.setBackground(Color.WHITE);
        //初始化头像JLabel
        final MyJLable infoHeadImageJLabel = new MyJLable("/image/default_personal_image.png", 50, 50, 10, 20);
        infoHeadImageJLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseClicked(e);
        		if(GOLBALVALUE.user.getUsername() != null){
        			currentCenterJPanel = INITCENTERUSERINF;
            		createCenterJPanel(currentCenterJPanel);
        		}
        	}
        	
        	public void mouseEntered(MouseEvent e) {// 鼠标进入
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {// 鼠标移除
				setCursor(Cursor.getDefaultCursor());
			}
		});
        infoHeadImageJLabel.setPreferredSize(new Dimension(50, 50));
        System.out.println(GOLBALVALUE.user.getHeadimagePath());
        if(GOLBALVALUE.user.getHeadimagePath() != null){
        	new GetImage().showJLabelImageFromUrl(infoHeadImageJLabel, GOLBALVALUE.user.getHeadimagePath());
        }//初始化昵称
        final JLabel nicknameJLabel = new JLabel("    呆萌小二逼");
        infoRightJPanel.add(nicknameJLabel);
      //初始化用户名
        final JLabel usernameJLabel = new JLabel("    m13814545863@163.com");
        infoRightJPanel.add(usernameJLabel);
        usernameJLabel.setForeground(MyColor.FONT_GRAY);
        if(GOLBALVALUE.user.getUsername() == null && GOLBALVALUE.user.getNickname() == null){
        	nicknameJLabel.setText("");
        	usernameJLabel.setText("");
        }else{
        	nicknameJLabel.setText(GOLBALVALUE.user.getNickname());
        	usernameJLabel.setText(GOLBALVALUE.user.getUsername());
        }
        //初始化登录
        final JLabel loginJLabel = new JLabel();
        loginJLabel.setText("登录");
        loginJLabel.setForeground(new Color(237, 125, 49));
        loginJLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseClicked(e);
        		new LoginJFrame();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseEntered(e);
        		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        		loginJLabel.setForeground(new Color(242, 165, 113));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseExited(e);
        		setCursor(Cursor.getDefaultCursor());  
        		loginJLabel.setForeground(new Color(237, 125, 49));
        	}
		});
        //初始化退出
        final JLabel exitJLabel = new JLabel();
        exitJLabel.setText("退出");
        exitJLabel.setForeground(new Color(237, 125, 49));
        exitJLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseClicked(e);
        		GOLBALVALUE.user = new UserInf();
        		usernameJLabel.setText("");
        		nicknameJLabel.setText("");
        		infoHeadImageJLabel.setBackground("/image/default_personal_image.png");
        		headImageJLabel.setBackground("/image/default_personal_image.png");
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseEntered(e);
        		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        		exitJLabel.setForeground(new Color(242, 165, 113));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseExited(e);
        		setCursor(Cursor.getDefaultCursor());  
        		exitJLabel.setForeground(new Color(237, 125, 49));
        	}
		});
        //初始化注册
        final JLabel registerJLabel = new JLabel();
        registerJLabel.setText("注册");
        registerJLabel.setForeground(new Color(237, 125, 49));
        registerJLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseClicked(e);
        		Desktop desktop = Desktop.getDesktop();  
        		try {
					desktop.browse(new URI("http://192.168.191.1:8080/LessonServer/main.jsp"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseEntered(e);
        		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        		registerJLabel.setForeground(new Color(242, 165, 113));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		// TODO Auto-generated method stub
        		super.mouseExited(e);
        		setCursor(Cursor.getDefaultCursor());  
        		registerJLabel.setForeground(new Color(237, 125, 49));
        	}
		});
        //初始化底部中间空白区域
        JLabel bottomNullJLabel = new JLabel();
        bottomNullJLabel.setPreferredSize(new Dimension(210, 30));
        //底部添加信息
        if(GOLBALVALUE.user.getUsername() == null){
        	infoBottomJPanel.add(loginJLabel);
            infoBottomJPanel.add(bottomNullJLabel);
            infoBottomJPanel.add(registerJLabel);
        }else{
        	infoBottomJPanel.add(new JLabel("  "));
            infoBottomJPanel.add(bottomNullJLabel);
            infoBottomJPanel.add(exitJLabel);
        }
        //添加布局
        infoPanel.add(infoHeadImageJLabel,BorderLayout.WEST);
        infoPanel.add(infoRightJPanel,BorderLayout.CENTER);
        infoPanel.add(infoBottomJPanel,BorderLayout.SOUTH);
        infoHeadImageJLabel.repaint();
        infoHeadImageJLabel.validate();
        infoRightJPanel.repaint();
        infoRightJPanel.validate();
        infoPanel.repaint();
        infoPanel.validate();
        return infoPanel;
	}

	public void openURL(String url){  
        try { 
            String command = "cmd /c start iexplore ";  
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://www.baidu.com");
        } catch (Exception e) {  
            System.out.println(e);  
        }  
    }
	
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(  
                RuntimeUtil.getLibVlcLibraryName(), "D:\\VLC\\sdk\\lib");  //导入的路径是vlc的安装路径  
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		final MainJFrame mainJFrame = new MainJFrame();
		mainJFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				// TODO Auto-generated method stub
				super.mousePressed(event);
				point.x = event.getX();
				point.y = event.getY();
			}
		});
		mainJFrame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				// TODO Auto-generated method stub
				super.mouseDragged(event);
				Point p = mainJFrame.getLocation();
				mainJFrame.setLocation(p.x + event.getX() - point.x, p.y
						+ event.getY() - point.y);
			}
		});
	}
	public void clearPlayer(){
		for(EmbeddedMediaPlayerComponent play:playList){
			play.getMediaPlayer().pause();
		}
		playList.clear();
	}
	
}
