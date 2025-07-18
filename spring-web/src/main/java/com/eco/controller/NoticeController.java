package com.eco.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.NoticePageResponseDTO;
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
	
	// 공지사항 목록 호출
//	@GetMapping("/noticeList")
//	@ResponseBody
//	public List<NoticeDTO> getNoticeList(@RequestParam(value = "search_word", required = false) String searchWord){
//		//int size = 10;
//		if (searchWord != null && !searchWord.isEmpty()) {
//			List<NoticeDTO> dto = noticeService.getNoticeSearchList(searchWord);
//			return dto;
//	        //return reportService.getLocalReportList(local, page, size);
//	    } else {
//	    	List<NoticeDTO> dto = noticeService.getNoticeList();
//	        return dto;
//	    	//ReportListResponseDTO dto = reportService.getAllReportList(page, size);
//	    	//return dto;
//	    }
//	}

	   /**
     * 공지사항 목록을 페이징하여 조회하는 API 엔드포인트입니다.
     * 기존 /noticeList 엔드포인트를 페이징 적용 버전으로 변경합니다.
     *
     * @param search_word 검색어 (제목 또는 내용 검색), 기본값은 빈 문자열
     * @param page 현재 페이지 번호 (서버 기준: 0-based), 기본값 0
     * @param size 페이지당 항목 수, 기본값 10
     * @return NoticePageResponseDTO 객체를 포함하는 ResponseEntity (JSON 응답)
     */
    @GetMapping("/noticeList")
    @ResponseBody
    public ResponseEntity<NoticePageResponseDTO> getNoticeList(
            @RequestParam(value = "search_word", required = false, defaultValue = "") String search_word,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            // search_word 유무와 관계없이 항상 페이징 서비스 메서드를 호출합니다.
            // 서비스 메서드 내부에서 search_word를 처리합니다.
            NoticePageResponseDTO response = noticeService.getNoticeListPaged(search_word, page, size);
            return ResponseEntity.ok(response); // HTTP 200 OK와 함께 DTO 반환
        } catch (Exception e) {
            // 오류 발생 시 500 Internal Server Error와 함께 빈 DTO 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticePageResponseDTO());
        }
    }
	
	// 공지사항 상세페이지 이동
	@GetMapping("/detail")
	public String noticeDetailPage(@RequestParam(required = false) Integer notice_cd, Model model, HttpSession session) {
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
	        model.addAttribute("userType", "guest");
	    }

	    NoticeDTO notice = noticeService.getNoticeDetail(notice_cd);
	    
	    if (notice != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedNoticeDt = notice.getCreate_dt().format(formatter);

	        String formattedUpdateDt = "-";
	        if (notice.getUpdate_dt() != null) {
	            // 등록일과 수정일이 다를 경우에만 수정일을 표시
	            if (!notice.getCreate_dt().isEqual(notice.getUpdate_dt())) {
	                formattedUpdateDt = notice.getUpdate_dt().format(formatter);
	            }
	        }
	        
	        model.addAttribute("notice", notice);
	        model.addAttribute("noticeDt", formattedNoticeDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	    }
	    
		return "/notice/noticeDetail"; // /WEB-INF/views/noticeDetail.jsp
	}

	// 공지사항 작성 페이지 이동
	@GetMapping("/form")
	public String noticeFormPage(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		Object user = session.getAttribute("currentUserInfo");
        // 비정상적 루트로 접근 제한
        boolean accessAllow = false;
        if (user instanceof UserVO) {
        	accessAllow = false;
        } else if(user instanceof StaffVO) {
        	StaffVO staff = (StaffVO) user;
        	if ("admin".equals(staff.getStaff_role())) {
        		accessAllow = true;
    	    	model.addAttribute("currentUserInfo", staff);
    	    } else {
    	    	accessAllow = false;
	    	}
        } else {
        	accessAllow = false;
        }
        
        if (accessAllow) {
        	log.info("공지사항 작성 페이지로 이동");
        	return "/notice/noticeForm";
        } else {
        	redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	return "redirect:/notice";
        }
	}
	
	// 공지사항 등록 처리
	@PostMapping("/register")
	public String registerNewNotice(@ModelAttribute NoticeDTO noticeDTO, 
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			HttpSession session, RedirectAttributes redirectAttrs) {
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    if (staff == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }
	    
	    noticeDTO.setStaff_cd(staff.getStaff_cd());
		/* boolean result = noticeService.registerNotice(noticeDTO); */
        try {
            // 서비스 계층에서 공지사항 등록 및 파일 정보 등록을 함께 처리
            // 파일이 없을 수도 있으므로 null 체크 또는 빈 배열 처리
            noticeService.registerNoticeWithFiles(noticeDTO, files); 
            
            log.info("공지사항 등록 성공");
            redirectAttrs.addFlashAttribute("message", "공지사항이 등록되었습니다.");
            return "redirect:/notice";
        } catch (Exception e) {
            log.error("공지사항 등록 실패: " + e.getMessage());
            redirectAttrs.addFlashAttribute("message", "공지사항 등록에 실패했습니다.");
            return "redirect:/notice/registerForm"; // 등록 폼으로 다시 이동하거나 적절한 에러 페이지로
        }
	}
	
	// 공지사항 수정화면 진입
	@GetMapping("/modify")
	public String noticeEditPage(@RequestParam("notice_cd") int noticeCd, Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("공지사항 페이지로 이동");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    if (staff == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }

	    NoticeDTO notice = noticeService.getNoticeDetail(noticeCd);

	    // 본인 확인 로직
	    boolean authorized = false;
	    if ("admin".equals(staff.getStaff_role()) ) {
	    	authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/notice";
	    }
		
	    model.addAttribute("notice", notice);
	    model.addAttribute("currentUserInfo", staff);
		return "/notice/noticeEdit";
	}

	// 공지사항 수정 처리
	@PostMapping("/modify")
	public String noticeEdit(@ModelAttribute NoticeDTO noticeDTO, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("공지사항 수정 완료");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
		noticeDTO.setStaff_cd(staff.getStaff_cd());
		noticeDTO.setUpdate_dt(LocalDateTime.now());
		
		boolean result = noticeService.modifyNotice(noticeDTO);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "공지사항 수정이 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "공지사항 수정에 실패하였습니다.");
		}
		return "redirect:/notice";
	}

	// 공지사항 삭제 처리(소프트 삭제)
	@PostMapping("/remove")
	public String noticeDelete(@RequestParam("notice_cd") int noticeCd, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("공지사항 삭제 완료");
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    boolean authorized = false;
	    if ("admin".equals(staff.getStaff_role()) ) {
	    	authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/notice";
	    }
		
		boolean result = noticeService.removeNotice(noticeCd);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "공지사항 삭제가 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "공지사항 삭제에 실패하였습니다.");
		}
		return "redirect:/notice";
	}
}
