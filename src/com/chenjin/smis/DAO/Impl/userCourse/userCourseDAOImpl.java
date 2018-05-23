package com.chenjin.smis.DAO.Impl.userCourse;

import java.util.List;

import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.domain.UserCourse;
import com.chenjin.smis.query.UserCourseQueryObject;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class userCourseDAOImpl implements IUserCourseDAO{

	@Override
	public void save(Long userId,Long courseId) {
		String sql = "insert into courseselection(userId,courseId) values(?,?)";
		Object[] obj = {userId,courseId};
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public UserCourse get(Long userId,Long courseId) {
		String sql = "select * from courseselection where userId = ? and courseId = ?";
		Object[] parms = { userId,courseId };
		UserCourse userCourse = jdbcTemplate.query(sql,new BeanHandler<>(UserCourse.class), parms);
		return userCourse;
	}

	@Override
	public List<UserCourse> list() {
		String sql = "select * from courseselection";
		Object[] parms = {};
		List<UserCourse> list = jdbcTemplate.query(sql,new BeanListResultSetHandler<>(UserCourse.class), parms);
		return list;
	}

	@Override
	public List<UserCourse> query(UserCourseQueryObject uo) {
		String sql = "select * from courseselection" + uo.getQuery();
		System.out.println(sql);
		List<UserCourse> list = jdbcTemplate.query(sql, new BeanListResultSetHandler<>(UserCourse.class), uo.getParams()
				.toArray());
		return list;
	}

	
}
