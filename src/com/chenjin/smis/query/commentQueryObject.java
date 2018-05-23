package com.chenjin.smis.query;

import lombok.Data;

import com.chenjin.smis.query.QueryObject.orderBy;

@Data
public class commentQueryObject extends QueryObject{
	private Long commentId;
	private String commentContent;
	private String commentTime;
	private Long parentId;
	private Long barId;
	private Long userId;
	
	//自己定制自己的查询条件及查询参数（覆盖父类的方法）
	protected void customizedQuery(){
		if(commentId!=null){
			this.addQuery("commentId = ?", commentId);
		}
		if(haslength(commentContent)){
			this.addQuery("commentContent = ?", commentContent);
		}
		if(haslength(commentTime)){
			this.addQuery("commentTime = ?", commentTime);
		}
		if(parentId != null){
			this.addQuery("parentId = ?", parentId);
		}
		if(barId != null){
			this.addQuery("barId = ?", barId);
		}
		if(userId != null){
			this.addQuery("userId = ?", userId);
		}
		setOrder("commentId",orderBy.ASC);
	}
}
