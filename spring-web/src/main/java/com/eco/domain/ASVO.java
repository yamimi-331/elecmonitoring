package com.eco.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ASVO {
	private int as_cd;
	private int staff_cd;
	private int user_cd;
	private String user_mail;
	private String as_title;
	private String as_content;
	private LocalDateTime as_date;
	private String as_addr;
	private String as_facility;
	private String as_status;
	private char use_yn;
}
