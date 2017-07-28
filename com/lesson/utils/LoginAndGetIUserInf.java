package com.lesson.utils;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lesson.bean.GOLBALVALUE;
import com.lesson.net.NetConnection;

public class LoginAndGetIUserInf {
	
	public static int login(String username,String password){
		String url = NetConnection.url+"LoginServlet?username="+username+"&password="+password+"&device=phone";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			String result = EntityUtils.toString(response.getEntity());
			JSONObject jsonobj = new JSONObject(result);
			if(jsonobj.getString("result").equals("success")){
				GOLBALVALUE.user.setBirthday(jsonobj.getString("birthday"));
				GOLBALVALUE.user.setEmail(jsonobj.getString("email"));
				GOLBALVALUE.user.setIntroduction(jsonobj.getString("introduction"));
				if(jsonobj.getString("nickname") == null){
					GOLBALVALUE.user.setNickname(jsonobj.getString("email"));
				}else{
					GOLBALVALUE.user.setNickname(jsonobj.getString("nickname"));
				}
				GOLBALVALUE.user.setSchool(jsonobj.getString("school"));
				GOLBALVALUE.user.setSex(Integer.parseInt(jsonobj.getString("sex")));
				GOLBALVALUE.user.setUsername(jsonobj.getString("username"));
				GOLBALVALUE.user.setEmail(jsonobj.getString("email"));
				GOLBALVALUE.user.setHeadimagePath(jsonobj.getString("headimage").toString().trim());
				return 1;
			}else if(result.equals("username_not_exist")){
				return 2;
			}else if(result.equals("password_error")){
				return 3;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 0;
		
	}
	
}
