package com.eco.service;

import org.springframework.stereotype.Service;

import com.eco.domain.StaffVO;
import com.eco.mapper.StaffMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService{
	private final StaffMapper staffMapper;
	
	// 직원 로그인
	@Override
	public StaffVO login(String staff_id, String staff_pw) {
		return staffMapper.selectStaffByIdAndPw(staff_id, staff_pw);
	}

	// 아이디 중복 확인
	@Override
	public StaffVO checkId(String staff_id) {
		return staffMapper.selectStaffById(staff_id);
	}
	
	// 직원 계정 생성
	@Override
	public boolean register(StaffVO staffVO) {
		int result = staffMapper.insertStaff(staffVO);
		return  result > 0;
	}

	// 직원 정보 수정
	@Override
	public boolean modify(StaffVO staffVO) {
		int result = staffMapper.updateStaff(staffVO);
		return result > 0;
	}
}
