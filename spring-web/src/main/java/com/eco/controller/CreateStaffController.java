package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.StaffService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/createStaff")
public class CreateStaffController {
	private final StaffService staffService;
	
	// 직원 관리자 계정 생성 페이지
	@GetMapping("")
	public String newStaffPage(HttpSession session, RedirectAttributes redirectAttrs) {
		Object currentUser = session.getAttribute("currentUserInfo");
		// 비정상적 루트로 접근 제한
		boolean accessAllow = false;
		if (currentUser instanceof UserVO) {
			accessAllow = false;
		} else if(currentUser instanceof StaffVO) {
        	 StaffVO vo = (StaffVO) currentUser;
        	 if("admin".equals(vo.getStaff_role())) {
        		 accessAllow = true;
        	 }else {
        		 accessAllow = false;
        	 }
         } else {
        	 accessAllow = false;
         }
         
         if (accessAllow) {
        	 log.info("직원 관리자 계정 생성 페이지 접속");
        	 return "admin/createStaff";
         } else {
        	 redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	 return "redirect:/";
         }
	}
	
	// 아이디 중복체크(GET /signup/checkId?user_id=xxx)
	@GetMapping(value = "/checkId", produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> checkId(StaffVO vo) {
		log.info("아이디 중복 체크 요청: " +  vo.getStaff_id());
		StaffVO staff = staffService.checkId(vo.getStaff_id());
		if (staff == null) {
			return ResponseEntity.ok("사용가능");
		} else {
			return ResponseEntity.ok("이미 사용 중인 아이디입니다.");
		}
	}
	
	// 직원 계정 생성
	@PostMapping("/staff")
	public String registerNewStaff(StaffVO vo, RedirectAttributes redirectAttrs) {
		log.info("직원 생성 요청");
		boolean result = staffService.register(vo);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "직원 계정이 생성되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "계정 생성 실패");
		}
		return "redirect:/createStaff";
	}
	
	
	// 관리자 계정 생성
	@PostMapping("/admin")
	public String guestLogin(StaffVO vo, RedirectAttributes redirectAttrs) {
		log.info("직원 생성 요청");
		boolean result = staffService.register(vo);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "관리자 계정이 생성되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "계정 생성 실패");
		}
		return "redirect:/createStaff";
	}
	
}
