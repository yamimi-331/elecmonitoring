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

import com.eco.domain.DTO.ReportDTO;
import com.eco.domain.vo.StaffVO;
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
	public String reportPage(Model model) {
		log.info("전기 재해 신고 목록 페이지로 이동");
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
		
		ReportDTO report = reportService.getDetailReport(reportCd);
		
		if (report != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedReportDt = report.getReport_dt().format(formatter);
	        String formattedUpdateDt = report.getUpdate_dt() != null ? report.getUpdate_dt().format(formatter) : "-";

	        model.addAttribute("report", report);
	        model.addAttribute("reportDt", formattedReportDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	    }
		
		StaffVO currentUserInfo = (StaffVO) session.getAttribute("currentUserInfo");
		model.addAttribute("currentUserInfo", currentUserInfo);
		
		return "/notice/reportDetail";
	}
	
	// 전기 재해 신고 작성 페이지 이동
	@GetMapping("/form")
	public String reportFormPage() {
		log.info("전기 재해 신고 작성 페이지로 이동");
		return "/notice/reportForm";
	}
}
