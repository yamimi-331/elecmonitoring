package com.eco.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eco.domain.UserVO;

@Mapper
public interface UserMapper {
	// 일반 사용자 로그인
    public UserVO selectUserByIdAndPw(@Param("user_id") String user_id, @Param("user_pw") String user_pw);

    // 아이디 중복 확인
    public UserVO selectUserById(UserVO userVO);

    // 회원가입
    public int insertUser(UserVO userVO);
    
    // 회원 정보 수정
    public int updateUser(UserVO userVO);

}
