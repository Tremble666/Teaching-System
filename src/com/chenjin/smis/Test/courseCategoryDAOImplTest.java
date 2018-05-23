package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.courseCategory.ICourseCategoryDAO;
import com.chenjin.smis.DAO.Impl.courseCategory.courseCategoryDAOImpl;
import com.chenjin.smis.domain.Category;
import com.chenjin.smis.query.categoryQueryObject;

public class courseCategoryDAOImplTest {
	private ICourseCategoryDAO categoryDAO = new courseCategoryDAOImpl();
	
	@Test
	public void testSave() {
		Category ctg = new Category();
		ctg.setCategoryName("体育");
		ctg.setCategoryUrl("/course?cmd='体育'");
		categoryDAO.save(ctg);
	}

	@Test
	public void testGet() {
		Long id = 2l;
		Category category = categoryDAO.get(id);
		System.out.println(category);
	}

	@Test
	public void testList() {
		List<Category> list = categoryDAO.list();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getCategoryName());
		}
	}
	@Test
	public void testUpdate() throws Exception {
		Category cg = categoryDAO.get(7l);
		cg.setCategoryName("地理科学");
		categoryDAO.update(cg);
	}
	@Test
	public void testDelete() throws Exception {
		categoryDAO.delete(7l);
	}
	@Test
	public void testQuery() throws Exception {
		categoryQueryObject cqo = new categoryQueryObject();
		cqo.setCategoryName("计算机");
		List<Category> cList = categoryDAO.query(cqo);
		for (Category category : cList) {
			System.out.println(category);
		}
	}
}
