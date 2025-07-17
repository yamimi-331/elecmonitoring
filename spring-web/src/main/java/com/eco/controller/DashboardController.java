package com.eco.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eco.domain.DTO.ReportStatsDTO;
import com.eco.service.ReportService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {
	
	private final ReportService reportService;
	
	// 대시보드 페이지 이동
	@GetMapping("")
	public String dashboardPage(Model model) {
		log.info("대시보드 페이지로 이동");

		List<ReportStatsDTO> stats = reportService.getTop5LocalReportStats();
		model.addAttribute("reportStats", stats);

		return "/monitor/dashboard";
	}
}
