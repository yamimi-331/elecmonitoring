package com.eco.service;

import java.util.List;

import com.eco.domain.StaffVO;

public interface StaffService {
	// 직원 로그인
   public StaffVO login(StaffVO inputStaff);

   // 아이디 중복 확인
   public StaffVO checkId(String staff_id);

   // 직원 계정 생성
   public boolean register(StaffVO staffVO);
   
   // 직원 정보 수정
   public boolean modify(StaffVO staffVO);
   
   // 회원 탈퇴
   public boolean deleteAccount(StaffVO staffVO);
   
   // 비밀 번호 확인 
   public boolean checkPassword(String rawPw, String encodedPw);
   
   // 직원 권한 변경 혹은 지역 배정을 위한 검색, 조회
   public List<StaffVO> getStaffList(StaffVO staffVO);
   
   // 직원 계정 복구를 위한 검색, 조회
   public List<StaffVO> getStaffForRecover(StaffVO staffVO);
   
   // 직원 배정 지역, 권한 변경
   public boolean modifyRegion(StaffVO staffVO);
   
   // 직원 계정 복구
   public boolean recoverAccount(StaffVO staffVO);
}
