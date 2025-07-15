package com.eco.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.domain.DTO.ReportDTO;
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
	
	@GetMapping("/reportList")
	@ResponseBody
	public List<ReportDTO> getReportList(){
		return reportService.getAllReportList();
	}
	
	// 전기 재해 신고 목록 페이지 이동
	@GetMapping("/detail")
	public String reportDetailPage() {
		log.info("전기 재해 신고 상세 페이지로 이동");
		return "/notice/reportDetail";
	}
	
	// 전기 재해 신고 작성 페이지 이동
	@GetMapping("/form")
	public String reportFormPage() {
		log.info("전기 재해 신고 작성 페이지로 이동");
		return "/notice/reportForm";
	}
}
