package com.chenjin.smis.domain;

import lombok.Data;

@Data
public class User {
	private Long userId;
	private String name;
	private String password;
	private String email;
	private int authority;
}
