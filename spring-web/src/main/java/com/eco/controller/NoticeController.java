package com.eco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/notice")
public class NoticeController {
	
	// 공지사항 페이지로 이동하기
	@GetMapping("")
	public String noticePage() {
		log.info("공지사항 페이지로 이동");
		return "notice";
	}
	
	
	// 공지사항 상세 페이지로 이동하기
	@GetMapping("/detail")
	public String noticeDetailPage() {
		log.info("공지사항 상세 페이지로 이동");
		return "noticeDetail";
	}
}
