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
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理请求小节视屏的servlet
@WebServlet("/courseBar")
@SuppressWarnings("all")
public class courseBarServlet extends HttpServlet {
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

		// 创建响应json对象
		JSONObject respJsob = new JSONObject();

		// cmd参数为该课程分类的Id
		String cmd = req.getParameter("cmd");

		// cmd参数为barId,根据barId获取相关信息
		CourseDetail cd = courseDetailDAO.get(Long.valueOf(cmd));

		// 每次访问该视屏页面，该小节的times需要加一
		int times = cd.getTimes();
		cd.setTimes(times + 1);
		courseDetailDAO.update(cd);

		respJsob.put("path", cd.getPath());
		respJsob.put("barName", cd.getBarName());

		Course course = courseDAO.get(cd.getCourseId());
		respJsob.put("courseName", course.getName());

		// 创建一个jsonArray对象存取课程大纲相关的信息
		JSONArray outLineJsar = new JSONArray();

		courseDetailQueryObject cdQo = new courseDetailQueryObject();
		cdQo.setCourseId(cd.getCourseId());
		List<CourseDetail> cdList = courseDetailDAO.query(cdQo);

		
		// 因为当前的courseDetail是按照barId从小到大排列的，因此可能导致章节是无序的，因此此处根据
		// 链表原理使其按照章节有序排列
		List<CourseDetail> tempDetailList = new ArrayList<>();
		// 首先找到第一个
		cdQo = new courseDetailQueryObject();
		cdQo.setParentId(0l);
		cdQo.setCourseId(cd.getCourseId());
		List<CourseDetail> cddList = courseDetailDAO.query(cdQo);
		tempDetailList.add(cddList.get(0));
		Long searchId = cddList.get(0).getBarId();
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

		
		for (int i = 0; i < tempDetailList.size(); i++) {
			JSONObject barJsob = new JSONObject();

			barJsob.put("barName", tempDetailList.get(i).getBarName());
			barJsob.put("barId", tempDetailList.get(i).getBarId());

			outLineJsar.add(barJsob);
		}
		respJsob.put("outline", outLineJsar);

		// 存取评论相关信息的jsonArray对象
		JSONArray commentJsar = new JSONArray();

		commentQueryObject commentQo = new commentQueryObject();
		commentQo.setBarId(cd.getBarId());
		List<Comment> commentList = commentDAO.query(commentQo);
		for (int i = 0; i < commentList.size(); i++) {
			JSONObject commentJsob = new JSONObject();
			if(commentList.get(i).getParentId() != null){
				continue;
			}
			commentJsob.put("commentId", commentList.get(i).getCommentId());

			// 根据userId查找是哪个用户评论的
			String userName = userDAO.get(commentList.get(i).getUserId())
					.getName();
			commentJsob.put("name", userName);

			commentJsob.put("commentContent", commentList.get(i)
					.getCommentContent());
			commentJsob.put("commentTime", commentList.get(i).getCommentTime());

			// 回复评论的jsar
			JSONArray responseJsar = new JSONArray();

			commentQo = new commentQueryObject();
			commentQo.setParentId(commentList.get(i).getCommentId());
			commentQo.setBarId(cd.getBarId());
			List<Comment> responseList = commentDAO.query(commentQo);
			for (Comment com : responseList) {
				JSONObject responseJsob = new JSONObject();

				String resUserName = userDAO.get(com.getUserId()).getName();
				responseJsob.put("name", resUserName);
				responseJsob.put("commentContent", com.getCommentContent());
				responseJsob.put("commentTime", com.getCommentTime());

				responseJsar.add(responseJsob);
			}
			commentJsob.put("responseContent", responseJsar);
			commentJsar.add(commentJsob);
		}
		respJsob.put("comment", commentJsar);

		respJsob.put("PDF", cd.getPdf());

		// 将响应json对象进行相应
		jsonutil.putJsonObject(respJsob, resp);
	}
}
