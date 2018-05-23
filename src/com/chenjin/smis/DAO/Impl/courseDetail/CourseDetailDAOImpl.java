package com.chenjin.smis.DAO.Impl.courseDetail;

import java.util.List;

import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class CourseDetailDAOImpl implements ICourseDetailDAO{

	@Override
	public void save(CourseDetail cd) {
		String sql = "insert into CourseDetail(barName,barIntroduction,path,parentId,times,pdf,courseId) values(?,?,?,?,?,?,?)";
		Object[] obj = {cd.getBarName(),cd.getBarIntroduction(),cd.getPath(),cd.getParentId(),cd.getTimes(),cd.getPdf(),cd.getCourseId()};
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public CourseDetail get(Long id) {
		String sql = "select * from CourseDetail where barId = ?";
		Object[] parms = { id };
		CourseDetail cd = jdbcTemplate.query(sql,new BeanHandler<>(CourseDetail.class), parms);
		return cd;
	}

	@Override
	public List<CourseDetail> list() {
		String sql = "select * from CourseDetail";
		Object[] parms = {};
		List<CourseDetail> list = jdbcTemplate.query(sql,new BeanListResultSetHandler<>(CourseDetail.class), parms);
		return list;
	}
	public void update(CourseDetail cd) {
		String sql = "update coursedetail set barName=?,barIntroduction=?,path=?,parentId=?,times=?,pdf=?,courseId=? where barId=?";
		Object[] obj = {cd.getBarName(),cd.getBarIntroduction(),cd.getPath(),cd.getParentId(),cd.getTimes(),cd.getPdf(),cd.getCourseId(),cd.getBarId()};
		jdbcTemplate.update(sql, obj);
	}

	public void delete(Long id) {
		String sql = "delete from coursedetail where barId = ?";
		Object[] obj = { id };
		jdbcTemplate.update(sql, obj);
	}
	@Override
	public List<CourseDetail> query(courseDetailQueryObject uo) {
		String sql = "select * from courseDetail" + uo.getQuery();
		System.out.println(sql);
		List<CourseDetail> list = jdbcTemplate.query(sql, new BeanListResultSetHandler<>(CourseDetail.class), uo.getParams()
				.toArray());
		return list;
	}	
}
