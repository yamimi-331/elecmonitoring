package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eco.domain.ProfileEditDTO;
import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/profileEdit")
public class ProfileEditController {

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

	@PostMapping("")
	public String profileEdit(ProfileEditDTO dto, HttpSession session) {
		String type = (String) session.getAttribute("userType");
		if ("common".equals(type)) {
			UserVO vo = new UserVO();
			vo.setUser_id(dto.getId());
			vo.setUser_pw(dto.getPw());
			vo.setUser_nm(dto.getNm());
			vo.setUser_addr(dto.getAddr());
			vo.setUser_mail(dto.getMail());
			// TODO: userService.update(vo);
			
			// 수정된 정보 다시 세션에 저장
			session.setAttribute("currentUserInfo", vo);

		} else if ("staff".equals(type) || "admin".equals(type)) {
			StaffVO vo = new StaffVO();
			vo.setStaff_id(dto.getId());
			vo.setStaff_pw(dto.getPw());
			vo.setStaff_nm(dto.getNm());
			vo.setStaff_addr(dto.getAddr());
			vo.setStaff_role(dto.getMail());
			// TODO: staffService.update(vo);
			// 세션 갱신!
	        session.setAttribute("currentUserInfo", vo);
		}

		return "redirect:/profileEdit";
	}
}
