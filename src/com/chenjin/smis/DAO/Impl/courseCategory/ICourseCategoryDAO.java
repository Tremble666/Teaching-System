package com.chenjin.smis.DAO.Impl.courseCategory;

import java.util.List;

import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.categoryQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public interface ICourseCategoryDAO {

	void save(Category category);

	Category get(Long id);

	List<Category> list();

	void update(Category category);

	void delete(Long id);

	
	 //将查询信息封装成对象，重构代码
	List<Category> query(categoryQueryObject cq);
	 
}
