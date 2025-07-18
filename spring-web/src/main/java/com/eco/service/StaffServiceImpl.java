package com.eco.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eco.domain.DTO.StaffPageResponseDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.StaffMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffMapper staffMapper;
    // 비밀번호 암호화
    private BCryptPasswordEncoder passwordEncoder;

    // 직원 로그인
    @Override
    public StaffVO login(StaffVO inputStaff) {
        try {
        	   // DB에서 해당 아이디로 사용자 정보 조회
             StaffVO dbstaff = staffMapper.selectStaffById(inputStaff.getStaff_id());
             // 조회된 사용자 있고, 입력한 PW가 암호화 PW와 일치하면
             if (dbstaff != null && passwordEncoder.matches(inputStaff.getStaff_pw(), dbstaff.getStaff_pw())) {
             	return dbstaff; // 로그인 성공
             }
             return null; // 로그인 실패
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
        	// 비밀번호 암호화
            String encodedPw = passwordEncoder.encode(staffVO.getStaff_pw());
            staffVO.setStaff_pw(encodedPw);
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
        	// 비밀번호가 null이 아니고 빈 값이 아니라면 -> 암호화해서 덮어씀
            if (staffVO.getStaff_pw() != null && !staffVO.getStaff_pw().isEmpty()) {
                String encodedPw = passwordEncoder.encode(staffVO.getStaff_pw());
                staffVO.setStaff_pw(encodedPw);
            }
            
            int result = staffMapper.updateStaff(staffVO);
            return result > 0;
        } catch (Exception e) {
            throw new ServiceException("직원 회원 정보 수정 실패", e);
        }
    }
    
    // 회원 탈퇴
    @Override
    public boolean deleteAccount(StaffVO staffVO) {
    	try {
    		int result = staffMapper.deleteStaff(staffVO);
    		return result>0;
    	} catch(Exception e) {
    		throw new ServiceException("직원 회원 탈퇴 실패", e);
    	}
    }
    
    // 비밀번호 일치 확인
    @Override
    public boolean checkPassword(String rawPw, String encodedPw) {
        try {
            return passwordEncoder.matches(rawPw, encodedPw);
        } catch (Exception e) {
            throw new ServiceException("직원 비밀번호 확인 실패", e);
        }
    }

    // 직원 지역 배정을 위한 검색, 조회
	@Override
	public List<StaffVO> getStaffList(StaffVO staffVO) {
		try {
			return staffMapper.selectStaffByIdAndUseyn(staffVO);
        } catch (Exception e) {
            throw new ServiceException("직원 계정 조회 실패", e);
        }
	}

	// 직원 계정 복구를 위한 검색, 조회
	@Override
	public List<StaffVO> getStaffForRecover(StaffVO staffVO) {
		try {
			return staffMapper.selectStaffForRecover(staffVO);
        } catch (Exception e) {
            throw new ServiceException("비활성화 직원 조회 실패", e);
        }
	}
	
	// 페이징을 위한 새로운 서비스 메서드 (활성 직원)
    @Override 
    public StaffPageResponseDTO getStaffListPaged(String staffId, int page, int size) {
        try {
            int offset = page * size; // 0-based 페이지 번호이므로 page * size
            long totalElements = staffMapper.countStaffByIdAndUseyn(staffId);
            List<StaffVO> content = staffMapper.selectStaffByIdAndUseynPaged(staffId, offset, size);

            StaffPageResponseDTO response = new StaffPageResponseDTO();
            response.setContent(content);
            response.setTotalElements(totalElements);
            response.setTotalPages((int) Math.ceil((double) totalElements / size));
            response.setCurrentPage(page); // 0-based 페이지 번호
            response.setPageSize(size);
            return response;
        } catch (Exception e) {
            throw new ServiceException("페이징된 직원 계정 조회 실패", e);
        }
    }

    // 페이징을 위한 새로운 서비스 메서드 (비활성 직원)
    @Override 
    public StaffPageResponseDTO getStaffForRecoverPaged(String staffId, int page, int size) {
        try {
            int offset = page * size;
            long totalElements = staffMapper.countStaffForRecover(staffId);
            List<StaffVO> content = staffMapper.selectStaffForRecoverPaged(staffId, offset, size);

            StaffPageResponseDTO response = new StaffPageResponseDTO();
            response.setContent(content);
            response.setTotalElements(totalElements);
            response.setTotalPages((int) Math.ceil((double) totalElements / size));
            response.setCurrentPage(page);
            response.setPageSize(size);
            return response;
        } catch (Exception e) {
            throw new ServiceException("페이징된 비활성화 직원 계정 조회 실패", e);
        }
    }
    
	// 직원 배정 지역 변경
	@Override
	public boolean modifyRegion(StaffVO staffVO) {
		try {
			// 여기서 "-" 들어오면 null로 변환
	        if ("-".equals(staffVO.getStaff_addr())) {
	            staffVO.setStaff_addr(null);
	        }
	        
            int result = staffMapper.updateRegionStaff(staffVO);
            return result>0;
        } catch (Exception e) {
            throw new ServiceException("직원 배정 지역 권한 변경 실패", e);
        }
	}

	// 직원 계정 복구
	@Override
	public boolean recoverAccount(StaffVO staffVO) {
		try {
            int result = staffMapper.updateRecoverStaff(staffVO);
            return result>0;
        } catch (Exception e) {
            throw new ServiceException("직원 계정 복구 실패", e);
        }
	}

}
