package com.eco.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.domain.DTO.InquiryDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.InquiryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/inquiry")
@AllArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;
	
	// 문의게시판으로 이동
	@GetMapping("")
	public String reportPage(Model model, HttpSession session) {
		log.info("전기 재해 신고 목록 페이지로 이동");
		
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	    	session.setAttribute("userType", "staff");
	        model.addAttribute("currentUserInfo", (StaffVO) user);
	    } else if (user instanceof UserVO) {
	    	session.setAttribute("userType", "common");
	        model.addAttribute("currentUserInfo", (UserVO) user);
	    } else {
	        // 로그인하지 않은 경우 처리할 수도 있음 (예: 비회원도 조회 가능 시)
	        model.addAttribute("userType", "guest");
	    }
		
		return "/notice/inquiry";
	}
	
	// 전기 재해 신고 목록 호출
	@GetMapping("/inquiryList")
	@ResponseBody
	public List<InquiryDTO> getReportList(){
		return inquiryService.getAllInquiry();
	}
	
	// 문의 게시판 상세 페이지 이동
	@GetMapping("/detail")
	public String reportDetailPage(@RequestParam("inquiry_cd") int inquiryCd, Model model, HttpSession session) {
		log.info("문의 게시판 상세 페이지 이동");
		log.info(inquiryCd);
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	    	session.setAttribute("userType", "staff");
	        model.addAttribute("currentUserInfo", (StaffVO) user);
	    } else if (user instanceof UserVO) {
	        session.setAttribute("userType", "common");
	        model.addAttribute("currentUserInfo", (UserVO) user);
	    } else {
	        // 로그인하지 않은 경우 처리할 수도 있음 (예: 비회원도 조회 가능 시)
	        model.addAttribute("userType", "guest");
	    }
		
		InquiryDTO inquiry = inquiryService.getDetailInquiry(inquiryCd);
		log.info(inquiry);
		
		if (inquiry != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedCreatedtDt = inquiry.getCreated_dt().format(formatter);
	        String formattedUpdateDt = inquiry.getUpdate_dt() != null ? inquiry.getUpdate_dt().format(formatter) : "-";

	        model.addAttribute("inquiry", inquiry);
	        model.addAttribute("createdDt", formattedCreatedtDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	    }
		
		return "/notice/inquiryDetail";
	}
}
