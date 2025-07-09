package com.eco.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eco.domain.UserVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	// 사용자 매퍼
    private final UserMapper userMapper;
    // 비밀번호 암호화
    private BCryptPasswordEncoder passwordEncoder;
    
    // 일반 사용자 로그인
    @Override
    public UserVO login(UserVO inputUser) {
        try {
       	   // DB에서 해당 아이디로 사용자 정보 조회
            UserVO dbUser = userMapper.selectUserById(inputUser); // inputUser.user_id 사용
            // 조회된 사용자 있고, 입력한 PW가 암호화 PW와 일치하면
            if (dbUser != null && passwordEncoder.matches(inputUser.getUser_pw(), dbUser.getUser_pw())) {
            	return dbUser; // 로그인 성공
            }
            return null; // 로그인 실패
        } catch (Exception e) {
            throw new ServiceException("사용자 로그인 실패", e);
        }
    }

    // 아이디 중복 확인
    @Override
    public UserVO checkId(UserVO inputUser) {
        try {
            return userMapper.selectUserById(inputUser);
        } catch (Exception e) {
            throw new ServiceException("사용자 아이디 중복 확인 실패", e);
        }
    }

    // 회원가입
    @Override
    public int register(UserVO userVO) {
        try {
        	// 비밀번호 암호화
            String encodedPw = passwordEncoder.encode(userVO.getUser_pw());
            userVO.setUser_pw(encodedPw);
            return userMapper.insertUser(userVO);
        } catch (Exception e) {
            throw new ServiceException("사용자 회원가입 실패", e);
        }
    }

    // 회원 정보 수정
    @Override
    public boolean modify(UserVO userVO) {
        try {
        	// 비밀번호가 null이 아니고 빈 값이 아니라면 -> 암호화해서 덮어씀
            if (userVO.getUser_pw() != null && !userVO.getUser_pw().isEmpty()) {
                String encodedPw = passwordEncoder.encode(userVO.getUser_pw());
                userVO.setUser_pw(encodedPw);
            }
            
            int result = userMapper.updateUser(userVO);
            return result > 0;
        } catch (Exception e) {
            throw new ServiceException("사용자 회원 정보 수정 실패", e);
        }
    }
    
    // 회원 탈퇴
    @Override
    public boolean deleteAccount(UserVO userVO) {
    	try {
    		int result = userMapper.deleteUser(userVO);
    		return result>0;
    	} catch(Exception e) {
    		throw new ServiceException("사용자 회원 탈퇴 실패", e);
    	}
    }
    
    // 비밀번호 일치 확인
    @Override
    public boolean checkPassword(String rawPw, String encodedPw) {
        try {
            return passwordEncoder.matches(rawPw, encodedPw);
        } catch (Exception e) {
            throw new ServiceException("비밀번호 확인 실패", e);
        }
    }

    // 사용자 계정 복구를 위한 검색
	@Override
	public List<UserVO> selectUserForRecover(UserVO userVO) {
		return userMapper.selectUserForRecover(userVO);
	}

	// 사용자 계정 복구
	@Override
	public boolean recoverAccount(UserVO userVO) {
		try {
			int result = userMapper.updateRecoverUser(userVO);
    		return result>0;
        } catch (Exception e) {
            throw new ServiceException("사용자 계정 복구 실패", e);
        }
	}

	
}
