package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/report")
public class ReportController {

	// 전기 재해 신고 목록 페이지 이동
	@GetMapping("")
	public String reportPage() {
		log.info("전기 재해 신고 목록 페이지로 이동");
		return "/notice/report";
	}
}
