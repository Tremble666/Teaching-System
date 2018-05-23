package com.chenjin.smis.DAO.Impl.comment;

import java.util.List;

import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.domain.Course;
import com.chenjin.smis.domain.User;
import com.chenjin.smis.query.UserQueryObject;
import com.chenjin.smis.query.commentQueryObject;
import com.chenjin.smis.query.courseQueryObject;

public interface ICommentDAO {
	
	void save(Comment comment);
	
	Comment get(Long id);

	List<Comment> list();
	
	void delete(Long id);
	
	// 将查询信息封装成对象，重构代码
	List<Comment> query(commentQueryObject uo);
}
