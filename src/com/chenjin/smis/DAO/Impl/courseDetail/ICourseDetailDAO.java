package com.chenjin.smis.DAO.Impl.courseDetail;

import java.util.List;

import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.CourseDetail;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseDetailQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public interface ICourseDetailDAO {
	
	void save(CourseDetail cd);
	
	CourseDetail get(Long id);

	List<CourseDetail> list();
	
	void update(CourseDetail cd);
	
	void delete(Long id);
	
	// 将查询信息封装成对象，重构代码
	List<CourseDetail> query(courseDetailQueryObject uo);
}
