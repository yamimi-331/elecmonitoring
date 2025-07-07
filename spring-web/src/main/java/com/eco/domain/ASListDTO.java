package com.eco.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ASListDTO {
	 // AS 기본 정보
    private int as_cd;              // AS 일정 코드
    private String as_title;        // 문제 종류 (신고 제목)
    private String as_content;      // 상세 정보
    private LocalDateTime as_date;  // 예약 일시
    private String as_addr;         // 위치
    private String as_facility;     // 시설물
    private String as_status;       // 진행 상태

    // 사용자 정보
    private int user_cd;
    private String user_id;         // 계정 ID
    private String user_nm;         // 사용자 이름
    private String user_mail;       // 사용자 메일
    private String user_addr;       // 사용자 주소
    private String user_social;     // 소셜 타입

    // 직원 정보
    private int staff_cd;
    private String staff_id;        // 직원 ID
    private String staff_nm;        // 담당자 이름
    private String staff_addr;      // 직원 주소
    private String staff_role;      // 직원 역할

    // 비회원 정보
    private String guest_mail;      // 비회원 메일
    private String guest_nm;        // 비회원 이름

    private String as_time; // 가공된 시간만 저장
    private char use_yn;            // 사용 여부
}
