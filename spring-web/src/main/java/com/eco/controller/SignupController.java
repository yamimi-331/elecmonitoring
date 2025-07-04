package com.eco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.UserVO;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/signup")
public class SignupController {

	private UserService userService;

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
	public String register(UserVO userVO, RedirectAttributes rttr) {
	    log.info("회원가입 처리");
	    try {
	        int result = userService.register(userVO);
	        if (result > 0) {
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

}
