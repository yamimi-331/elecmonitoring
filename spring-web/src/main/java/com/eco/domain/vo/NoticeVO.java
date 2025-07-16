package com.eco.domain.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoticeVO {
	private int notice_cd;   			// 공지사항 번호
	private String title;    			// 공지사항 제목
	private String content;  			// 공지사항 내용
	private LocalDateTime create_dt;  	// 공지사항 등록일
	private LocalDateTime update_dt;  	// 공지사항 수정일자
	private int staff_cd;				// 직원 코드값
	private String allow_range;			// 열람 범위
	private char use_yn;     			// 사용 유무
}
