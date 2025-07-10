package com.eco.domain.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoticeVO {

	private int notice_cd;   // PK, auto_increment
	private String title;    // VARCHAR(200)
	private String content;  // TEXT
	private LocalDateTime create_dt;  // DATETIME, 기본값 CURRENT_TIMESTAMP
	private LocalDateTime update_dt;  // DATETIME, NULL 허용
	private String user_nm;  // VARCHAR(100)
	private char use_yn;     // CHAR(1), 'Y' 또는 'N"
}
