package com.eco.domain;

import lombok.Data;

@Data
public class StaffVO {
	private int staff_cd;		// 직원 구분 CD값
	private String staff_id;	// 직원 계정 ID
	private String staff_pw; 	// 직원 계정 PW
	private String staff_nm;  	// 직원 이름
	private String staff_addr;	// 직원 주소
	private String staff_role; 	// 직원 역할 
	private char use_yn;	 	// 계정 사용 여부
}
