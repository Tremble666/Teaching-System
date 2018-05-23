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

//处理主页请求的servlet
@WebServlet("/courseDetail")
@SuppressWarnings("all")
public class courseDetailServlet extends HttpServlet {
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

		// cmd参数为该课程的Id
		String cmd = req.getParameter("cmd");
		if (stringutil.haslength(cmd)) {
			this.handleCourse(req, resp, cmd);
		} else {
			this.list(req, resp);
		}
	}

	// 处理点击具体某一课程时发送的请求，返回的信息是该课程更加具体的信息
	protected void handleCourse(HttpServletRequest req,
			HttpServletResponse resp, String courseId) throws ServletException,
			IOException {
		// 创建响应对象
		JSONObject respJsob = new JSONObject();

		// 根据传递的主键查询出哪一个课程
		courseQueryObject qo = new courseQueryObject();
		qo.setCourseId(Long.valueOf(courseId));
		List<Course> list = courseDAO.query(qo);

		// 课程介绍和课程名称
		String courseIntroduction = "";
		String courseName = "";

		// list.get(0)是根据courseName进行查询后获取的课程对象
		if (list.size() > 0) {
			// 课程介绍
			courseIntroduction = list.get(0).getIntroduction();
			respJsob.put("name", list.get(0).getName());
			// 课程名称
			courseName = list.get(0).getName();
			respJsob.put("introduction", list.get(0).getIntroduction());
		}

		// jsonArray对象，封装小节相关信息
		JSONArray barJsar = new JSONArray();

		// jsonArray对象，封装评论相关信息
		JSONArray commentJsar = new JSONArray();

		// 根据courseId查询出该课程的小节相关信息
		courseDetailQueryObject cdqo = new courseDetailQueryObject();
		cdqo.setCourseId(Long.valueOf(courseId));
		List<CourseDetail> courseDetaiList = courseDetailDAO.query(cdqo);

		// 因为当前的courseDetail是按照barId从小到大排列的，因此可能导致章节是无序的，因此此处根据
		// 链表原理使其按照章节有序排列
		List<CourseDetail> tempDetailList = new ArrayList<>();
		// 首先找到第一个
		cdqo = new courseDetailQueryObject();
		cdqo.setParentId(0l);
		cdqo.setCourseId(Long.valueOf(courseId));
		List<CourseDetail> cdList = courseDetailDAO.query(cdqo);
		tempDetailList.add(cdList.get(0));
		Long searchId = cdList.get(0).getBarId();
		while (searchId != null) {
			cdqo = new courseDetailQueryObject();
			cdqo.setParentId(searchId);
			List<CourseDetail> tempList = courseDetailDAO.query(cdqo);
			if (tempList.size() > 0) {
				tempDetailList.add(tempList.get(0));
				searchId = tempList.get(0).getBarId();
			} else {
				searchId = null;
			}
		}

		/*System.out.println("-------------------");
		for (CourseDetail cd : tempDetailList) {
			System.out.println(cd);
		}
		System.out.println("-------------------");*/

		for (CourseDetail cd : tempDetailList) {
			// 创建bar的信息json对象，将该对象加入barJsar中
			JSONObject barJsob = new JSONObject();
			barJsob.put("barName", cd.getBarName());
			barJsob.put("barId", cd.getBarId() + "");
			barJsar.add(barJsob);

			// 获取该小结的id，与评论表进行关联
			commentQueryObject commentQo = new commentQueryObject();
			commentQo.setBarId(cd.getBarId());
			List<Comment> commentList = commentDAO.query(commentQo);
			for (Comment comment : commentList) {
				// 创建评论信息的json对象
				JSONObject commentJsob = new JSONObject();

				// 为评论信息的json对象添加内容
				User user = userDAO.get(comment.getUserId());
				commentJsob.put("name", user.getName());
				commentJsob.put("commentContent", comment.getCommentContent());
				commentJsob.put("commentTime", comment.getCommentTime());

				// 根据barId,找出具体的小结，确定该评论在哪一个小节中
				commentJsob.put("barName", cd.getBarName());

				// 将评论的json对象加入jsonArray对象中
				commentJsar.add(commentJsob);

				// System.out.println(comment);
			}

		}

		// 将barJsar添加到响应的json对象中
		respJsob.put("outline", barJsar);

		respJsob.put("teacher", courseDAO.get(Long.valueOf(courseId))
				.getTeacher());

		// 将commentJsar添加到响应的json对象中
		respJsob.put("comment", commentJsar);

		// 将respJsob响应给浏览器
		jsonutil.putJsonObject(respJsob, resp);
	}

	// 目前没用
	protected void list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}
}
