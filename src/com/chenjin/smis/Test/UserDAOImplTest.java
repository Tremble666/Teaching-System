package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.user.IUserDAO;
import com.chenjin.smis.DAO.Impl.user.UserDAOImpl;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;

public class UserDAOImplTest {
	private IUserDAO userDAO = new UserDAOImpl();
	@Test
	public void testSave() {
		User u = new User();
		u.setName("李四");
		u.setPassword("1232312");
		u.setEmail("32432432");
		u.setAuthority(2);
		userDAO.save(u);
	}

	@Test
	public void testDelete(){
		List<User> users = userDAO.list();
		System.out.println(users.size());
		userDAO.delete(21l);
		List<User> users1 = userDAO.list();
		System.out.println(users1.size());
	}

	@Test
	public void testUpdate() {
		System.out.println(userDAO.get(6l));
		User u = userDAO.get(6l);
		u.setUserId(6l);
		u.setAuthority(2);
		u.setEmail("588");
		u.setName("东门吹雪");
		u.setPassword("588");
		userDAO.update(u);
		System.out.println(userDAO.get(6l));
	}

	@Test
	public void testGet() {
		User u = userDAO.get(2l);
		System.out.println(u);
	}

	@Test
	public void testList() {
		List<User> list =userDAO.list();
		for (User user : list) {
			System.out.println(user);
		}
	}
	
	@Test
	public void testQuery() throws Exception {
		UserQueryObject uo = new UserQueryObject();
		uo.setAuthority(2768);
		List<User> list = userDAO.query(uo);
		System.out.println(list);
		for (User user : list) {
			System.out.println(user);
		}
	}

}
