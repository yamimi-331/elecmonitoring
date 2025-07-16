package com.eco.domain.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InquiryDTO {
	private int inquiry_cd;				// 문의게시판 글 번호
	private int user_cd;				// 사용자 코드값
	private String user_nm;				// 사용자 이름
	private String inquiry_status;		// 문의글 상태
	private String inquiry_title;		// 문의글 제목
	private String inquiry_content;		// 문의글 내용
	private LocalDateTime created_dt;	// 문의 일자
	private LocalDateTime update_dt;	// 문의글 수정 일자
}
