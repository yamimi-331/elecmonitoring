package com.eco.service;

import java.util.List;

import com.eco.domain.UserVO;

public interface UserService {
   // 일반 사용자 로그인
   public UserVO login(UserVO inputUser);

   // 아이디 중복 확인
   public UserVO checkId(UserVO inputUser);

   // 회원가입
   public int register(UserVO userVO);
   
   // 회원 정보 수정
   public boolean modify(UserVO userVO);
   
   // 회원 탈퇴
   public boolean deleteAccount(UserVO userVO);
   
   // 비밀 번호 확인 
   public boolean checkPassword(String rawPw, String encodedPw);
   
   // 계정 복구를 위한 검색
   public List<UserVO> selectUserForRecover(UserVO userVO);
   
   // 사용자 계정 복구
   public boolean recoverAccount(UserVO userVO);
}
