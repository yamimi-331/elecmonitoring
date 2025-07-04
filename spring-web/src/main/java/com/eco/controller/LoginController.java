package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eco.domain.GuestDTO;
import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/login")
public class LoginController {

	@GetMapping("")
	public String loginPage() {
		return "user/login";
	}

	@PostMapping("/user")
	public String userLogin(UserVO vo) {
		// TODO: 일반 사용자 로그인 로직
		// 예: 아이디 비번 DB에서 확인
		return "redirect:/index";
	}

	@PostMapping("/staff")
	public String staffLogin(StaffVO vo) {
		// TODO: 직원 로그인 로직
		// 예: 직원 ID/PW 인증 후 권한 체크
		return "redirect:/index";
	}

	@PostMapping("/guest")
	public String guestLogin(GuestDTO dto) {
		// TODO: 비회원 인증 로직
		// 예: 이메일, 이름 확인 후 세션 저장
		return "redirect:/index";
	}
}
