package com.wx.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wx.connect.WXConnect;
import com.wx.httpclient.utils.ExcelUtils;
import com.wx.model.UserInfo;

@RequestMapping("/user")
@Controller
public class GetUserInfoController {
	
	@RequestMapping("/userInfo")
	public String getUserInfo(HttpServletResponse response){
		String token = WXConnect.getLatestToken();
		List<String> openIds  = WXConnect.getUserOpenIdList(token);
		List<Map<String,Object>> userInfoList = new ArrayList<Map<String,Object>>();
		for (String openId : openIds) {
			UserInfo userInfo = WXConnect.getUserInfo(token, openId);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("city", userInfo.getCity());
			map.put("country", userInfo.getCountry());
			map.put("groupid", userInfo.getGroupid());
			map.put("headimgurl", userInfo.getHeadimgurl());
			map.put("language", userInfo.getLanguage());
			map.put("nickname", userInfo.getNickname());
			map.put("openid", userInfo.getOpenid());
			map.put("province", userInfo.getProvince());
			map.put("remark", userInfo.getRemark());
			map.put("subscribe_time", userInfo.getSubscribe_time());
			map.put("sex", userInfo.getSex());
			map.put("subscribe", userInfo.getSubscribe());
			map.put("unionid", userInfo.getUnionid());
			userInfoList.add(map);
		}
		
		try {
			HSSFWorkbook wb = ExcelUtils.exportExcel("应收款详情", userInfoList
						, new String[] { "昵称", "OPENID",
						"性别", "国家", "省份", "城市", "是否订阅" , "头像" , "UNIONID" ,"备注","分组ID" }, new String[] {
						"nickname", "openid", "sex", "country",
						"province", "city","subscribe","headimgurl","unionid","remark","groupid" });
		
			this.export(wb, "mdWXUserList.xls", response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/getUser")
	public String getUser(){
		return "userInfo";
	}

	protected void export(HSSFWorkbook wb, String fileName,HttpServletResponse response) throws IOException {
		//HttpServletResponse response = response();
		// 设置response的编码方式
		response.setContentType("application/x-msdownload");

		// 写明要下载的文件的大小
		// response.setContentLength((int)fileName.length());

		// 设置附加文件名
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);

		// 解决中文乱码
		// response.setHeader("Content-Disposition","attachment;filename="+new
		// String
		// (filename.getBytes("gbk"),"iso-8859-1"));
		OutputStream output = response.getOutputStream();
		wb.write(output);
		output.flush();
		output.close();

	}
	
}
