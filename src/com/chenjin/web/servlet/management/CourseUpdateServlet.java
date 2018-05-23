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
import com.chenjin.smis.DAO.Impl.courseCategory.ICourseCategoryDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.courseCategoryDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.CourseDetailDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.ICourseDetailDAO;
import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.DAO.Impl.userCourse.IUserCourseDAO;
import com.chenjin.smis.DAO.Impl.userCourse.userCourseDAOImpl;
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.categoryQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.fileutil;
import com.chenjin.smis.util.jsonutil;

//课程管理员进行课程增删改的操作的servlet
@WebServlet("/courseUpdate")
@SuppressWarnings("all")
public class CourseUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 课程分类DAO对象
	private ICourseCategoryDAO categoryDAO = new courseCategoryDAOImpl();
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
		if ("save".equals(cmd)) {
			try {
				this.save(req, resp);
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
		}
	}

	// 处理新增表单
	protected void save(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		Course course = (Course) jsob.toBean(jsob, Course.class);
		
		String categoryName = categoryDAO.get(course.getCategoryId()).getCategoryName();
		
		courseDAO.save(course);
		
		//课程分类查询对象
		categoryQueryObject cqo = new categoryQueryObject();
		cqo.setCategoryName(categoryName);
		List<Category> cList = categoryDAO.query(cqo);
		
		//课程对象
		courseQueryObject courseQo = new courseQueryObject();
		courseQo.setName(course.getName());
		List<Course> courseList = courseDAO.query(courseQo);
		
		// 新增分类是增加一个文件夹
		fileutil.mkDirectory(req.getRealPath("/upload/pdf/" + cList.get(0).getCategoryId()+"/"+courseList.get(0).getCourseId()));
		fileutil.mkDirectory(req.getRealPath("/upload/video/" + cList.get(0).getCategoryId()+"/"+courseList.get(0).getCourseId()));

	}

	// 处理修改表单
	protected void update(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		Long courseId = Long.valueOf((String) jsob.get("courseId"));

		// 根据传递来的json数据的courseId,找出需要修改的那个课程
		Course course = courseDAO.get(courseId);

		// 根据传递来的新的课程信息对该课程进行修改
		course.setName((String) jsob.get("name"));
		course.setIntroduction((String) jsob.getString("introduction"));
		course.setTeacher((String) jsob.getString("teacher"));
		// 更新信息
		courseDAO.update(course);
	}

	// 处理删除
	protected void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// 拿到需要删除的courseId
		String courseId = req.getParameter("courseId");
		courseDAO.delete(Long.valueOf(courseId));
		System.out.println("删除该课程成功");
	}
}
