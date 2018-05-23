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

//处理用户与评论关系的servlet
@WebServlet("/userComment")
@SuppressWarnings("all")
public class userCommentServlet extends HttpServlet {
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
		
		//jsonArray对象，用于存取多条评论信息
		JSONArray commentJsar = new JSONArray();
		
		//根据用户的id在评论表中查出该用户所有的评论信息
		commentQueryObject commentQo = new commentQueryObject();
		commentQo.setUserId(userId);
		List<Comment> commentList = commentDAO.query(commentQo);
		for (Comment comment : commentList) {
			//json对象，用以存取每条评论信息
			JSONObject commentJsob = new JSONObject();
			
			commentJsob.put("commentContent", comment.getCommentContent());
			commentJsob.put("commentTime", comment.getCommentTime());
			
			//获取barId,在进行关联表，查询出课程名与小节名
			Long barId = comment.getBarId();
			
			//获取课程名
			courseDetailQueryObject courseDetailQo = new courseDetailQueryObject();
			courseDetailQo.setBarId(barId);
			List<CourseDetail> courseDetailList = courseDetailDAO.query(courseDetailQo);
			Long courseId = courseDetailList.get(0).getCourseId();
			String courseName = courseDAO.get(courseId).getName();
			commentJsob.put("courseName", courseName);
			
			//获取小节名
			String barName = courseDetailDAO.get(barId).getBarName();
			commentJsob.put("barName", barName);
			
			//将commentJsob对象添加到jsonArray对象中
			commentJsar.add(commentJsob);
		}
		
		//创建响应json对象，将信息响应给浏览器
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", commentJsar);
		jsonutil.putJsonObject(respJsob, resp);
	}
}
