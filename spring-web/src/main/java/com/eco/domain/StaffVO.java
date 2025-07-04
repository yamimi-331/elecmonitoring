package com.eco.domain;

import lombok.Data;

@Data
public class StaffVO {
	private int staff_cd;
	private String staff_id;
	private String staff_pw;
	private String staff_nm;
	private String staff_addr;
	private String staff_role;
	private char use_yn;
}
