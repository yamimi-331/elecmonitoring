package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/pjhtest")
public class PjhtestController {

	@GetMapping("")
	public String asRegisterPage() {
		return "pjhtest";
	}
}
