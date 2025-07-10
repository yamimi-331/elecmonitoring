package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class IndexController {

	//메인화면 접속
	@GetMapping("/")
	public String mainPage() {
		log.info("메인화면 페이지로 이동");
		return "index";
	}
}
