package com.eco.domain;

import lombok.Data;

@Data
public class UserVO {
	private int user_cd;
	private String user_id;
	private String user_pw;
	private String user_nm;
	private String user_addr;
	private String user_mail;
	private String user_social;
	private char use_yn;
}
