package com.chenjin.smis.DAO.Impl.user;

import java.util.List;
import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;

public class UserDAOImpl implements IUserDAO {
	//用户信息保存操作
	public void save(User user) {
		String sql = "insert into user(name,password,email,authority) values(?,?,?,?)";
		Object[] obj = {user.getName(),user.getPassword(),user.getEmail(),user.getAuthority()};
		jdbcTemplate.update(sql, obj);
	}
	//用户信息删除操作
	public void delete(Long id) {
		String sql = "delete from user where userId = ?";
		Object[] obj = {id};
		jdbcTemplate.update(sql, obj);
	}
	//用户信息更新操作
	public void update(User user) {
		String sql = "update user set name=?,password=?,email=?,authority=? where userId=?";
		Object[] obj = { user.getName(),user.getPassword(),user.getEmail(),user.getAuthority(),user.getUserId()};
		jdbcTemplate.update(sql, obj);
	}
	//根据id获取用户信息操作
	public User get(Long id) {
		String sql = "select * from user where userId = ?";
		Object[] parms = { id };
		User user = jdbcTemplate.query(sql,new BeanHandler<>(User.class), parms);
		return user;
	}
	////获取所有用户信息
	public List<User> list() {
		String sql = "select * from User";
		Object[] parms = {};
		List<User> list = jdbcTemplate.query(sql,new BeanListResultSetHandler<>(User.class), parms);
		return list;
	}
	@Override
	public List<User> query(UserQueryObject uo) {
		String sql = "select * from user" + uo.getQuery();
		System.out.println(sql);
		List<User> list = jdbcTemplate.query(sql, new BeanListResultSetHandler<>(User.class), uo.getParams()
				.toArray());
		return list;
	}
}
