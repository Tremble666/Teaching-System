package com.chenjin.smis.query;

import lombok.Data;

import com.chenjin.smis.query.QueryObject.orderBy;

@Data
public class courseDetailQueryObject extends QueryObject{
	private Long barId;
	private String barName;
	private String barIntroduction;
	private String path;
	private Long parentId;
	private Integer times;
	private String pdf;
	private Long courseId;
	
	//自己定制自己的查询条件及查询参数（覆盖父类的方法）
	protected void customizedQuery(){
		if(barId!=null){
			this.addQuery("barId = ?", barId);
		}
		if(haslength(barName)){
			this.addQuery("barName = ?", barName);
		}
		if(haslength(barIntroduction)){
			this.addQuery("barIntroduction = ?", barIntroduction);
		}
		if(haslength(path)){
			this.addQuery("path = ?", path);
		}
		if(parentId != null){
			this.addQuery("parentId = ?", parentId);
		}
		if(times != null){
			this.addQuery("times = ?", times);
		}
		if(haslength(pdf)){
			this.addQuery("pdf = ?", pdf);
		}
		if(courseId != null){
			this.addQuery("courseId = ?", courseId);
		}
		setOrder("barId",orderBy.ASC);
	}
}
