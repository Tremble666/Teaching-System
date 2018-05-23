package com.chenjin.web.servlet.management;

import java.io.BufferedReader;
import java.io.File;
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

import org.apache.commons.io.FilenameUtils;

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
import com.chenjin.smis.util.fileutil;
import com.chenjin.smis.util.jsonutil;

//课程管理员进行课程增删改的操作的servlet
@WebServlet("/cateGoryManagement")
@SuppressWarnings("all")
public class cateGoryManagementServlet extends HttpServlet {
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
		String categoryName = jsob.getString("categoryName");
		
		Category cg = new Category();
		cg.setCategoryName(categoryName);
		categoryDAO.save(cg);
		
		
		categoryQueryObject cqo = new categoryQueryObject();
		cqo.setCategoryName(categoryName);
		List<Category> cList = categoryDAO.query(cqo);
		
		//新增分类是增加一个文件夹
		fileutil.mkDirectory(req.getRealPath("/upload/pdf/"+cList.get(0).getCategoryId()));
		fileutil.mkDirectory(req.getRealPath("/upload/video/"+cList.get(0).getCategoryId()));
	}

	// 处理修改表单
	protected void update(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		String categoryId = jsob.getString("categoryId");
		Category cg = categoryDAO.get(Long.valueOf(categoryId));
		cg.setCategoryName(jsob.getString("name"));
		categoryDAO.update(cg);
	}

	// 处理删除
	protected void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		JSONObject jsob = jsonutil.getJsonObject(req);
		JSONArray jsar = jsob.getJSONArray("categoryIdList");
		for (int i = 0; i < jsar.size(); i++) {
			String categoryId = jsar.getString(i);
			categoryDAO.delete(Long.valueOf(categoryId));
		}
	}
}
