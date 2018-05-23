package com.chenjin.web.servlet.videoAndcomment;

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
import com.chenjin.smis.DAO.Impl.courseCategory.ICourseCategoryDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.courseCategoryDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.CourseDetailDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.ICourseDetailDAO;
import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.DAO.Impl.userCourse.IUserCourseDAO;
import com.chenjin.smis.DAO.Impl.userCourse.userCourseDAOImpl;
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.domain.UserCourse;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理用户添加课程的servlet
@WebServlet("/addCourse")
@SuppressWarnings("all")
public class addCourseServlet extends HttpServlet {
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

		try {
			JSONObject reqJsob = jsonutil.getJsonObject(req);
			String barId = reqJsob.getString("barId");
			String userName = reqJsob.getString("userName");
			
			//通过请求参数拿到courseId和userId
			Long courseId = courseDAO.get(courseDetailDAO.get(Long.valueOf(barId)).getCourseId()).getCourseId();
			UserQueryObject userQo = new UserQueryObject();
			userQo.setName(userName);
			List<User> userList = userDAO.query(userQo);
			Long userId = userList.get(0).getUserId();
			
			//创建响应对象
			JSONObject respJsob = new JSONObject();
			
			//判断数据库是否已经有该用户的选课信息
			UserCourse uc = userCourseDAO.get(userId, courseId);
			if(uc == null){
				//如果没有
				userCourseDAO.save(userId, courseId);
				respJsob.put("state", "true");
			}else{
				respJsob.put("state", "false");
			}
			
			//将响应json对象响应至浏览器
			jsonutil.putJsonObject(respJsob, resp);
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
}
