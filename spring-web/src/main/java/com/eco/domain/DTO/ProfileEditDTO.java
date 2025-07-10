package com.eco.domain.DTO;

import lombok.Data;

@Data
public class ProfileEditDTO {
	
	//공통 프로필
	private String id;		// 계정 ID
	private String pw;		// 계정 비밀번호
	private String nm;		// 계정 이름
	private String addr;	// 계정 주소
	private String mail;	// 계정 메일
	
	//일반 사용자 프로필
	private String user_social;	// 일반, 구글, 네이버, 카카오
	//직원 프로필
	private String staff_role; 	// 직원 역할 
	
	
}
