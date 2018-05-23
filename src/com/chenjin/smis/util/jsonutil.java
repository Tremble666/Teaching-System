package com.chenjin.smis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class jsonutil {
	// 接收请求数据，将数据封装成json对象
	public static JSONObject getJsonObject(HttpServletRequest req) throws Exception {
		// 接受请求数据（字符串格式）
		StringBuffer jsonData = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = req.getReader();
		} catch (IOException e) {
			System.out.println("获取reader出错！");
		}
		String line = null;
		
		try {
			while ((line = reader.readLine()) != null) {
				jsonData.append(line);
			}
		} catch (IOException e) {
			System.out.println("读取行出错！");
		}finally{
			reader.close();	
		}
		// 将字符串转为json对象
		JSONObject json = JSONObject.fromObject(jsonData.toString());
		
		return json;
	}

	// 参数为一个jsonObject对象，浏览器输出该对象
	public static void putJsonObject(JSONObject jsob, HttpServletResponse resp) {
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
			out.write(jsob.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
