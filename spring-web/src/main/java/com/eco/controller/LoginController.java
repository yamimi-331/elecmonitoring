package com.eco.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.GuestDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.OAuthService;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {
	// 사용자 서비스
	private final UserService userService;
	// 직원 서비스
	private final StaffService staffService;
	// OAuth2 인증 서비스
	private final OAuthService oAuthService;

	// 로그인 페이지로 이동
	@GetMapping("")
	public String loginPage() {
		log.info("로그인 페이지로 이동");
		return "user/login";
	}

	// 일반 사용자 로그인
	@PostMapping("/user")
	public String userLogin(UserVO vo, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("일반 사용자 로그인 시도");

		UserVO loginUser = userService.login(vo);

		if (loginUser != null) {
			log.info("로그인 성공");
			// 세션에 사용자 정보 저장
			session.setAttribute("currentUserInfo", loginUser);
			session.setAttribute("userType", "common");
			return "redirect:/";
		} else {
			log.info("로그인 실패");
			redirectAttrs.addFlashAttribute("message", "아이디 및 비밀번호가 틀립니다.");
			return "redirect:/login";
		}
	}

	// 직원 로그인
	@PostMapping("/staff")
	public String staffLogin(StaffVO vo, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("직원 로그인 시도");

		StaffVO loginStaff = staffService.login(vo);

		if (loginStaff != null) {
			log.info("로그인 성공");
			session.setAttribute("currentUserInfo", loginStaff);
			session.setAttribute("userType", loginStaff.getStaff_role());
			return "redirect:/";
		} else {
			log.info("로그인 실패");
			redirectAttrs.addFlashAttribute("message", "아이디 및 비밀번호가 틀립니다.");
			return "redirect:/login?error=fail";
		}
	}

	@PostMapping("/guest")
	public String guestLogin(GuestDTO dto) {
		// TODO: 비회원 인증 로직
		// 예: 이메일, 이름 확인 후 세션 저장
		return "redirect:/";
	}

	// # 구글 로그인 start --------------------------------------------------------
	// 1. 사용자 로그인 URL을 반환
	@GetMapping("/googleLogin")
	public void googleLogin(HttpServletResponse response) throws IOException {
		response.sendRedirect(oAuthService.getGoogleLoginUrl());
	}

	// 2. 예외 처리
	@GetMapping("/oauth/google/callback")
	public String oauth2Callback(@RequestParam("code") String code, HttpSession session, Model model)
			throws IOException {
		UserVO user = oAuthService.processGoogleLogin(code);

		if (user == null) {
			// 로그인 실패 처리
			model.addAttribute("message", "구글 로그인 실패");
			return "redirect:/login";
		}
		// 세션에 사용자 정보 저장
		session.setAttribute("currentUserInfo", user);
		session.setAttribute("userType", "common");
		return "redirect:/";
	}
	// # 구글 로그인 End --------------------------------------------------------
	// # Naver Login Start ---------------------------------------------------
	// 네이버 로그인 페이지로 리다이렉트
	@GetMapping("/naverLogin")
	public void naverLogin(HttpServletResponse response) throws IOException {
		// 서비스에서 네이버 로그인 URL 생성
		String naverAuthUrl = oAuthService.getNaverLoginUrl();
		response.sendRedirect(naverAuthUrl);
	}

	// 2. 콜백 처리 - code와 state 받음
	@GetMapping("/oauth2/callback/naver")
	public String naverCallback(@RequestParam("code") String code, @RequestParam("state") String state,
			HttpSession session, Model model) throws IOException {
		// 서비스에 로그인 처리 위임
		UserVO user = oAuthService.processNaverLogin(code, state);

		if (user == null) {
			// 로그인 실패 처리
			model.addAttribute("message", "네이버 로그인 실패");
			return "redirect:/login";
		}

		// 세션에 사용자 정보 저장
		session.setAttribute("currentUserInfo", user);
		session.setAttribute("userType", "common");

		return "redirect:/";
	}
	// # Naver Login End ---------------------------------------------------
}
