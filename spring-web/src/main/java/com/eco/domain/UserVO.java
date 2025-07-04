package com.eco.domain;

import lombok.Data;

@Data
public class UserVO {
	private int user_cd; 		// 사용자 구분 CD값
	private String user_id;		// 계정 ID
	private String user_pw;		// 계정 비밀번호
	private String user_nm;		// 사용자 이름
	private String user_addr;	// 사용자 주소
	private String user_mail;	// 사용자 메일
	private String user_social;	// 일반, 구글, 네이버, 카카오
	private char use_yn;		// 계정 사용 여부
}
