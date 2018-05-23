package com.chenjin.smis.query;

import lombok.Data;

import com.chenjin.smis.query.QueryObject.orderBy;

@Data
public class UserCourseQueryObject extends QueryObject{
	private Long userId;
	private Long courseId;
	
	//自己定制自己的查询条件及查询参数（覆盖父类的方法）
	protected void customizedQuery(){
		if(userId!=null){
			this.addQuery("userId = ?", userId);
		}
		if(courseId!=null){
			this.addQuery("courseId = ?", courseId);
		}
		
		setOrder("userId",orderBy.ASC);
	}
}
