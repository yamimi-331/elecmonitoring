package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eco.domain.GuestDTO;
import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

	@GetMapping("")
	public String loginPage() {
		return "user/login";
	}

	@PostMapping("/user")
	public String userLogin(UserVO vo, HttpSession session) {
		log.info("일반 사용자 로그인 시도: " + vo);

        UserVO loginUser = userService.login(vo.getUser_id(), vo.getUser_pw());

        if (loginUser != null) {
            log.info("로그인 성공: " + loginUser.getUser_id());
            // 세션에 사용자 정보 저장
            session.setAttribute("currentUserInfo", loginUser);
            return "redirect:/index";
        } else {
            log.info("로그인 실패");
            return "redirect:/login?error=fail";
        }
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
