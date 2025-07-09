package com.eco.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;
	private final StaffService staffService;

	@GetMapping("/account")
	public String adminPage() {
		log.info("관리자 페이지 이동");
		return "/admin/account";
	}
	
	//이름으로 직원 정보 조회
	@GetMapping("/search-users")
	@ResponseBody
	public List<StaffVO> searchStaffbyName(@RequestParam("staffId") String staff_id) {
		StaffVO staffVO = new StaffVO();
		staffVO.setStaff_id(staff_id);
		return staffService.getStaffList(staffVO);
	}
	
	// 비활성화 계정 조회
	@GetMapping("/search-deleted-users")
	@ResponseBody
	public List<?> searchDeleteUsers(@RequestParam("userType") String userType, @RequestParam("id") String id) {
		if ("common".equals(userType)) {
			UserVO userVO = new UserVO();
			userVO.setUser_id(id);
			List<UserVO> result = userService.getUserForRecover(userVO);
			return result;	
		} else if("staff".equals(userType)) {
			StaffVO staffVO = new StaffVO();
			staffVO.setStaff_id(id);
			List<StaffVO> result = staffService.getStaffForRecover(staffVO);
			return result;
		} else {
			return null;
		}
	}
	
	// 계정 복구 버튼
	@PostMapping("/restore-account")
	@ResponseBody
	public void restoreAccount(@RequestParam("userType") String userType, @RequestParam("id") String id) {
		if ("common".equals(userType)) {
			UserVO userVO = new UserVO();
			userVO.setUser_id(id);
			userService.recoverAccount(userVO);
		} else if("staff".equals(userType)) {
			StaffVO staffVO = new StaffVO();
			staffVO.setStaff_id(id);
			staffService.recoverAccount(staffVO);
		} else {
			log.info("복구할 계정의 타입 오류 발생");
		}
	}
	
	// 직원 정보 수정
	@PostMapping("/update-staff")
	@ResponseBody
	public void updateStaff(StaffVO staffVO) {
		boolean result = false;
		result = staffService.modifyRegion(staffVO);
		if(!result) {
			log.info("직원 정보 수정 중 오류 발생");
		}
	}
}
