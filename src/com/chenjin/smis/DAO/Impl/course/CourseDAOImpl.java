package com.chenjin.smis.DAO.Impl.course;

import java.util.List;

import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class CourseDAOImpl implements ICourseDAO {

	@Override
	public void save(Course course) {
		String sql = "insert into course(name,introduction,outline,picture,teacher,categoryId) values(?,?,?,?,?,?)";
		Object[] obj = { course.getName(), course.getIntroduction(),
				course.getOutline(), course.getPicture(), course.getTeacher(),
				course.getCategoryId() };
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public Course get(Long id) {
		String sql = "select * from course where courseId = ?";
		Object[] parms = { id };
		Course course = jdbcTemplate.query(sql,
				new BeanHandler<>(Course.class), parms);
		return course;
	}

	public void update(Course course) {
		String sql = "update course set name=?,introduction=?,teacher=?,picture=?,categoryId=? where courseId=?";
		Object[] obj = { course.getName(), course.getIntroduction(),
				course.getTeacher(), course.getPicture(),
				course.getCategoryId(), course.getCourseId() };
		jdbcTemplate.update(sql, obj);
	}

	public void delete(Long id) {
		String sql = "delete from course where courseId = ?";
		Object[] obj = { id };
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public List<Course> list() {
		String sql = "select * from course";
		Object[] parms = {};
		List<Course> list = jdbcTemplate.query(sql,
				new BeanListResultSetHandler<>(Course.class), parms);
		return list;
	}

	@Override
	public List<Course> query(courseQueryObject uo) {
		String sql = "select * from course" + uo.getQuery();
		System.out.println(sql);
		List<Course> list = jdbcTemplate.query(sql,
				new BeanListResultSetHandler<>(Course.class), uo.getParams()
						.toArray());
		return list;
	}

}
