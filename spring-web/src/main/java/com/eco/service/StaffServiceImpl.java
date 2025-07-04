package com.eco.service;

import org.springframework.stereotype.Service;

import com.eco.domain.StaffVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.StaffMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffMapper staffMapper;

    // 직원 로그인
    @Override
    public StaffVO login(String staff_id, String staff_pw) {
        try {
            return staffMapper.selectStaffByIdAndPw(staff_id, staff_pw);
        } catch (Exception e) {
            throw new ServiceException("직원 로그인 실패", e);
        }
    }

    // 아이디 중복 확인
    @Override
    public StaffVO checkId(String staff_id) {
        try {
            return staffMapper.selectStaffById(staff_id);
        } catch (Exception e) {
            throw new ServiceException("직원 아이디 중복 확인 실패", e);
        }
    }

    // 직원 계정 생성
    @Override
    public boolean register(StaffVO staffVO) {
        try {
            int result = staffMapper.insertStaff(staffVO);
            return result > 0;
        } catch (Exception e) {
            throw new ServiceException("직원 계정 생성 실패", e);
        }
    }

    // 직원 정보 수정
    @Override
    public boolean modify(StaffVO staffVO) {
        try {
            int result = staffMapper.updateStaff(staffVO);
            return result > 0;
        } catch (Exception e) {
            throw new ServiceException("직원 정보 수정 실패", e);
        }
    }
}
