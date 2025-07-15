package com.eco.domain.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportVO {
	private int report_cd;
	private int staff_cd;
	private String title;
	private String type;
	private String content;
	private String local;
	private String phone;
	private LocalDateTime report_dt;
	private LocalDateTime update_dt;
	private char use_yn;
}
