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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.IQuery;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.util.jsonutil;

//课程管理员进行课程增删改的操作的servlet
@WebServlet("/barUpdate")
@SuppressWarnings("all")
public class barUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
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

		// JSONObject jsob = jsonutil.getJsonObject(req);

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println(isMultipart);
		if (!isMultipart) {
			return;
		} else {
			// 1.创建FileItemFactory对象，用来创建FileItem对象
			// FileItemFactory对象是对Form表单中表单控件的封装
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(req);

			// 一旦拿到表单数据barName和afterBarId,就存入改变该变量中
			String barName = "";
			String afterBarId = "";
			String curCourseId = "";
			for (FileItem item : items) {
				// getFieldName()得到的是表单中name的值，getName()得到的是文件的名称
				String fieldName = item.getFieldName();

				if (item.isFormField()) {
					// 普通表单控件
					String value = item.getString("utf-8");

					if ("barName".equals(fieldName)) {
						barName = value;
					} else if ("afterBarId".equals(fieldName)) {
						afterBarId = value;
					} else if ("courseId".equals(fieldName)) {
						curCourseId = value;
					}
				} else {
					System.out.println(barName + "aa");
					System.out.println(afterBarId + "aa");

					// 因为会发送两次请求，所以加个判断只执行一次
					courseDetailQueryObject cdQo = new courseDetailQueryObject();
					cdQo.setBarName(barName);
					List<CourseDetail> detailList = courseDetailDAO.query(cdQo);

					if (detailList.size() == 0) {
						if (afterBarId.equals("0")) {
							// 如果新增的是第一节课
							CourseDetail curCd = new CourseDetail();
							curCd.setBarName(barName);
							curCd.setParentId(0l);
							curCd.setCourseId(Long.valueOf(curCourseId));
							courseDetailDAO.save(curCd);
							
						} else {
							// 在新增新的小节之前，确定原先被插入的后一小节是那个
							cdQo = new courseDetailQueryObject();
							cdQo.setParentId(Long.valueOf(afterBarId));
							List<CourseDetail> cdList = courseDetailDAO
									.query(cdQo);

							// 设置新增小节的相关信息
							CourseDetail newBar = new CourseDetail();
							newBar.setBarName(barName);
							newBar.setParentId(Long.valueOf(afterBarId));
							newBar.setCourseId(courseDetailDAO.get(
									Long.valueOf(afterBarId)).getCourseId());

							if (cdList.size() == 0) {
								// 如果没有以afterBarId作为parentId的小节，说明afterBarId就是最后一个小节
								courseDetailDAO.save(newBar);
							} else { //
								// 如果被插入小节的后一小节不为空，则将那一小节的parentId设为新增小节
								courseDetailDAO.save(newBar);

								cdQo = new courseDetailQueryObject();
								cdQo.setBarName(barName);
								List<CourseDetail> newBarList = courseDetailDAO
										.query(cdQo);

								cdList.get(0).setParentId(
										newBarList.get(0).getBarId());
								courseDetailDAO.update(cdList.get(0));
							}
						}
					}
						// 表单上传控件
						//System.out.println(fieldName + ":" + item.getName());
						if (item.getName().endsWith(".docx")) {
							String realPdfPath = "";
							/*Long courseId = courseDetailDAO.get(
									Long.valueOf(afterBarId)).getCourseId();*/
							Long courseId = Long.valueOf(curCourseId);
							String courseName = courseDAO.get(courseId)
									.getName();
							String categoryId = courseDAO.get(courseId).getCategoryId()+"";
						
							realPdfPath += "/upload/pdf" + "/" + categoryId;
							realPdfPath += "/" + courseId + "/";
							System.out.println(realPdfPath);
							// 将该path保存至数据库
							cdQo = new courseDetailQueryObject();
							cdQo.setBarName(barName);
							detailList = courseDetailDAO.query(cdQo);
							detailList.get(0).setPdf(
									realPdfPath + item.getName());
							courseDetailDAO.update(detailList.get(0));

							item.write(new File(this.getServletContext()
									.getRealPath(realPdfPath), item.getName()));
						}
						if (item.getName().endsWith(".mp4")) {
							String realVideoPath = "";
							/*Long courseId = courseDetailDAO.get(
									Long.valueOf(afterBarId)).getCourseId();*/
							Long courseId = Long.valueOf(curCourseId);
							String courseName = courseDAO.get(courseId)
									.getName();
							String categoryId = courseDAO.get(courseId).getCategoryId()+"";
							realVideoPath += "/upload/video" + "/"
									+ categoryId;
							realVideoPath += "/" + courseId + "/";
							System.out.println(realVideoPath);

							// 将该path保存至数据库
							cdQo = new courseDetailQueryObject();
							cdQo.setBarName(barName);
							detailList = courseDetailDAO.query(cdQo);
							detailList.get(0).setPath(
									realVideoPath + item.getName());
							courseDetailDAO.update(detailList.get(0));

							item.write(new File(this.getServletContext()
									.getRealPath(realVideoPath), item.getName()));
						}
				}
			}
		}
	}

	// 处理修改表单
	protected void update(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		/*
		 * // 获取需要请求的json对象 JSONObject jsob = jsonutil.getJsonObject(req);
		 * 
		 * CourseDetail updateCd = courseDetailDAO.get(Long.valueOf(jsob
		 * .getString("barId")));
		 * 
		 * // 在修改小节之前，确定原先被插入的后一小节是那个 courseDetailQueryObject cdQo = new
		 * courseDetailQueryObject();
		 * cdQo.setParentId(Long.valueOf(jsob.getString("afterBarId")));
		 * List<CourseDetail> cdList = courseDetailDAO.query(cdQo);
		 * 
		 * if (cdList.size() == 0) { // //
		 * 如果没有以afterBarId作为parentId的小节，说明afterBarId就是最后一个小节
		 * updateCd.setParentId(Long.valueOf(jsob.getString("afterBarId")));
		 * courseDetailDAO.update(updateCd); } else {
		 * updateCd.setParentId(Long.valueOf(jsob.getString("afterBarId")));
		 * courseDetailDAO.update(updateCd);
		 * 
		 * cdList.get(0).setParentId(Long.valueOf(jsob.getString("barId")));
		 * courseDetailDAO.update(cdList.get(0)); }
		 */

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println(isMultipart);
		if (!isMultipart) {
			return;
		} else {
			// 1.创建FileItemFactory对象，用来创建FileItem对象
			// FileItemFactory对象是对Form表单中表单控件的封装
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(req);

			// 一旦拿到表单数据barName和afterBarId,就存入改变该变量中
			String barName = "";
			String barId = "";
			for (FileItem item : items) {
				// getFieldName()得到的是表单中name的值，getName()得到的是文件的名称
				String fieldName = item.getFieldName();

				if (item.isFormField()) {
					// 普通表单控件
					String value = item.getString("utf-8");
					System.out.println(fieldName + value);
					if ("barName".equals(fieldName)) {
						barName = value;
					} else if ("barId".equals(fieldName)) {
						barId = value;
					}
				} else {
					// 将该barId的名称改为barName
					CourseDetail currentBar = courseDetailDAO.get(Long
							.valueOf(barId));
					currentBar.setBarName(barName);
					if (item.getName().endsWith(".docx")) {
						String realPdfPath = FilenameUtils
								.getFullPath(currentBar.getPdf());
						// 将上传的pdf保存至文件夹中
						item.write(new File(this.getServletContext()
								.getRealPath(realPdfPath), item.getName()));
						System.out.println("pdf:" + realPdfPath);
						// 将pdf路径更新至数据库
						realPdfPath += item.getName();
						currentBar.setPdf(realPdfPath);
						courseDetailDAO.update(currentBar);

					}
					if (item.getName().endsWith(".mp4")) {
						String realVideoPath = FilenameUtils
								.getFullPath(currentBar.getPath());
						// 将上传的pdf保存至文件夹中
						item.write(new File(this.getServletContext()
								.getRealPath(realVideoPath), item.getName()));
						System.out.println("mp4:" + realVideoPath);
						// 将视屏的路径更新到数据库
						realVideoPath += item.getName();
						currentBar.setPath(realVideoPath);
						courseDetailDAO.update(currentBar);

					}

				}
			}
		}
	}

	// 处理删除
	protected void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String barId = req.getParameter("barId");
		System.out.println(barId);

		// 再删除小节之前，先判断该小节是否为最后一小节
		courseDetailQueryObject cdQo = new courseDetailQueryObject();
		cdQo.setParentId(Long.valueOf(barId));
		List<CourseDetail> cdList = courseDetailDAO.query(cdQo);

		if (cdList.size() == 0) {
			courseDetailDAO.delete(Long.valueOf(barId));
		} else {
			// 先获取被删除小节的前一小节
			CourseDetail deleteCd = courseDetailDAO.get(Long.valueOf(barId));
			// 如果删除小节不是第一小节
			if (deleteCd.getParentId() == 0l) {
				cdList.get(0).setParentId(0l);
				courseDetailDAO.update(cdList.get(0));
				courseDetailDAO.delete(Long.valueOf(barId));
			} else {
				cdList.get(0).setParentId(deleteCd.getParentId());
				courseDetailDAO.update(cdList.get(0));
				courseDetailDAO.delete(Long.valueOf(barId));
			}
		}
	}
}