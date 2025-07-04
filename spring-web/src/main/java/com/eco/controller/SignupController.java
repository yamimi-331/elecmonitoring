package com.eco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public ResponseEntity<String> checkId(@RequestParam("user_id") String userId) {
		log.info("아이디 중복 체크 요청: " + userId);
		UserVO user = userService.checkId(userId);
		if (user == null) {
			return ResponseEntity.ok("사용 가능");
		} else {
			return ResponseEntity.ok("이미 사용 중인 아이디입니다.");
		}
	}

	// 회원가입 처리(POST /signup)
	@PostMapping("")
	@ResponseBody
	public ResponseEntity<String> register(UserVO userVO) {
		log.info("회원가입 처리: " + userVO);
		try {
			int result = userService.register(userVO);
			if (result > 0) {
				return ResponseEntity.ok("success");
			} else {
				return ResponseEntity.ok("fail");
			}
		} catch (Exception e) {
			log.error("회원가입 중 오류 발생", e);
			return ResponseEntity.ok("fail");
		}
	}
}
