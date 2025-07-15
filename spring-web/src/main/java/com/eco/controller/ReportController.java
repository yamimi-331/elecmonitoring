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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.ReportDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.ReportService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {

	private final ReportService reportService;
		
	// 전기 재해 신고 목록 페이지 이동
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
		
		return "/notice/report";
	}
	
	// 전기 재해 신고 목록 호출
	@GetMapping("/reportList")
	@ResponseBody
	public List<ReportDTO> getReportList(){
		return reportService.getAllReportList();
	}
	
	// 전기 재해 신고 상세 페이지 이동
	@GetMapping("/detail")
	public String reportDetailPage(@RequestParam("report_cd") int reportCd, Model model, HttpSession session) {
		log.info("전기 재해 신고 상세 페이지로 이동");
		
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
		
		ReportDTO report = reportService.getDetailReport(reportCd);
		
		if (report != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedReportDt = report.getReport_dt().format(formatter);
	        String formattedUpdateDt = report.getUpdate_dt() != null ? report.getUpdate_dt().format(formatter) : "-";

	        model.addAttribute("report", report);
	        model.addAttribute("reportDt", formattedReportDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	    }
		
		return "/notice/reportDetail";
	}
	
	// 전기 재해 신고 작성 페이지 이동
	@GetMapping("/form")
	public String reportFormPage(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		Object user = session.getAttribute("currentUserInfo");
        // 비정상적 루트로 접근 제한
        boolean accessAllow = false;
        if (user instanceof UserVO) {
        	accessAllow = false;
        } else if(user instanceof StaffVO) {
        	accessAllow = true;
        	model.addAttribute("currentUserInfo", (StaffVO) user);
        } else {
        	accessAllow = false;
        }
        
        if (accessAllow) {
        	log.info("전기 재해 신고 작성 페이지로 이동");
        	return "/notice/reportForm";
        } else {
        	redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	return "redirect:/report";
        }
	}
}
