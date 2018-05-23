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
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理查询课程关键字的servlet
@WebServlet("/searchCourse")
@SuppressWarnings("all")
public class searchCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 课程DAO对象
	private ICourseDAO courseDAO = new CourseDAOImpl();
	// 课程细节对象
	private ICourseDetailDAO courseDetailDAO = new CourseDetailDAOImpl();
	// 评论对象
	private ICommentDAO commentDAO = new CommentDAOImpl();
	// 用户对象
	private IUserDAO userDAO = new UserDAOImpl();

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码为utf-8
		req.setCharacterEncoding("utf-8");

		// cmd参数为该课程信息的关键字
		String keyWord = req.getParameter("cmd");
		if (stringutil.haslength(keyWord)) {
			// 关键字查询对象，将传递的关键字参数设置到查询对象中
			courseQueryObject courseQo = new courseQueryObject();
			courseQo.setKeyword(keyWord);
			List<Course> courseList = courseDAO.query(courseQo);

			if (courseList.size() > 0) {
				// 创建响应json对象，将jsonArray对象塞入其中
				JSONObject respJsob = new JSONObject();

				// jsonArray对象，将课程信息json对象放入其中
				JSONArray jsar = new JSONArray();
				for (Course course : courseList) {
					// 将所有课程信息封装到json对象中
					JSONObject courseJsob = new JSONObject();
					courseJsob.put("name", course.getName());
					courseJsob.put("courseId", course.getCourseId() + "");
					courseJsob.put("teacher", course.getTeacher());

					// 将封装好信息的json对象加入jsonArray对象中
					jsar.add(courseJsob);
				}
				
				respJsob.put("data", jsar);
				
				//将响应对象响应到浏览器
				jsonutil.putJsonObject(respJsob, resp);
			}
		}
	}
}
