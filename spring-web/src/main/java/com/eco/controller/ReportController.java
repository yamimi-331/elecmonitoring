package com.eco.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.ReportDTO;
import com.eco.domain.DTO.ReportListResponseDTO;
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
	public ReportListResponseDTO getReportList(@RequestParam(value = "local", required = false) String local, @RequestParam(value = "page", defaultValue = "1") int page){
		int size = 10;
		if (local != null && !local.isEmpty()) {
			/* return reportService.getLocalReportList(local); */
	        return reportService.getLocalReportList(local, page, size);
	    } else {
	        //return reportService.getAllReportList();
	    	ReportListResponseDTO dto = reportService.getAllReportList(page, size);
	    	return dto;
	    }
	}
	
	// 전기 재해 신고 상세 페이지 이동
	@GetMapping("/detail")
	public String reportDetailPage(@RequestParam("report_cd") int reportCd, Model model, HttpSession session) {
		log.info("전기 재해 신고 상세 페이지로 이동");
		
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	    	StaffVO staff = (StaffVO) user;
	    	if ("staff".equals(staff.getStaff_role())){
	    		session.setAttribute("userType", "staff");
	    	}else {
	    		session.setAttribute("userType", "admin");
	    	}
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
	
	// 신고 글 등록
	@PostMapping("/register")
	public String registerNewReport(@ModelAttribute ReportDTO reportDTO, HttpSession session, RedirectAttributes redirectAttrs) {
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    if (staff == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }
	    
		reportDTO.setStaff_cd(staff.getStaff_cd());
        boolean result = reportService.registerReport(reportDTO);
		
		if (result) {
        	log.info("전기 재해 신고 등록 성공");
        	redirectAttrs.addFlashAttribute("message", "신고 게시글이 등록되었습니다.");
        	return "redirect:/report";
        } else {
        	redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	return "redirect:/report";
        }
	}
	
	// 전기재해 신고 글 수정화면 진입
	@GetMapping("/modify")
	public String reportEditPage(@RequestParam("report_cd") int reportCd, Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("전기 재해 신고 수정 페이지로 이동");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    if (staff == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }

	    ReportDTO report = reportService.getDetailReport(reportCd);

	    // 본인 확인 로직
	    boolean authorized = false;
	    int staff_cd = staff.getStaff_cd();
	    if (report != null && report.getStaff_cd() == staff_cd) {
	        authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/report";
	    }
		
	    model.addAttribute("report", report);
	    model.addAttribute("currentUserInfo", staff);
		return "/notice/reportEdit";
	}
	
	// 전기 재해 신고 게시글 수정
	@PostMapping("/modify")
	public String reportEdit(@ModelAttribute ReportDTO reportDTO, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("전기 재해 신고 수정 완료");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
		reportDTO.setStaff_cd(staff.getStaff_cd());
		reportDTO.setUpdate_dt(LocalDateTime.now());
		
		boolean result = reportService.modifyReport(reportDTO);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "신고 게시글 수정이 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "신고 게시글 수정에 실패하였습니다.");
		}
		return "redirect:/report";
	}
	
	// 전기 재해 신고 게시글 삭제
	@PostMapping("/remove")
	public String reportEdit(@RequestParam("report_cd") int reportCd, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("전기 재해 신고 삭제 완료");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
		ReportDTO report = reportService.getDetailReport(reportCd);
		// 본인 확인 로직
	    boolean authorized = false;
	    int staff_cd = staff.getStaff_cd();
	    if (report != null && report.getStaff_cd() == staff_cd) {
	        authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/report";
	    }
		
		boolean result = reportService.removeReport(reportCd);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "신고 게시글 삭제가 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "신고 게시글 삭제에 실패하였습니다.");
		}
		return "redirect:/report";
	}
}
