package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eco.domain.vo.UserVO;

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
    
    // 회원 탈퇴
    public int deleteUser(UserVO userVO);
    
    // 계정 복구를 위한 검색
    public List<UserVO> selectUserForRecover(UserVO userVO);
    
    // 사용자 계정 복구
    public int updateRecoverUser(UserVO userVO);
    
	// 페이징을 위한 새로운 메서드 (비활성 일반 사용자)
	public List<UserVO> selectUserForRecoverPaged(@Param("userId") String userId, @Param("offset") int offset,
			@Param("limit") int limit);

	public long countUserForRecover(@Param("userId") String userId);

	public UserVO selectUserByCd(UserVO inputVO);

}
