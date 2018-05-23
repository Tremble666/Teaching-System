package com.chenjin.smis.DAO.Impl.comment;

import java.util.List;

import com.chenjin.smis.DMLTemplate.jdbcTemplate;
import com.chenjin.smis.ResultSetHandler.BeanHandler;
import com.chenjin.smis.ResultSetHandler.BeanListResultSetHandler;
import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public class CommentDAOImpl implements ICommentDAO{

	@Override
	public void save(Comment comment) {
		String sql = "insert into comment(commentContent,commentTime,parentId,barId,userId) values(?,?,?,?,?)";
		Object[] obj = {comment.getCommentContent(),comment.getCommentTime(),comment.getParentId(),comment.getBarId(),comment.getUserId()};
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public Comment get(Long id) {
		String sql = "select * from comment where commentId = ?";
		Object[] parms = { id };
		Comment comment = jdbcTemplate.query(sql,new BeanHandler<>(Comment.class), parms);
		return comment;
	}

	@Override
	public List<Comment> list() {
		String sql = "select * from comment";
		Object[] parms = {};
		List<Comment> list = jdbcTemplate.query(sql,new BeanListResultSetHandler<>(Comment.class), parms);
		return list;
	}
	
	public void delete(Long id) {
		String sql = "delete from comment where commentId = ?";
		Object[] obj = { id };
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public List<Comment> query(commentQueryObject uo) {
		String sql = "select * from comment" + uo.getQuery();
		System.out.println(sql);
		List<Comment> list = jdbcTemplate.query(sql, new BeanListResultSetHandler<>(Comment.class), uo.getParams()
				.toArray());
		return list;
	}

	
}
