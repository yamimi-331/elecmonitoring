package com.eco.domain.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportDTO {
	private int report_cd;				// 전기 재해 신고 번호
	private String staff_cd;			// 직원 코드값
	private String staff_nm;			// 직원 이름
	private String title;				// 신고 제목
	private String type;				// 신고 타입
	private String content;				// 신고 내용
	private String local;				// 신고 지역
	private String phone;				// 신고자 번호
	private LocalDateTime report_dt;	// 신고 일자
	private LocalDateTime update_dt;	// 신고글 수정 일자
}
