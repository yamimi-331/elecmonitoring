package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.ProfileEditDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/profileEdit")
public class ProfileEditController {
	private final UserService userService;
	private final StaffService staffService;

	//회원 정보 수정 페이지 접속
	@GetMapping("")
	public String profilePage(HttpSession session, Model model) {
		String type = (String) session.getAttribute("userType");
		ProfileEditDTO dto = new ProfileEditDTO();
		if ("common".equals(type)) {
			UserVO currentUser = (UserVO) session.getAttribute("currentUserInfo");
			dto.setId(currentUser.getUser_id());
			dto.setPw(currentUser.getUser_pw());
			dto.setNm(currentUser.getUser_nm());
			dto.setAddr(currentUser.getUser_addr());
			dto.setMail(currentUser.getUser_mail());
		} else if ("staff".equals(type) || "admin".equals(type)) {
			StaffVO currentUser = (StaffVO) session.getAttribute("currentUserInfo");
			dto.setId(currentUser.getStaff_id());
			dto.setPw(currentUser.getStaff_pw());
			dto.setNm(currentUser.getStaff_nm());
			dto.setAddr(currentUser.getStaff_addr());
			dto.setMail(currentUser.getStaff_role());
		}

		model.addAttribute("profileInfo", dto);

		return "user/profileEdit";
	}
	
	//회원 정보 수정
	@PostMapping("")
	public String profileEdit(ProfileEditDTO dto, HttpSession session, RedirectAttributes redirectAttrs) {
		String type = (String) session.getAttribute("userType");
		boolean result = false;
		if ("common".equals(type)) {
			UserVO vo = new UserVO();
			vo.setUser_id(dto.getId());
			vo.setUser_pw(dto.getPw());
			vo.setUser_nm(dto.getNm());
			vo.setUser_addr(dto.getAddr());
			vo.setUser_mail(dto.getMail());
			result = userService.modify(vo);
			if (result) {
				// DB에서 최신 정보 다시 조회
				UserVO updatedUser = userService.checkId(vo);
				session.setAttribute("currentUserInfo", updatedUser);
			}
		} else if ("staff".equals(type) || "admin".equals(type)) {
			StaffVO vo = new StaffVO();
			vo.setStaff_id(dto.getId());
			vo.setStaff_pw(dto.getPw());
			vo.setStaff_nm(dto.getNm());
			vo.setStaff_addr(dto.getAddr());
			vo.setStaff_role(dto.getStaff_role());

			result = staffService.modify(vo);
			// 세션 갱신!
			session.setAttribute("currentUserInfo", vo);
		}

		if (result) {
			redirectAttrs.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "회원 정보가 수정을 실패했습니다.");
		}
		return "redirect:/";
	}
	
	//회원 탈퇴
	@GetMapping("/delete")
	public String deleteAccount(HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("함수 진입");
		Object obj = session.getAttribute("currentUserInfo");
		log.info("currentUserInfo: " + obj);
		
		if (obj == null) {
			// 로그인 정보 없으면 빈 리스트 반환 (또는 예외처리)
			return "";
		}

		boolean result = false;
		if (obj instanceof UserVO) {
			UserVO user = (UserVO) obj;
			result = userService.deleteAccount(user);
			if (result) {
				redirectAttrs.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
				session.invalidate();
				return "redirect:/";
			} else {
				redirectAttrs.addFlashAttribute("message", "회원 탈퇴를 실패했습니다.");
				return "redirect:/profileEdit";
			}
		} else if (obj instanceof StaffVO) {
			StaffVO staff = (StaffVO) obj;
			result = staffService.deleteAccount(staff);
			if (result) {
				redirectAttrs.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
				session.invalidate();
				return "redirect:/";
			} else {
				redirectAttrs.addFlashAttribute("message", "회원 탈퇴를 실패했습니다.");
				return "redirect:/profileEdit";
			}
		} else {
			redirectAttrs.addFlashAttribute("message", "사용자 타입 오류 발생.");
			return "redirect:/profileEdit";
		}
	}

	// 비밀번호 일치 확인 (AJAX)
	@PostMapping(value = "/checkPassword", produces = "application/json")
	@ResponseBody
	public boolean checkPassword(@RequestParam("inputPw") String inputPw, HttpSession session) {
		String type = (String) session.getAttribute("userType");
		boolean match = false;
		if ("common".equals(type)) {
			UserVO currentUser = (UserVO) session.getAttribute("currentUserInfo");
			if (currentUser == null) {
				return false; // 로그인 정보 없음
			}
			match = userService.checkPassword(inputPw, currentUser.getUser_pw());
		} else if ("staff".equals(type) || "admin".equals(type)) {
			StaffVO currentstaff = (StaffVO) session.getAttribute("currentUserInfo");
			if (currentstaff == null) {
				return false; // 로그인 정보 없음
			}
			match = staffService.checkPassword(inputPw, currentstaff.getStaff_pw());
		}
		return match; // JSON true/false 로 응답!
	}
}
