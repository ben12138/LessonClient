package com.lesson.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GOLBALVALUE {
	public static Map<String, HashMap<String, HashMap<String,String>>> map = new HashMap<String, HashMap<String, HashMap<String,String>>>();
	public static List<String> coursetype2;
	public static UserInf user = new UserInf();
	public static List<CourseUrl> videoURLS;
	public static CourseInf course = null;
	public static List<Comments> comments = null;
	public static Teacher teacherInf = null;
	public static String coursetype1str = null;
	public static String coursetype2str = null;
	public static String coursetype3str = null;
	public static int id = 0;
}
