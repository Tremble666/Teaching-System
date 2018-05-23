package com.chenjin.smis.DAO.Impl.userCourse;

import java.util.List;

import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.domain.UserCourse;
import com.chenjin.smis.query.UserCourseQueryObject;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public interface IUserCourseDAO {
	
	void save(Long userId,Long courseId);
	
	UserCourse get(Long userId,Long courseId);

	List<UserCourse> list();
	
	// 将查询信息封装成对象，重构代码
	List<UserCourse> query(UserCourseQueryObject uo);
}
