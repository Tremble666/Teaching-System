package com.chenjin.smis.domain;

import lombok.Data;

@Data
public class Course {
	private long courseId;
	private String name;
	private String introduction;
	private String outline;
	private String picture;
	private String teacher;
	private long categoryId;
}
