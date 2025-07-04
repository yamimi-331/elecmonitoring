package com.eco.controller;

import javax.servlet.http.HttpSession; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.GuestDTO;
import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final StaffService staffService;

	@GetMapping("")
	public String loginPage() {
		return "user/login";
	}

	@PostMapping("/user")
	public String userLogin(UserVO vo, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("일반 사용자 로그인 시도: " + vo);

        UserVO loginUser = userService.login(vo);

        if (loginUser != null) {
            log.info("로그인 성공: " + loginUser.getUser_id());
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

	@PostMapping("/staff")
	public String staffLogin(StaffVO vo, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("직원 로그인 시도: " + vo);

        StaffVO loginStaff = staffService.login(vo.getStaff_id(), vo.getStaff_pw());

        if (loginStaff != null) {
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
}
