package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.vo.UserVO;
import com.eco.service.MailService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/signup")
public class SignupController {

	private UserService userService;
	// 메일 본인인증 코드 발송 서비스
    private final MailService mailService;

	// 회원가입 폼 페이지 보여주기(GET /signup)
	@GetMapping("")
	public String signupPage() {
		log.info("회원가입 페이지 요청");
		return "user/signup";
	}

	// 아이디 중복체크(GET /signup/checkId?user_id=xxx)
	@GetMapping(value = "/checkId", produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> checkId(UserVO vo) {
		log.info("아이디 중복 체크 요청: " +  vo.getUser_id());
		UserVO user = userService.checkId(vo);
		if (user == null) {
			return ResponseEntity.ok("사용 가능");
		} else {
			return ResponseEntity.ok("이미 사용 중인 아이디입니다.");
		}
	}

	// 회원가입 처리(POST /signup)
	@PostMapping("")
	public String register(UserVO userVO, HttpSession session, RedirectAttributes rttr) {
	    log.info("회원가입 처리");
	    
	    //이메일 인증 여부
	    Boolean isVerified = (Boolean) session.getAttribute("signupEmailVerified");
	    if(isVerified == null || !isVerified) {
	    	rttr.addFlashAttribute("message", "이메일 인증을 완료해주세요.");
	    	return "redirect:/signup";
	    }
	    
	    try {
	    	userVO.setUser_social("Basic");
	        int result = userService.register(userVO);
	        if (result > 0) {
	        	// 인증 정보 삭제
	        	session.removeAttribute("signupEmailVerified");
	            session.removeAttribute("signupEmailAuthCode");
	            
	            rttr.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.");
	            return "redirect:/login";
	        } else {
	            rttr.addFlashAttribute("message", "회원가입에 실패했습니다. 다시 시도해주세요.");
	            return "redirect:/signup";  // 실패 시 다시 회원가입 페이지로
	        }
	    } catch (Exception e) {
	        log.error("회원가입 중 오류 발생", e);
	        rttr.addFlashAttribute("message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
	        return "redirect:/signup";
	    }
	}
	
	// 메일 인증 로직
	@PostMapping("/send-code")
	public ResponseEntity<String> sendCode(@RequestParam("user_mail") String email, HttpSession session) {
		String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
		mailService.sendAuthCode(email, authCode);

		session.setAttribute("signupEmailAuthCode", authCode);
		session.setMaxInactiveInterval(5 * 60);

		return ResponseEntity.ok("인증코드 발송 완료");
	}

	@PostMapping(value = "/verify-code", produces = "text/plain; charset=UTF-8")
	public ResponseEntity<String> verifyCode(@RequestParam String signup_code, HttpSession session) {
		String savedCode = (String) session.getAttribute("signupEmailAuthCode");

		if (signup_code == null || signup_code.trim().isEmpty() || !signup_code.equals(savedCode)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증코드가 일치하지 않습니다.");
		}
		session.setAttribute("signupEmailVerified", true);
		return ResponseEntity.ok("인증코드 확인 완료");
	}
	

}
