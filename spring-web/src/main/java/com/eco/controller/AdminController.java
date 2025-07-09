package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("/account")
	public String adminPage() {
		log.info("관리자 페이지 이동");
		return "/admin/account";
	}
}
