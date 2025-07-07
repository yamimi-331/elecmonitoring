package com.eco.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eco.domain.NoticeVO;
import com.eco.domain.StaffVO;
import com.eco.domain.UserVO;
import com.eco.service.NoticeService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/notice")
@AllArgsConstructor
public class NoticeController {

	private NoticeService noticeService;

	// 공지사항 목록 페이지 이동 + 데이터 조회
	@GetMapping("")
	public String noticePage(HttpSession session, Model model) {
		log.info("공지사항 목록 조회 페이지로 이동");
		Object obj = session.getAttribute("currentUserInfo");
		List<NoticeVO> noticeList = null;
		// 등급별 공지사항 조회 목록 구분
		if (obj instanceof UserVO) {
			UserVO user = (UserVO) obj;
			// user의 특정 필드 접근 가능 (예: user.getUsername())
			noticeList = noticeService.getNoticeList("common");
		} else if (obj instanceof StaffVO) {
			StaffVO staff = (StaffVO) obj;
			// staff의 특정 필드 접근 가능 (예: staff.getStaffId())
			noticeList = noticeService.getNoticeList(staff.getStaff_role());
		} else {
			// 예상치 못한 타입 혹은 null 처리
		}

		// List<NoticeVO> noticeList = noticeService.getNoticeList();

		model.addAttribute("noticeList", noticeList);
		return "/notice/notice";
	}

	// 공지사항 상세/수정/등록 페이지 이동
	@GetMapping("/detail")
	public String noticeDetailPage(@RequestParam(required = false) Integer notice_cd,
			@RequestParam(defaultValue = "view") String mode, Model model) {
		log.info("공지사항 상세 페이지로 이동, mode = " + mode);
		// 상세 조회 일때
		if (notice_cd != null && !mode.equals("insert")) {
			NoticeVO notice = noticeService.getNoticeDetail(notice_cd);
			model.addAttribute("notice", notice);
		}

		model.addAttribute("mode", mode); // view, edit, insert 구분
		return "/notice/noticeDetail"; // /WEB-INF/views/noticeDetail.jsp
	}

	// 공지사항 등록 처리
	@PostMapping("/insert")
	public String insertNotice(NoticeVO notice) {
		log.info("공지사항 등록 처리");
		noticeService.insertNotice(notice);
		return "redirect:/notice";
	}

	// 공지사항 수정 처리
	@PostMapping("/update")
	public String updateNotice(NoticeVO notice) {
		log.info("공지사항 수정 처리");
		noticeService.updateNotice(notice);
		return "redirect:/notice/detail?notice_cd=" + notice.getNotice_cd() + "&mode=view";
	}

	// 공지사항 삭제 처리(소프트 삭제)
	@PostMapping("/delete")
	public String deleteNotice(@RequestParam("notice_cd") int notice_cd) {
		log.info("공지사항 삭제 처리");
		noticeService.deleteNotice(notice_cd);
		return "redirect:/notice";
	}
}
