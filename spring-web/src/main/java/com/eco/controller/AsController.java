package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eco.service.AsService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/as")
public class AsController {
	private final AsService asService;
	
	
}
