package com.chenjin.web.servlet.videoAndcomment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.chenjin.smis.util.fileutil;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理请求小节视屏的servlet
@WebServlet("/makeComment")
@SuppressWarnings("all")
public class makeCommentServlet extends HttpServlet {
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

		// 获取点击评论时发送的json请求
		try {
			JSONObject reqJsob = jsonutil.getJsonObject(req);

			// 将提交的信息进行保存
			String userName = reqJsob.getString("name");
			UserQueryObject userQo = new UserQueryObject();
			userQo.setName(userName);
			List<User> userList = userDAO.query(userQo);
			Long userId = userList.get(0).getUserId();

			// 获取系统当前时间
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			// 将品论信息保存至数据库
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCommentContent(reqJsob.getString("commentContent"));
			comment.setCommentTime(dateFormat.format(date)+"");
			comment.setBarId(Long.valueOf(reqJsob.getString("barId")));
			
			String commentId = reqJsob.getString("commentId");
			//有commentId是回复，没有是发表的评论
			if (!"-1".equals(commentId)) {
				comment.setParentId(Long.valueOf(commentId));
				commentDAO.save(comment);
			} else {
				commentDAO.save(comment);
			}
			
			//返回的json数据
			JSONArray commentJsar = new JSONArray();
			commentQueryObject commentQo = new commentQueryObject();
			commentQo.setBarId(Long.valueOf(reqJsob.getString("barId")));
			List<Comment> commentList = commentDAO.query(commentQo);
			for (int i = 0; i < commentList.size(); i++) {
				JSONObject commentJsob = new JSONObject();
				if(commentList.get(i).getParentId() != null){
					continue;
				}
				commentJsob.put("commentId", commentList.get(i).getCommentId());
				
				
				//根据userId查找是哪个用户评论的
				String userName1 = userDAO.get(commentList.get(i).getUserId()).getName();
				commentJsob.put("name", userName1);
				
				//敏感词过滤
				String commentContent = fileutil.pass(commentList.get(i).getCommentContent());
				
				commentJsob.put("commentContent", commentContent);
				commentJsob.put("commentTime", commentList.get(i).getCommentTime());
				
				//回复评论的jsar
				JSONArray responseJsar = new JSONArray();
				
				commentQo = new commentQueryObject();
				commentQo.setParentId(commentList.get(i).getCommentId());
				List<Comment> responseList = commentDAO.query(commentQo);
				for (Comment com : responseList) {
					JSONObject responseJsob = new JSONObject();
					
					String resUserName = userDAO.get(com.getUserId()).getName();
					responseJsob.put("name",resUserName);
					
					//敏感词过滤
					String commentContentString = fileutil.pass(com.getCommentContent());
					
					responseJsob.put("commentContent", commentContentString);
					responseJsob.put("commentTime", com.getCommentTime());
					
					responseJsar.add(responseJsob);
				}
				commentJsob.put("responseContent", responseJsar);
				commentJsar.add(commentJsob);
			}
			
			//创建响应对象
			JSONObject respJsob = new JSONObject();
			respJsob.put("comment", commentJsar);
			
			//将响应json对象进行相应
			jsonutil.putJsonObject(respJsob, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
