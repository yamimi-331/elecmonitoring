package com.eco.service;

import org.springframework.stereotype.Service;

import com.eco.domain.UserVO;
import com.eco.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;

	// 일반 사용자 로그인
	@Override
	public UserVO login(String user_id, String user_pw) {
		return userMapper.selectUserByIdAndPw(user_id, user_pw);
	}
	// 아이디 중복 확인
	@Override
	public UserVO checkId(String user_id) {
		return userMapper.selectUserById(user_id);
	}
	// 회원가입
	@Override
	public int register(UserVO userVO) {
		return userMapper.insertUser(userVO);
	}
	
	// 직원 정보 수정
	@Override
	public boolean modify(UserVO userVO) {
		int result = userMapper.updateUser(userVO);
		return result > 0;
	}
}
