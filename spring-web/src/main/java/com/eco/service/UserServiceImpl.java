package com.eco.service;

import org.springframework.stereotype.Service;

import com.eco.domain.UserVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    // 일반 사용자 로그인
    @Override
    public UserVO login(String user_id, String user_pw) {
        try {
            return userMapper.selectUserByIdAndPw(user_id, user_pw);
        } catch (Exception e) {
            throw new ServiceException("사용자 로그인 실패", e);
        }
    }

    // 아이디 중복 확인
    @Override
    public UserVO checkId(String user_id) {
        try {
            return userMapper.selectUserById(user_id);
        } catch (Exception e) {
            throw new ServiceException("아이디 중복 확인 실패", e);
        }
    }

    // 회원가입
    @Override
    public int register(UserVO userVO) {
        try {
            return userMapper.insertUser(userVO);
        } catch (Exception e) {
            throw new ServiceException("회원가입 실패", e);
        }
    }

    // 회원 정보 수정
    @Override
    public boolean modify(UserVO userVO) {
        try {
            int result = userMapper.updateUser(userVO);
            return result > 0;
        } catch (Exception e) {
            throw new ServiceException("회원 정보 수정 실패", e);
        }
    }
}
