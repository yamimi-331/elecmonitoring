package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eco.domain.vo.StaffVO;

@Mapper
public interface StaffMapper {
	// 직원 로그인
    public StaffVO selectStaffByIdAndPw(@Param("staff_id") String staff_id, @Param("staff_pw") String staff_pw);
    
    // 아이디 중복 조회
    public StaffVO selectStaffById(@Param("staff_id") String staff_id);
    
    // 직원 계정 생성
    public int insertStaff(StaffVO staffVO);
    
    // 직원 정보 수정
    public int updateStaff(StaffVO staffVO);
    
    // 회원 탈퇴
    public int deleteStaff(StaffVO staffVO);
    
    // 직원 지역 배정을 위한 검색, 조회
    public List<StaffVO> selectStaffByIdAndUseyn(StaffVO staffVO);
    
    // 직원 계정 복구를 위한 검색, 조회
    public List<StaffVO> selectStaffForRecover(StaffVO staffVO);
    
	// 페이징을 위한 새로운 메서드 (활성 직원)
	public List<StaffVO> selectStaffByIdAndUseynPaged(@Param("staffId") String staffId, @Param("offset") int offset,
			@Param("limit") int limit);

	public long countStaffByIdAndUseyn(@Param("staffId") String staffId);

	// 페이징을 위한 새로운 메서드 (비활성 직원)
	public List<StaffVO> selectStaffForRecoverPaged(@Param("staffId") String staffId, @Param("offset") int offset,
			@Param("limit") int limit);

	public long countStaffForRecover(@Param("staffId") String staffId);
    
    // 직원 배정 지역 변경
    public int updateRegionStaff(StaffVO staffVO);
    
    // 직원 계정 복구
    public int updateRecoverStaff(StaffVO staffVO);
}
