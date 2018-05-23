package com.chenjin.smis.DAO.Impl.course;

import java.util.List;

import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public interface ICourseDAO {
	
	void save(Course course);
	
	Course get(Long id);
	
	void update(Course course);
	
	void delete(Long id);

	List<Course> list();
	
	// 将查询信息封装成对象，重构代码
	List<Course> query(courseQueryObject uo);
}
