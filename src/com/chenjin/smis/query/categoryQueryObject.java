package com.chenjin.smis.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
//封装商品查询信息对象
public class categoryQueryObject extends QueryObject{
	private Long categoryId;
	private String categoryName;
	
	
	//自己定制自己的查询条件及查询参数（覆盖父类的方法）
	protected void customizedQuery(){
		if(categoryId!=null){
			this.addQuery("categoryId = ?", categoryId);
		}
		if(haslength(categoryName)){
			this.addQuery("categoryName = ?", categoryName);
		}
		setOrder("categoryId",orderBy.ASC);
	}
}
