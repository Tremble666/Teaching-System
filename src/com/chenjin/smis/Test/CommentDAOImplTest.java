package com.chenjin.smis.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chenjin.smis.DAO.Impl.comment.CommentDAOImpl;
import com.chenjin.smis.DAO.Impl.comment.ICommentDAO;
import com.chenjin.smis.domain.Comment;
import com.chenjin.smis.query.commentQueryObject;

public class CommentDAOImplTest {
	private ICommentDAO commentDAO = new CommentDAOImpl(); 
	@Test
	public void testSave() {
		Comment comment = new Comment();
		comment.setCommentContent("老师差劲");
		comment.setCommentTime("2004-08-13 18:29:00");
		comment.setBarId(8l);
		comment.setUserId(14l);
		commentDAO.save(comment);
	}

	@Test
	public void testGet() {
		Comment comment = commentDAO.get(3l);
		System.out.println(comment);
	}

	@Test
	public void testList() {
		List<Comment> list = commentDAO.list();
		for (Comment comment : list) {
			System.out.println(comment);
		}
	}
	
	@Test
	public void testQuery() throws Exception {
		commentQueryObject qo = new commentQueryObject();
		qo.setParentId(13l);
		List<Comment> list = commentDAO.query(qo);
		for (Comment comment : list) {
			System.out.println(comment);
		}
	}
	@Test
	public void testDelete() throws Exception {
		commentDAO.delete(12l);
	}
}
