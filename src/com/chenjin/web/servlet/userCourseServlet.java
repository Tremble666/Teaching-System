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
import com.chenjin.smis.query.UserCourseQueryObject;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理用户与课程关系的servlet
@WebServlet("/userCourse")
@SuppressWarnings("all")
public class userCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 课程DAO对象
	private ICourseDAO courseDAO = new CourseDAOImpl();
	// 课程细节对象
	private ICourseDetailDAO courseDetailDAO = new CourseDetailDAOImpl();
	// 评论对象
	private ICommentDAO commentDAO = new CommentDAOImpl();
	// 用户对象
	private IUserDAO userDAO = new UserDAOImpl();
	//用户课程对象
	private IUserCourseDAO userCourseDAO = new userCourseDAOImpl();

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码为utf-8
		req.setCharacterEncoding("utf-8");
		
		//接受请求参数，请求参数为userName
		String userName = req.getParameter("cmd");
		
		//根据用户名查询出该用户的id
		UserQueryObject userQo = new UserQueryObject();
		userQo.setName(userName);
		List<User> user = userDAO.query(userQo);
		Long userId = user.get(0).getUserId();
		
		//课程信息的jsonArray对象
		JSONArray courseJsar = new JSONArray();
		
		//根据userId在用户课程信息表找出该用户所选课的信息
		UserCourseQueryObject userCourseQo = new UserCourseQueryObject();
		userCourseQo.setUserId(userId);
		List<UserCourse> userCourseList = userCourseDAO.query(userCourseQo);
		for (UserCourse uc : userCourseList) {
			//创建课程信息的json对象
			JSONObject courseJsob = new JSONObject();
			
			//查询出课程Id,并根据该id找到对应课程
			Long courseId = uc.getCourseId();
			Course course = courseDAO.get(courseId);
			
			//将查询出的课程信息加入课程的jsonD对象中
			courseJsob.put("name", course.getName());
			courseJsob.put("courseId", course.getCourseId()+"");
			courseJsob.put("teacher", course.getTeacher());
			
			//将课程的json对象加到jsonArray对象中
			courseJsar.add(courseJsob);
		}
		
		//创建响应对象，将课程信息响应给浏览器
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", courseJsar);
		jsonutil.putJsonObject(respJsob, resp);
	}
}
