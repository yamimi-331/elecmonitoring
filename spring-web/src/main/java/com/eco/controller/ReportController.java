package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String reportPage() {
		log.info("전기 재해 신고 목록 페이지로 이동");
		return "/notice/report";
	}
}
