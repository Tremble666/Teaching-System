package com.chenjin.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.druid.support.json.JSONUtils;
import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.util.jsonutil;

//处理主页请求的servlet
@WebServlet("/course")
@SuppressWarnings("all")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IUserDAO userDAO = new UserDAOImpl();

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码为utf-8
		req.setCharacterEncoding("utf-8");
		
		
		// 接受请求参数，封装成对象
		String cmd = req.getParameter("cmd");
		if ("regist".equals(cmd)) {
			try {
				this.regist(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("signup".equals(cmd)) {
			try {
				this.signup(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.list(req, resp);
		}
	}

	// 处理跳转至主页
	protected void list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 控制界面跳转
		req.getRequestDispatcher("/WEB-INF/views/dist/index.jsp").forward(req,
				resp);
	}

	// 处理注册请求
	protected void regist(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		//接受注册信息，将json格式的数据转化为对象，保存至数据库
		JSONObject jt = jsonutil.getJsonObject(req);
		User user = (User) jt.toBean(jt, User.class);
		
		//对注册的用户信息进行检测
		UserQueryObject uo = new UserQueryObject();
		uo.setName(user.getName());
		List<User> list = userDAO.query(uo);
		if(!(list.size() == 0)){
			uo = new UserQueryObject();
			//如果根据用户注册的姓名在数据库进行匹配，如果数据库中有同名的，则不能保存用户信息
			JSONObject jsob = new JSONObject();
			jsob.put("state", "false");
			jsob.put("errorReason", "name");
			jsonutil.putJsonObject(jsob, resp);
			System.out.println(user.getName()+"---------------------注册失败");
			System.out.println("错误信息已成功反馈");
			
		}else{
			UserQueryObject uoByEmail = new UserQueryObject();
			uoByEmail.setEmail(user.getEmail());
			List<User> listByEmail = userDAO.query(uoByEmail);
			if(!(listByEmail.size() == 0)){
				//如果用户注册的email是重复的
				JSONObject jsob = new JSONObject();
				jsob.put("state", "false");
				jsob.put("errorReason", "email");
				jsonutil.putJsonObject(jsob, resp);
				System.out.println(user.getName()+"---------------------注册失败");
				System.out.println("错误信息已成功反馈");
			}else{
				//如果姓名没有同名且email没有重复，则将用户信息保存至数据库	
				user.setAuthority(1);
				userDAO.save(user);
				System.out.println(user.getName()+"---------------------注册成功");
				
				
				//反馈成功信息给浏览器，数据为json格式
				JSONObject jsob = new JSONObject();
				jsob.put("state", true);
				jsob.put("name", user.getName());
				jsob.put("email", user.getEmail());
				jsonutil.putJsonObject(jsob, resp);
				System.out.println("信息已成功反馈");
			}
		}
	}

	// 处理登录请求
	protected void signup(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		//接受注册信息，将json格式的数据转化为对象，保存至数据库
		JSONObject jt = jsonutil.getJsonObject(req);
		User user = (User) jt.toBean(jt, User.class);
		System.out.println(user);
		
		//对登录的用户信息进行检测
		UserQueryObject uo = new UserQueryObject();
		uo.setEmail(user.getEmail());
		uo.setPassword(user.getPassword());
		List<User> listByEmailAndPassword = userDAO.query(uo);
		if(listByEmailAndPassword.size()>0){
			//如果用户的两个信息均存在与数据库且信息匹配,将成功信息反馈给浏览器
			JSONObject jsob = new JSONObject();
			jsob.put("isLogin", true);
			jsob.put("userName", listByEmailAndPassword.get(0).getName());
			jsob.put("userEmail", listByEmailAndPassword.get(0).getEmail());
			jsob.put("authority", listByEmailAndPassword.get(0).getAuthority());
			jsonutil.putJsonObject(jsob, resp);
		}else{
			JSONObject jsob = new JSONObject();
			jsob.put("isLogin", false);
			jsonutil.putJsonObject(jsob, resp);
		}	
	}
}
