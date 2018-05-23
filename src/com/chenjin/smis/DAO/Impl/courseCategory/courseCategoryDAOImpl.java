package com.chenjin.smis.DAO.Impl.courseCategory;

import java.util.List;

import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.categoryQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class courseCategoryDAOImpl implements ICourseCategoryDAO{

	public void save(Category category) {
		String sql = "insert into courseCategory(categoryName,categoryUrl) values(?,?)";
		Object[] obj = {category.getCategoryName(),category.getCategoryUrl()};
		jdbcTemplate.update(sql, obj);
	}
	
	public void update(Category category) {
		String sql = "update coursecategory set categoryName=? where categoryId=?";
		Object[] obj = {category.getCategoryName(),category.getCategoryId()};
		jdbcTemplate.update(sql, obj);
	}

	public void delete(Long id) {
		String sql = "delete from coursecategory where categoryId = ?";
		Object[] obj = { id };
		jdbcTemplate.update(sql, obj);
	}

	public Category get(Long id) {
		String sql = "select * from courseCategory where categoryID = ?";
		Object[] parms = { id };
		Category category = jdbcTemplate.query(sql,new BeanHandler<>(Category.class), parms);
		return category;
	}

	public List<Category> list() {
		String sql = "select * from courseCategory";
		Object[] parms = {};
		List<Category> list = jdbcTemplate.query(sql,new BeanListResultSetHandler<>(Category.class), parms);
		return list;
	}

	public List<Category> query(categoryQueryObject cq) {
		String sql = "select * from coursecategory" + cq.getQuery();
		System.out.println(sql);
		List<Category> list = jdbcTemplate.query(sql, new BeanListResultSetHandler<>(Category.class), cq.getParams()
				.toArray());
		return list;
	}
}
