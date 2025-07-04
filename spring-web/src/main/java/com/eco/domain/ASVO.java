package com.eco.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ASVO {
	private int as_cd;				// AS 일정 구분 CD값
	private int staff_cd;			// 직원 구분 CD값
	private int user_cd;			// 사용자 구분 CD값
	private String user_mail;		// 사용자 메일
	private String as_title; 		// 문제 종류
	private String as_content; 		// 상세 정보
	private LocalDateTime as_date; 	// 예약 일시
	private String as_addr;			// AS 대상 시설 위치
	private String as_facility; 	// 시설물 종류
	private String as_status; 		// 진행 상태
	private String guest_mail; 		// 비회원 로그인 메일
	private String guest_nm; 		// 비회원 이름
	private char use_yn;			// 사용 여부
}
