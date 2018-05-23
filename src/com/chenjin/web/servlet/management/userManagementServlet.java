package com.chenjin.web.servlet.management;

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
import com.chenjin.smis.DAO.Impl.comment.CommentDAOImpl;
import com.chenjin.smis.DAO.Impl.comment.ICommentDAO;
import com.chenjin.smis.DAO.Impl.course.CourseDAOImpl;
import com.chenjin.smis.DAO.Impl.course.ICourseDAO;
import com.chenjin.smis.DAO.Impl.courseDetail.CourseDetailDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.ICourseDetailDAO;
import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.DAO.Impl.userCourse.IUserCourseDAO;
import com.chenjin.smis.DAO.Impl.userCourse.userCourseDAOImpl;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.util.jsonutil;

//处理系统管理员的操作
@WebServlet("/userManagement")
@SuppressWarnings("all")
public class userManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 课程DAO对象
	private ICourseDAO courseDAO = new CourseDAOImpl();
	// 课程细节对象
	private ICourseDetailDAO courseDetailDAO = new CourseDetailDAOImpl();
	// 评论对象
	private ICommentDAO commentDAO = new CommentDAOImpl();
	// 用户对象
	private IUserDAO userDAO = new UserDAOImpl();
	// 用户课程对象
	private IUserCourseDAO userCourseDAO = new userCourseDAOImpl();

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码为utf-8
		req.setCharacterEncoding("utf-8");

		// 接受请求参数，封装成对象
		String cmd = req.getParameter("operation");
		if ("freeze".equals(cmd)) {
			try {
				this.freeze(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("update".equals(cmd)) {
			try {
				this.update(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(cmd)) {
			try {
				this.delete(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.list(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 处理冻结用户
	protected void freeze(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		JSONArray emailArray = jsob.getJSONArray("emailList");
		for (int i = 0; i < emailArray.size(); i++) {
			UserQueryObject userQo = new UserQueryObject();
			userQo.setEmail(emailArray.getString(i));
			List<User> user = userDAO.query(userQo);
			user.get(0).setAuthority(0);
			userDAO.update(user.get(0));
		}

	}

	// 处理修改
	protected void update(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		JSONArray emailArray = jsob.getJSONArray("emailList");
		for (int i = 0; i < emailArray.size(); i++) {
			UserQueryObject userQo = new UserQueryObject();
			userQo.setEmail(emailArray.getString(i));
			List<User> user = userDAO.query(userQo);
			user.get(0).setAuthority(
					Integer.valueOf(jsob.getString("authority")));
			userDAO.update(user.get(0));
		}
	}

	// 处理删除
	protected void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		System.out.println(jsob);
		JSONArray emailArray = jsob.getJSONArray("emailList");
		for (int i = 0; i < emailArray.size(); i++) {
			UserQueryObject userQo = new UserQueryObject();
			userQo.setEmail(emailArray.getString(i));
			List<User> user = userDAO.query(userQo);
			userDAO.delete(user.get(0).getUserId());
		}
	}

	// 返回所有用户
	protected void list(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONArray userJsar = new JSONArray();
		List<User> userList = userDAO.list();
		for (int i = 0; i < userList.size(); i++) {
			JSONObject userJsob =  new JSONObject();
			userJsob.put("name", userList.get(i).getName());
			userJsob.put("email", userList.get(i).getEmail());
			userJsob.put("authority", userList.get(i).getAuthority());
			userJsar.add(userJsob);
		}
		
		//响应
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", userJsar);
		jsonutil.putJsonObject(respJsob, resp);
	}
}
