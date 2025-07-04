package com.eco.service;

import com.eco.domain.StaffVO;

public interface StaffService {
	// 직원 로그인
   public StaffVO login(String staff_id, String staff_pw);

   // 아이디 중복 확인
   public StaffVO checkId(String staff_id);

   // 직원 계정 생성
   public boolean register(StaffVO staffVO);
   
   // 직원 정보 수정
   public boolean modify(StaffVO staffVO);
}
