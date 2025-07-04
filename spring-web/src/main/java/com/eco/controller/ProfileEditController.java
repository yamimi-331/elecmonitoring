package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
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
	public String profilePage(HttpSession session) {
		String type = (String) session.getAttribute("userType");
		ProfileEditDTO dto = new ProfileEditDTO();
		if(type =="common") {
			UserVO currentUser = (UserVO) session.getAttribute("currentUserInfo");
			dto.setId(currentUser.getUser_id());
			dto.setPw(currentUser.getUser_pw());
			dto.setNm(currentUser.getUser_nm());
			dto.setAddr(currentUser.getUser_addr());
			dto.setMail(currentUser.getUser_mail());
		}else if(type =="staff" || type =="admin") {
			StaffVO currentUser = (StaffVO) session.getAttribute("currentUserInfo");
			dto.setId(currentUser.getStaff_id());
			dto.setPw(currentUser.getStaff_pw());
			dto.setNm(currentUser.getStaff_nm());
			dto.setAddr(currentUser.getStaff_addr());
			dto.setMail(currentUser.getStaff_role());
		}
		session.setAttribute("profileInfo", dto);
		
		return "user/profileEdit";
	}
	
	@PostMapping("")
	public String profileEdit(ProfileEditDTO dto, HttpSession session) {
		String type = (String) session.getAttribute("userType");
		if(type =="user") {
			UserVO vo = new UserVO();
			

		}else if(type =="staff" || type =="admin") {
			StaffVO vo = new StaffVO();
		}
		
		return "user/profileEdit";
	}
}
