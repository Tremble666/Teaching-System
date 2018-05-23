package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.course.CourseDAOImpl;
import com.chenjin.smis.DAO.Impl.course.ICourseDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.ICourseCategoryDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.courseCategoryDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.CourseDetailDAOImpl;
import com.chenjin.smis.DAO.Impl.courseDetail.ICourseDetailDAO;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class CourseDetailDAOImplTest {
	private CourseDetailDAOImpl courseDetailDAO = new CourseDetailDAOImpl();
	private ICourseCategoryDAO categoryDAO = new courseCategoryDAOImpl();
	private ICourseDAO courseDAO = new CourseDAOImpl();
	@Test
	public void testSave() {
		CourseDetail cd = new CourseDetail();
		cd.setBarName("多线程");
		cd.setBarIntroduction("这一小节带你介绍java的起源");
		cd.setPath("c://python//java.mp4");
		cd.setParentId(2l);
		cd.setTimes(3);
		cd.setPdf("c://python//java.pdf");
		cd.setCourseId(9l);
		courseDetailDAO.save(cd);
	}

	@Test
	public void testGet() {
		CourseDetail cd = courseDetailDAO.get(2l);
		System.out.println(cd);
	}

	@Test
	public void testList() {
		List<CourseDetail> list = courseDetailDAO.list();
		for (CourseDetail cd : list) {
			String cateId = categoryDAO.get(courseDAO.get(cd.getCourseId()).getCategoryId()).getCategoryId()+"";
			String pdf = "/upload/pdf/"+cateId+"/"+cd.getCourseId()+"/"+"学习资源.docx";
			cd.setPdf(pdf);
			courseDetailDAO.update(cd);
		}
	}
	@Test
	public void testUpdate() throws Exception {
		CourseDetail cd = courseDetailDAO.get(10l);
		cd.setBarIntroduction("异常机制是java中的一项非常重要的机制");
		courseDetailDAO.update(cd);
	}
	@Test
	public void testDelete() throws Exception {
		courseDetailDAO.delete(12l);
	}
	@Test
	public void testQuery() throws Exception {
		courseDetailQueryObject qo = new courseDetailQueryObject();
		qo.setParentId(4l);
		List<CourseDetail> list = courseDetailDAO.query(qo);
		for (CourseDetail cd : list) {
			System.out.println(cd);
		}
	}
}
