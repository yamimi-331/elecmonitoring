package com.eco.service;

import com.eco.domain.UserVO;

public interface UserService {
   // 일반 사용자 로그인
   public UserVO login(String user_id, String user_pw);

   // 아이디 중복 확인
   public UserVO checkId(String user_id);

   // 회원가입
   public int register(UserVO userVO);
   
   // 회원 정보 수정
   public boolean modify(UserVO userVO);
}
