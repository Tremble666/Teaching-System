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
import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.util.jsonutil;

//课程管理员进行课程增删改的操作的servlet
@WebServlet("/commentManagement")
@SuppressWarnings("all")
public class commentManagementServlet extends HttpServlet {
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
		String cmd = req.getParameter("cmd");
		if ("delete".equals(cmd)) {
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

	// 处理删除评论
	protected void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		
		JSONArray emailArray = jsob.getJSONArray("emailList");
		for (int i = 0; i < emailArray.size(); i++) {
			//拿到该email对应的userId
			String email = emailArray.getString(i);
			UserQueryObject userQo = new UserQueryObject();
			userQo.setEmail(email);
			List<User> userList = userDAO.query(userQo);
			Long userId = userList.get(0).getUserId();
			
			//根据userId删除评论表中该用户发表的评论
			commentQueryObject commentQo =new commentQueryObject();
			commentQo.setUserId(userId);
			List<Comment> commentList = commentDAO.query(commentQo);
			for (int j = 0; j < commentList.size(); j++) {
				Long commentId = commentList.get(j).getCommentId();
				commentDAO.delete(commentId);
			}
		}
		//resp.setHeader("Access-Control-Allow-Origin", "*");
	}

	// 返回所有评论
	protected void list(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// 评论的jsonArray
		JSONArray commentJsar = new JSONArray();

		List<Comment> commentList = commentDAO.list();
		for (int i = 0; i < commentList.size(); i++) {
			// 评论的jsob
			JSONObject commentJsob = new JSONObject();

			Long userId = commentList.get(i).getUserId();
			User user = userDAO.get(userId);
			commentJsob.put("name", user.getName());
			commentJsob.put("email", user.getEmail());
			
			commentJsob.put("commentContent", commentList.get(i).getCommentContent());
			commentJsar.add(commentJsob);
		}
		
		//创建响应的json对象
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", commentJsar);
		jsonutil.putJsonObject(respJsob, resp);
	}
}
