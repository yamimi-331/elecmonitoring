package com.eco.domain.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InquiryVO {
	private int inquiry_cd;
	private int user_cd;
	private String inquiry_status;
	private String inquiry_title;
	private String inquiry_content;
	private LocalDateTime created_dt;
	private LocalDateTime update_dt;
	private char secret_yn;
	private char use_yn;
}
