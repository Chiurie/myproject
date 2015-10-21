package com.wx.connect;

import java.util.List;

import com.google.gson.Gson;
import com.wx.httpclient.utils.HttpClient;
import com.wx.info.WXAppConfig;
import com.wx.model.OpenIDList;
import com.wx.model.Token;
import com.wx.model.UserInfo;

public class WXConnect {

	
	private WXConnect(){
	}
	
	public static String getLatestToken(){
		String param = "grant_type=client_credential&appid="+WXAppConfig.APPID+"&secret="+WXAppConfig.APPSECRET;
		String returnStr  = HttpClient.sendGet(WXAppConfig.TOKEN_URL, param); 
		Gson gson  = new Gson();
    	Token token = gson.fromJson(returnStr, Token.class);
		return token.getAccess_token();
	}
	
	public static List<String> getUserOpenIdList(String token){
		String param = "access_token="+token;
    	String returnStr = HttpClient.sendGet(WXAppConfig.GET_OPENID_URL, param);
    	Gson gson  = new Gson();
    	OpenIDList openIdList  = gson.fromJson(returnStr, OpenIDList.class);    	
		return openIdList.getData().getOpenid();
	} 
	
	public static UserInfo getUserInfo(String token,String openId){
		String param = "access_token="+token+"&openid="+openId+"&lang=zh_CN";
		String returnStr = HttpClient.sendGet(WXAppConfig.USER_INFO_URL, param);
		Gson gson  = new Gson();
		UserInfo userInfo = gson.fromJson(returnStr, UserInfo.class);    	
		return userInfo;
	}
	
}
