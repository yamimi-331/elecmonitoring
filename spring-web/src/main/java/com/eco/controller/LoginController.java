package com.eco.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.eco.service.MailService;
import com.eco.service.OAuthService;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

	// 사용자 서비스
	private final UserService userService;
	// 직원 서비스
	private final StaffService staffService;
	// OAuth2 인증 서비스
	private final OAuthService oAuthService;
	// 메일 본인인증 코드 발송 서비스
    private final MailService mailService;

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
			// 세션 타임아웃 설정 (예: 30분)
		    session.setMaxInactiveInterval(30 * 60);
			return "redirect:/";
		} else {
			log.info("로그인 실패");
			redirectAttrs.addFlashAttribute("message", "아이디 및 비밀번호가 틀립니다.");
			return "redirect:/login";
		}
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
		// 세션 타임아웃 설정 (예: 30분)
	    session.setMaxInactiveInterval(30 * 60);
	    
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
		// 세션 타임아웃 설정 (예: 30분)
	    session.setMaxInactiveInterval(30 * 60);

		return "redirect:/";
	}
	// # Naver Login End ---------------------------------------------------

	// # kakao Login Start ---------------------------------------------------
	@GetMapping("/kakaoLogin")
	public void kakaoLogin(HttpServletResponse response) throws IOException {
	    String kakaoAuthUrl = oAuthService.getKakaoLoginUrl();
	    response.sendRedirect(kakaoAuthUrl);
	}

	@GetMapping("/oauth2/callback/kakao")
	public String kakaoCallback(@RequestParam("code") String code,
			@RequestParam(value = "state", required = false) String state, HttpSession session, Model model) {
		// 카카오에서 access token 받고 사용자 정보 받아오기
		UserVO user = oAuthService.processKakaoLogin(code);

		if (user == null) {
			// 로그인 실패 처리
			model.addAttribute("message", "카카오 로그인 실패");
			return "redirect:/login";
		}
		// 세션에 로그인 정보 저장
		session.setAttribute("currentUserInfo", user);
		session.setAttribute("userType", "common");
		// 세션 타임아웃 설정 (예: 30분)
	    session.setMaxInactiveInterval(30 * 60);

		// 원하는 리다이렉트 페이지로 이동
		return "redirect:/";
	}
	// # kakao Login End ---------------------------------------------------
	
	// 직원 로그인
	@PostMapping("/staff")
	public String staffLogin(StaffVO vo, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("직원 로그인 시도");

		StaffVO loginStaff = staffService.login(vo);

		if (loginStaff != null) {
			log.info("로그인 성공");
			session.setAttribute("currentUserInfo", loginStaff);
			session.setAttribute("userType", loginStaff.getStaff_role());
			// 세션 타임아웃 설정 (예: 30분)
		    session.setMaxInactiveInterval(30 * 60);
			return "redirect:/";
		} else {
			log.info("로그인 실패");
			redirectAttrs.addFlashAttribute("message", "아이디 및 비밀번호가 틀립니다.");
			return "redirect:/login?error=fail";
		}
	}

	@PostMapping("/guest")
	public String guestLogin(GuestDTO dto, HttpSession session) {
	    // 1. 세션에 저장된 인증코드 꺼내기
	    String savedCode = (String) session.getAttribute("guestEmailAuthCode");
	    
	    // 2. 요청 dto의 인증코드와 비교
	    if (dto.getGuest_code() == null || !dto.getGuest_code().equals(savedCode)) {
	        // 인증 실패 시 다시 인증페이지로 리다이렉트 또는 에러 메시지
	        return "redirect:/guest/auth?error=invalid_code";
	    }

	    // 3. 인증 성공 → GuestDTO 세션에 저장 (로그인 처리)
	    session.setAttribute("currentUserInfo", dto);
	    session.setAttribute("userType", "guest");

	    // 4. 인증코드는 사용 후 삭제
	    session.removeAttribute("guestEmailAuthCode");

	    // 5. 세션 타임아웃 설정 (예: 30분)
	    session.setMaxInactiveInterval(30 * 60);

	    return "redirect:/";  // 메인페이지로 이동
	}
	
	// 메일 인증 로직
	@PostMapping("/guest/send-code")
	public ResponseEntity<String> sendCode(@RequestParam("guest_mail") String email, HttpSession session) {
		String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
		mailService.sendAuthCode(email, authCode);

		session.setAttribute("guestEmailAuthCode", authCode);
		session.setMaxInactiveInterval(5 * 60);

		return ResponseEntity.ok("인증코드 발송 완료");
	}

	@PostMapping(value = "/guest/verify-code", produces = "text/plain; charset=UTF-8")
	public ResponseEntity<String> verifyCode(@RequestParam String guest_code, HttpSession session) {
		String savedCode = (String) session.getAttribute("guestEmailAuthCode");

		if (guest_code == null || !guest_code.equals(savedCode)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증코드가 일치하지 않습니다.");
		}
		return ResponseEntity.ok("인증코드 확인 완료");
	}

	
}
