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
import com.chenjin.smis.DAO.Impl.course.CourseDAOImpl;
import com.chenjin.smis.DAO.Impl.course.ICourseDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.ICourseCategoryDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.courseCategoryDAOImpl;
import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseQueryObject;
import com.chenjin.smis.util.jsonutil;
import com.chenjin.smis.util.stringutil;

//处理主页请求的servlet
@WebServlet("/courseCateGory")
@SuppressWarnings("all")
public class courseCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//课程分类DAO对象
	private ICourseCategoryDAO categoryDAO = new courseCategoryDAOImpl();
	//课程DAO对象
	private ICourseDAO courseDAO = new CourseDAOImpl();
	
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码为utf-8
		req.setCharacterEncoding("utf-8");

		// cmd参数为该课程分类的Id
		String cmd = req.getParameter("cmd");
		if (stringutil.haslength(cmd)) {
			this.handleCourse(req, resp, cmd);
		} else {
			this.list(req, resp);
		}
	}

	// 处理点击具体某一课程时发送的请求，返回的信息是该课程相关的信息
	protected void handleCourse(HttpServletRequest req, HttpServletResponse resp,String cateGoryId)
			throws ServletException, IOException {
		courseQueryObject qo = new courseQueryObject();
		qo.setCategoryId(Long.valueOf(cateGoryId));
		List<Course> list= courseDAO.query(qo);
		//list.get(0)是根据courseName进行查询后获取的课程对象
		if(list.size()>0){
			//创建响应json对象，将jsonArray对象塞入其中
			JSONObject respJsob = new JSONObject();
			
			//jsonArray对象，将课程信息json对象放入其中
			JSONArray jsar = new JSONArray();
			for (Course course : list) {
				//将所有课程信息封装到json对象中
				JSONObject courseJsob = new JSONObject();
				courseJsob.put("name",course.getName());
				courseJsob.put("courseId", course.getCourseId()+"");
				courseJsob.put("teacher", course.getTeacher());
				
				//将封装好信息的json对象加入jsonArray对象中
				jsar.add(courseJsob);	
			}
			
			respJsob.put("data", jsar);
			
			//将相应信息进行响应
			jsonutil.putJsonObject(respJsob, resp);
		}
	}

	// 点击目录时发送的请求，返回的是所有的课程信息
	protected void list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> list = categoryDAO.list();
		//创建json对象数组
		JSONObject [] jsob = new JSONObject[list.size()];
		System.out.println(jsob.length);
		for (int i = 0; i < list.size(); i++) {
			jsob[i] = new JSONObject();
			jsob[i].put("categoryName",list.get(i).getCategoryName());
			jsob[i].put("categoryId", list.get(i).getCategoryId().toString());
		}
		
		//创建jsonArray对象，将json对象数组中的对象放入里边
		JSONArray jsar = new JSONArray();
		for (int i = 0; i < jsob.length; i++) {
			jsar.add(jsob[i]);
		}
		
		//将列表加入到响应json对象中，响应给浏览器
		JSONObject respJsonObj = new JSONObject();
		respJsonObj.put("data", jsar);
		//将该json对象响应给浏览器
		jsonutil.putJsonObject(respJsonObj, resp);
		System.out.println(respJsonObj);
	}
}
