package com.lesson.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lesson.bean.Comments;
import com.lesson.bean.CourseInf;
import com.lesson.bean.CourseUrl;
import com.lesson.bean.GOLBALVALUE;
import com.lesson.net.NetConnection;

public class GetCoursesInf {
	public static void getAllTypeInf(){
		String urlString = NetConnection.url+"GetCourseInf";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "phone"));
		list.add(new BasicNameValuePair("getInf", "allcoursename"));
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response;
			response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			JSONArray jsonarray = new JSONArray(result);
			String coursetype1 = null;
			String coursetype2 = null;
			for(int i=0;i<jsonarray.length();i++){
				JSONObject json = jsonarray.getJSONObject(i);
				if(json.has("coursetype1")){
					coursetype1 = json.getString("coursetype1");
					GOLBALVALUE.map.put(coursetype1, new HashMap<String,HashMap<String,String>>());
				}else if(json.has("coursetype2")){
					coursetype2 = json.getString("coursetype2");
					if(!GOLBALVALUE.map.get(coursetype1).containsKey(coursetype2)){
						GOLBALVALUE.map.get(coursetype1).put(coursetype2, new HashMap<String,String>());
					}
				}else{
					GOLBALVALUE.map.get(coursetype1).get(coursetype2).put(json.getString("id"), (String) json.get("coursetype3"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, ArrayList<CourseInf>> getTuiSongCourseInf(){
		HashMap<String, ArrayList<CourseInf>> map = new HashMap<String, ArrayList<CourseInf>>();
		String urlString = NetConnection.url+"GetCourseInf";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "phone"));
		list.add(new BasicNameValuePair("getInf", "tuisong"));
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response;
			response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			JSONArray jsonarray = new JSONArray(result);
			String coursename = "";
			for(int i=0;i<jsonarray.length();i++){
				JSONObject json = jsonarray.getJSONObject(i);
				if(json.has("coursetype1")){
					map.put(json.getString("coursetype1"), new ArrayList<CourseInf>());
					coursename = json.getString("coursetype1");
				}else{
					CourseInf course = new CourseInf();
					course.setId(Integer.parseInt(json.getString("id")));
					course.setCourseid(Integer.parseInt(json.getString("courseid")));
					course.setTeacherid(Integer.parseInt(json.getString("teacherid")));
					course.setCoursename(json.getString("coursename"));
					course.setCourseintroduction(json.getString("courseintroduction"));
					course.setCoursedegree(Double.parseDouble(json.getString("coursedegree")));
					course.setCoursecomments(json.getString("coursecomments"));
					course.setCatalogue(json.getString("catalogue"));
					course.setAndroidimage(json.getString("androidimage"));
					course.setWatchernum(Integer.parseInt(json.getString("watchernum")));
					map.get(coursename).add(course);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static void getCourseVideoURLS(CourseInf course){
		GOLBALVALUE.videoURLS = new ArrayList<>();
		String urlString = NetConnection.url+"GetCourseVideoServlet";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("courseinfid", course.getId()+""));
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response;
			response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			if (!result.equals("null")) {
				JSONArray jsonarray = new JSONArray(result);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject json = jsonarray.getJSONObject(i);
					CourseUrl temp = new CourseUrl();
					temp.setId(json.getInt("id"));
					temp.setCourseinfid(json.getInt("courseinfid"));
					temp.setCoursename(json.getString("coursename"));
					temp.setCourseurl(json.getString("courseurl"));
					GOLBALVALUE.videoURLS.add(temp);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getComments(CourseInf course){
		GOLBALVALUE.comments = new ArrayList<>();
		String urlString = NetConnection.url+"GetAllCommentsServlet";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("courseinfid", course.getId()+""));
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response;
			response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			if (!result.equals("null")) {
				JSONArray jsonarray = new JSONArray(result);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject json = jsonarray.getJSONObject(i);
					Comments comment = new Comments();
					comment.setId(json.getInt("id"));
					comment.setContent(json.getString("content"));
					comment.setPraisenum(json.getInt("praisenum"));
					comment.setSender(json.getString("sender"));
					comment.setSenderheadImage(json.getString("senderheadimage"));
					comment.setSenderNickName(json.getString("sendernickname"));
					comment.setSendtime(json.getString("sendertime"));
					GOLBALVALUE.comments.add(comment);
				}
			}
			System.out.println(GOLBALVALUE.comments);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<CourseInf> getCourses(String coursetype1,String coursetype2,String coursetype3){
		List<CourseInf> courses = new ArrayList<>();
		String urlString = NetConnection.url+"GetCourseInf";
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "phone"));
		list.add(new BasicNameValuePair("getInf", "getcourses"));
		list.add(new BasicNameValuePair("coursetype1", coursetype1));
		list.add(new BasicNameValuePair("coursetype2", coursetype2));
		list.add(new BasicNameValuePair("coursetype3", coursetype3));
		HttpClient client = new DefaultHttpClient();
		String json = null;
		HttpResponse response;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list,HTTP.UTF_8);//设置编码，防止中午乱码
			post.setEntity(entity);
			response = client.execute(post);
			json = EntityUtils.toString(response.getEntity());
			if(json != null && !json.equals("null")){
				courses = new ArrayList<>();
				JSONArray jsonarray = new JSONArray(json);
				for(int i=0;i<jsonarray.length();i++){
					net.sf.json.JSONObject jsonObj = (net.sf.json.JSONObject) jsonarray.get(i);
					CourseInf course = new CourseInf();
					course.setId(jsonObj.getInt("id"));
					course.setCourseid(jsonObj.getInt("courseid"));
					course.setTeacherid(jsonObj.getInt("teacherid"));
					course.setCoursename(jsonObj.getString("coursename"));
					course.setCourseintroduction(jsonObj.getString("courseintroduction"));
					course.setCoursedegree(jsonObj.getDouble("coursedegree"));
					course.setCoursecomments(jsonObj.getString("coursecomments"));
					course.setCatalogue(jsonObj.getString("catalogue"));
					course.setAndroidimage(jsonObj.getString("androidimage"));
					course.setWatchernum(jsonObj.getInt("watchernum"));
					courses.add(course);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return courses;
	}
	
	public static List<CourseInf> getmyCourse(){
		List<CourseInf> courses = new ArrayList<>();
		String url = NetConnection.url
				+ "GetCourseInf?type=phone&getInf=getmycourse&username="
				+ GOLBALVALUE.user.getUsername();
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			String result = EntityUtils.toString(response.getEntity());
			if (!result.equals("null")) {
				JSONArray jsonarray = new JSONArray(result);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject json = jsonarray.getJSONObject(i);
					CourseInf course = new CourseInf();
					course.setId(json.getInt("id"));
					course.setCourseid(json.getInt("courseid"));
					course.setTeacherid(json.getInt("teacherid"));
					course.setCoursename(json.getString("coursename"));
					course.setCourseintroduction(json
							.getString("courseintroduction"));
					course.setCoursedegree(json.getDouble("coursedegree"));
					course.setCoursecomments(json.getString("coursecomments"));
					course.setCatalogue(json.getString("catalogue"));
					course.setAndroidimage(json.getString("androidimage"));
					course.setWatchernum(json.getInt("watchernum"));
					courses.add(course);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}
	
	public static boolean hascourse(){
		String url = NetConnection.url+"/AddCourseServlet";
		String result = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "hascourse"));
		list.add(new BasicNameValuePair("username", GOLBALVALUE.user.getUsername()));
		list.add(new BasicNameValuePair("courseinfid", GOLBALVALUE.course.getId()+""));
		list.add(new BasicNameValuePair("device", "phone"));
		HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		if(result.equals("has")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean deleteCourse(){
		String urlString = NetConnection.url+"/AddCourseServlet";
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "deletecourse"));
		list.add(new BasicNameValuePair("username", GOLBALVALUE.user.getUsername()));
		list.add(new BasicNameValuePair("courseinfid", GOLBALVALUE.course.getId()+""));
		list.add(new BasicNameValuePair("device", "phone"));
		HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 设置编码，防止中午乱码
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("success")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean addCourse(){
		String urlString = NetConnection.url+"/AddCourseServlet";
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "addcourse"));
		list.add(new BasicNameValuePair("username", GOLBALVALUE.user.getUsername()));
		list.add(new BasicNameValuePair("courseinfid", GOLBALVALUE.course.getId()+""));
		list.add(new BasicNameValuePair("device", "phone"));
		HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("success")){
			return true;
		}else{
			return false;
		}
	}

	public static List<CourseInf> searchcourses(String name){
		String url = NetConnection.url+"/GetCourseInf";
		List<CourseInf> courses = new ArrayList<>();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("type", "phone"));
		list.add(new BasicNameValuePair("getInf", "searchcourses"));
		list.add(new BasicNameValuePair("name", name));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list,HTTP.UTF_8);//设置编码，防止中午乱码
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			if(!result.equals("null")){
				JSONArray jsonarray = new JSONArray(result);
				courses = new ArrayList<>();
				for(int i=0;i<jsonarray.length();i++){
					JSONObject json = jsonarray.getJSONObject(i);
					CourseInf course = new CourseInf();
					course.setId(json.getInt("id"));
					course.setCourseid(json.getInt("courseid"));
					course.setTeacherid(json.getInt("teacherid"));
					course.setCoursename(json.getString("coursename"));
					course.setCourseintroduction(json.getString("courseintroduction"));
					course.setCoursedegree(json.getDouble("coursedegree"));
					course.setCoursecomments(json.getString("coursecomments"));
					course.setCatalogue(json.getString("catalogue"));
					course.setAndroidimage(json.getString("androidimage"));
					course.setWatchernum(json.getInt("watchernum"));
					courses.add(course);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}
	
}
