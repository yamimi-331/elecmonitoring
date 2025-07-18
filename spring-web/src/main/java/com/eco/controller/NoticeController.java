package com.eco.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.ReportListResponseDTO;
import com.eco.domain.vo.NoticeVO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
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
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	    	StaffVO staff = (StaffVO) user;
	    	if ("staff".equals(staff.getStaff_role())){
	    		session.setAttribute("userType", "staff");
	    	}else {
	    		session.setAttribute("userType", "admin");
	    	}
	        model.addAttribute("currentUserInfo", (StaffVO) user);
	    } else if (user instanceof UserVO) {
	    	session.setAttribute("userType", "common");
	        model.addAttribute("currentUserInfo", (UserVO) user);
	    } else {
	        // 로그인하지 않은 경우 처리할 수도 있음 (예: 비회원도 조회 가능 시)
	        model.addAttribute("userType", "guest");
	    }

		return "/notice/notice";
	}
	
	// 전기 재해 신고 목록 호출
	@GetMapping("/noticeList")
	@ResponseBody
	public List<NoticeDTO> getNoticeList(@RequestParam(value = "search_word", required = false) String searchWord){
		//int size = 10;
		if (searchWord != null && !searchWord.isEmpty()) {
			List<NoticeDTO> dto = noticeService.getNoticeSearchList(searchWord);
			return dto;
	        //return reportService.getLocalReportList(local, page, size);
	    } else {
	    	List<NoticeDTO> dto = noticeService.getNoticeList();
	    	log.info(dto);
	        return dto;
	    	//ReportListResponseDTO dto = reportService.getAllReportList(page, size);
	    	//return dto;
	    }
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
