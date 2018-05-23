package com.chenjin.smis.domain;

import lombok.Data;

@Data
public class Comment {
	private Long commentId;
	private String commentContent;
	private String commentTime;
	private Long parentId;
	private Long barId;
	private Long userId;
}
