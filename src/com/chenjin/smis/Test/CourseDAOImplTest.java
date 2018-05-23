package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.course.CourseDAOImpl;
import com.chenjin.smis.DAO.Impl.course.ICourseDAO;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.query.courseQueryObject;

public class CourseDAOImplTest {
	private ICourseDAO courseDAO = new CourseDAOImpl();
	@Test
	public void testSave() {
		Course course = new Course();
		course.setName("足球");
		course.setIntroduction("足球是一门高级程序语言...");
		course.setOutline("1.走进地足球");
		course.setPicture("c:/picture/足球.jpg");
		course.setTeacher("程泰宁");
		course.setCategoryId(6l);
		courseDAO.save(course);
	}

	@Test
	public void testGet() {
		Course course = courseDAO.get(8l);
		System.out.println(course);
	}
	@Test
	public void testUpdate() throws Exception {
		Course course = courseDAO.get(17l);
		course.setIntroduction("足球是一项大众喜爱的运动");
		courseDAO.update(course);
	}
	@Test
	public void testList() {
		List<Course> list = courseDAO.list();
		for (Course course : list) {
			System.out.println(course);
		}
	}
	@Test
	public void testQuery() {
		courseQueryObject qo = new courseQueryObject();
		qo.setKeyword("python");
		List<Course> list = courseDAO.query(qo);
		for (Course course : list) {
			System.out.println(course);
		}
	}
}
