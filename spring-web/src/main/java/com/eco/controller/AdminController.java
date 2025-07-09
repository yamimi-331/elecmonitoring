package com.eco.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.domain.StaffVO;
import com.eco.service.StaffService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final StaffService staffService;

	@GetMapping("/account")
	public String adminPage() {
		log.info("관리자 페이지 이동");
		return "/admin/account";
	}
	
	//이름으로 직원 정보 조회
	@GetMapping("/search-users")
	@ResponseBody
	public List<StaffVO> searchStaffbyName(@RequestParam String staff_nm) {
		StaffVO staffvo = new StaffVO();
		staffvo.setStaff_nm(staff_nm);
		return staffService.getStaffList(staffvo);
	}
	
//	// 비활성화 계정 조회
//	@GetMapping("/noUseAccount")
//	@ResponseBody
//	public List<> getScheduleByDate() {
//
//	}
}
