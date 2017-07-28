package com.lesson.utils;

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
import com.lesson.net.NetConnection;

public class GetComments {
	
	public static boolean praise(int id){
		String result = "";
		String url = NetConnection.url+"GetAllCommentsServlet?type=praise&id="+id;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("success") ){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean addComment(Comments comment){
		String result = "";
		String url = NetConnection.url+"AddAllCommentsServlet";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("sendernickname", comment.getSenderNickName()));
		list.add(new BasicNameValuePair("senderheadimage", comment.getSenderheadImage()));
		list.add(new BasicNameValuePair("content", comment.getContent()));
		list.add(new BasicNameValuePair("sender", GOLBALVALUE.user.getUsername()));
		list.add(new BasicNameValuePair("courseinfid", comment.getId()+""));
		HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
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
		if (result.trim().equals("failure")) {
			return false;
		} else {
			return true;
		}
	}
	
}
