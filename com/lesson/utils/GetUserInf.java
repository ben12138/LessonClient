package com.lesson.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.lesson.bean.GOLBALVALUE;
import com.lesson.bean.Teacher;
import com.lesson.bean.UserInf;
import com.lesson.net.NetConnection;

public class GetUserInf {
	public static void getTeacher(CourseInf course){
		String urlString = NetConnection.url+"GetTeacherInfServlet?id="+GOLBALVALUE.course.getTeacherid();
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlString);
		try {
			HttpResponse response;
			response = client.execute(get);
			String result = EntityUtils.toString(response.getEntity());
			if (!result.equals("null")) {
				Teacher teacher = new Teacher();
				JSONObject json = new JSONObject(result);
				teacher.setTeacherimage(json.getString("teacherimage"));
				teacher.setTeacherintroduction(json.getString("teacherintroduction"));
				teacher.setTeachername(json.getString("teachername"));
				GOLBALVALUE.teacherInf = teacher;
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
	
	public static boolean submitUser(UserInf user){
		String result = "";
		String url = NetConnection.url+"UpDateUserInfServlet";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		   
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("nickname", user.getNickname()));
		list.add(new BasicNameValuePair("sex", user.getSex()+""));
		list.add(new BasicNameValuePair("birthday", GOLBALVALUE.user.getBirthday()));
		list.add(new BasicNameValuePair("school", user.getSchool()));
		list.add(new BasicNameValuePair("introduction", user.getIntroduction()));
		list.add(new BasicNameValuePair("email", user.getEmail()));
		
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list,HTTP.UTF_8);//设置编码，防止中午乱码
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
		if(result.equals("success")){
			GOLBALVALUE.user = user;
			return true;
		}else{
			return false;
		}
	}
	
	public static void upLoadImage(File imageFile){
		
	}
	
}
