package com.chenjin.smis.domain;

import lombok.Data;

@Data
public class CourseDetail {
	private Long barId;
	private String barName;
	private String barIntroduction;
	private String path;
	private Long parentId;
	private int times;
	private String pdf;
	private long courseId;
}
