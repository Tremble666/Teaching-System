package com.chenjin.smis.domain;

import lombok.Data;

@Data
//用户与课程关系表对象
public class UserCourse {
	private Long userId;
	private Long courseId;
}
