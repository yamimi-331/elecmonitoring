package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/dashboard")
public class DashboardController {

	//대시보드 페이지 이동
	@GetMapping("")
	public String dashboardPage() {
		log.info("대시보드 페이지로 이동");
		return "/monitor/dashboard";
	}
}
