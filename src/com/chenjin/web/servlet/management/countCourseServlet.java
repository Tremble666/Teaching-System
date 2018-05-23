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
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;

//统计没课课程分类下的课程数量
@WebServlet("/countCourse")
@SuppressWarnings("all")
public class countCourseServlet extends HttpServlet {
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
		if ("countTimes".equals(cmd)) {
			try {
				this.countTimes(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.countCategory(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 返回课程下的
	protected void countTimes(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		System.out.println("aaa");
		JSONArray dataJsar = new JSONArray();

		List<Course> courseList = courseDAO.list();
		for (int i = 0; i < courseList.size(); i++) {
			Long courseId = courseList.get(i).getCourseId();

			JSONObject courseJsob = new JSONObject();

			// 查询该课程的课程数量
			courseDetailQueryObject cdQo = new courseDetailQueryObject();
			cdQo.setCourseId(courseId);
			List<CourseDetail> barList = courseDetailDAO.query(cdQo);

			// 因为当前的courseDetail是按照barId从小到大排列的，因此可能导致章节是无序的，因此此处根据
			// 链表原理使其按照章节有序排列
			List<CourseDetail> tempDetailList = new ArrayList<>();
			// 首先找到第一个
			cdQo = new courseDetailQueryObject();
			cdQo.setParentId(0l);
			cdQo.setCourseId(Long.valueOf(courseId));
			List<CourseDetail> cdList = courseDetailDAO.query(cdQo);
			
			tempDetailList.add(cdList.get(0));
			Long searchId = cdList.get(0).getBarId();
			while (searchId != null) {
				cdQo = new courseDetailQueryObject();
				cdQo.setParentId(searchId);
				List<CourseDetail> tempList = courseDetailDAO.query(cdQo);
				if (tempList.size() > 0) {
					tempDetailList.add(tempList.get(0));
					searchId = tempList.get(0).getBarId();
				} else {
					searchId = null;
				}
			}
			
			courseJsob.put("courseName",courseList.get(i).getName());
			
			//统计该课程的所有小节点击总次数
			cdQo = new courseDetailQueryObject();
			cdQo.setCourseId(courseList.get(i).getCourseId());
			List<CourseDetail> currentDetailList = courseDetailDAO.query(cdQo);
			int totalTimes = 0;
			for (CourseDetail cd : currentDetailList) {
				totalTimes+=cd.getTimes();
			}
			courseJsob.put("courseTimes", totalTimes);
			

			// 查询小节的点击次数
			JSONArray barJsar = new JSONArray();
			for (int j = 0; j < tempDetailList.size(); j++) {
				JSONObject barJsob = new JSONObject();
				barJsob.put("barName", tempDetailList.get(j).getBarName());
				barJsob.put("times", tempDetailList.get(j).getTimes());
				barJsar.add(barJsob);
			}
			courseJsob.put("barList", barJsar);
			dataJsar.add(courseJsob);
		}

		// 创建响应json对象
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", dataJsar);
		jsonutil.putJsonObject(respJsob, resp);

	}

	// 返回课程分类课程数量统计
	protected void countCategory(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		JSONArray countJsar = new JSONArray();

		List<Category> cgList = categoryDAO.list();
		for (int i = 0; i < cgList.size(); i++) {
			Long categoryId = cgList.get(i).getCategoryId();

			courseQueryObject courseQo = new courseQueryObject();
			courseQo.setCategoryId(categoryId);
			List<Course> courseList = courseDAO.query(courseQo);

			JSONObject cgJsob = new JSONObject();
			cgJsob.put("categoryName", cgList.get(i).getCategoryName());
			cgJsob.put("categoryNumber", courseList.size());

			countJsar.add(cgJsob);
		}
		// 响应json对象
		JSONObject respJsob = new JSONObject();
		respJsob.put("data", countJsar);
		jsonutil.putJsonObject(respJsob, resp);
	}
}
