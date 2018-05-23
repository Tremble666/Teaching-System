package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.userCourse.IUserCourseDAO;
import com.chenjin.smis.DAO.Impl.userCourse.userCourseDAOImpl;
import com.chenjin.smis.domain.UserCourse;
import com.chenjin.smis.query.UserCourseQueryObject;

public class userCourseDAOImplTest {
	private IUserCourseDAO userCourseDAO = new userCourseDAOImpl();
	@Test
	public void testSave() {
		userCourseDAO.save(4l, 11l);
	}

	@Test
	public void testGet() {
		UserCourse uc = userCourseDAO.get(1l, 7l);
		System.out.println(uc);
	}

	@Test
	public void testList() {
		List<UserCourse> list = userCourseDAO.list();
		for (UserCourse uc : list) {
			System.out.println(uc);
		}
	}
	@Test
	public void testQuery() throws Exception {
		UserCourseQueryObject qo =new UserCourseQueryObject();
		qo.setCourseId(8l);
		List<UserCourse> list = userCourseDAO.query(qo);
		for (UserCourse uc : list) {
			System.out.println(uc);
		}
	}
}
