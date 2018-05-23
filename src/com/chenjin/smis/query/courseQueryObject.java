package com.chenjin.smis.query;

import lombok.Data;

import com.chenjin.smis.query.QueryObject.orderBy;

@Data
public class courseQueryObject extends QueryObject{
	private Long courseId;
	private String name;
	private String introduction;
	private String outiline;
	private String picture;
	private String teacher;
	private Long categoryId;
	private String keyword;
	
	//自己定制自己的查询条件及查询参数（覆盖父类的方法）
	protected void customizedQuery(){
		if(courseId!=null){
			this.addQuery("courseId = ?", courseId);
		}
		if(haslength(name)){
			this.addQuery("name = ?", name);
		}
		if(haslength(introduction)){
			this.addQuery("introduction = ?", introduction);
		}
		if(haslength(outiline)){
			this.addQuery("outiline = ?", outiline);
		}
		if(haslength(picture)){
			this.addQuery("picture = ?", picture);
		}
		if(haslength(teacher)){
			this.addQuery("teacher = ?", teacher);
		}
		if(categoryId!=null){
			this.addQuery("categoryId = ?", categoryId);
		}
		if(haslength(keyword)){
			this.addQuery("name like ? or introduction like ? or teacher like ?","%"+keyword+"%","%"+keyword+"%","%"+keyword+"%");
		}
		setOrder("courseId",orderBy.ASC);
	}
}
