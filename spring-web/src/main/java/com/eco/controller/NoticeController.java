package com.eco.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.core.io.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.FileUploadDTO;
import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.NoticePageResponseDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.exception.ServiceException;
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
	        
	        // 1. 해당 공지사항의 첨부 파일 목록 조회
            List<FileUploadDTO> attachedFiles = noticeService.getAttachedFiles(notice_cd);
            
	        model.addAttribute("notice", notice);
	        model.addAttribute("noticeDt", formattedNoticeDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	        model.addAttribute("attachedFiles", attachedFiles);
	    }
	    
		return "/notice/noticeDetail"; // /WEB-INF/views/noticeDetail.jsp
	}
	 /**
     * 파일 다운로드 요청 처리
     * @param fileCd 다운로드할 파일의 코드
     * @return 파일 데이터와 HTTP 헤더를 포함하는 ResponseEntity
     */
    @GetMapping("/downloadFile/{fileCd}") // 파일 코드를 PathVariable로 받음
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileCd) {
        FileUploadDTO fileDTO = noticeService.getFileDetail(fileCd); // 파일 정보 조회

        if (fileDTO == null) {
            log.warn("파일을 찾을 수 없습니다. fileCd: " + fileCd);
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        try {
            File file = new File(fileDTO.getFile_path()); // 실제 파일 경로로 File 객체 생성
            if (!file.exists() || !file.canRead()) {
                log.error("파일이 존재하지 않거나 읽을 수 없습니다: " + fileDTO.getFile_path());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
            }

            // 파일의 MIME 타입 결정 (예: image/png, application/pdf 등)
            // 실제 서비스에서는 Files.probeContentType(file.toPath()) 등을 사용하여 동적으로 결정하는 것이 좋습니다.
            String contentType = "application/octet-stream"; // 기본값: 알 수 없는 타입 (강제 다운로드)
            try {
                String detectedContentType = Files.probeContentType(file.toPath());
                if (detectedContentType != null) {
                    contentType = detectedContentType;
                }
            } catch (Exception e) {
                log.warn("파일 "+fileDTO.getOriginal_name()+"의 MIME 타입 감지 실패, 기본값 사용: " + e.getMessage());
            }

            Resource resource = new InputStreamResource(new FileInputStream(file));
            String encodedFileName = URLEncoder.encode(fileDTO.getOriginal_name(), "UTF-8").replace("+", "%20"); // 파일명 인코딩

            HttpHeaders headers = new HttpHeaders();
            // Content-Disposition을 attachment로 설정하여 다운로드 강제
            // filename*은 UTF-8 인코딩을 위한 RFC 5987 표준
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            headers.add(HttpHeaders.CONTENT_TYPE, contentType); // 파일의 실제 MIME 타입 설정
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length())); // 파일 크기 설정

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다: " + fileDTO.getFile_path(), e);
            return ResponseEntity.notFound().build();
        } catch (UnsupportedEncodingException e) {
            log.error("파일명 인코딩 실패: " + fileDTO.getOriginal_name(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생: " + fileDTO.getOriginal_name(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
		 
		// DTO 바인딩 직후 로그 (이 위치에서 title, content가 null로 나오면 문제)
        log.info("DTO (before staff_cd set): " + noticeDTO);
        
		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
	    if (staff == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }
	    
	    noticeDTO.setStaff_cd(staff.getStaff_cd());
	    // 이 위치에서 DTO 로그를 다시 확인 (staff_cd가 추가된 후)
        log.info("DTO (after staff_cd set): " + noticeDTO);

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
            return "redirect:/notice/noticeForm"; // 등록 폼으로 다시 이동하거나 적절한 에러 페이지로
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
	    // 첨부 파일 목록 조회하여 모델에 추가
        List<FileUploadDTO> attachedFiles = noticeService.getAttachedFiles(noticeCd);
        
	    model.addAttribute("notice", notice);
	    model.addAttribute("currentUserInfo", staff);
	    model.addAttribute("attachedFiles", attachedFiles);
		return "/notice/noticeEdit";
	}
	
	// 공지사항 수정 처리
    @PostMapping("/modify")
    public String noticeEditWidthFile(@ModelAttribute NoticeDTO noticeDTO,
                             @RequestParam(value = "newFiles", required = false) MultipartFile[] newFiles, // 새로 추가된 파일
                             @RequestParam(value = "deletedFileCds", required = false) String[] deletedFileCds, // 삭제될 파일 코드
                             HttpSession session, RedirectAttributes redirectAttrs) {
        log.info("공지사항 수정 요청 수신: " + noticeDTO);
        log.info("새로운 파일 수: "+ (newFiles != null ? newFiles.length : 0));
        log.info("삭제될 파일 코드: " + (deletedFileCds != null ? String.join(", ", deletedFileCds) : "없음"));

        StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
        
        if (staff == null) {
            redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
            return "redirect:/login";
        }

        noticeDTO.setStaff_cd(staff.getStaff_cd());

        try {
            // 파일 처리 로직을 포함하는 새로운 서비스 메서드 호출
            noticeService.modifyNoticeWithFiles(noticeDTO, newFiles, deletedFileCds);
            
            redirectAttrs.addFlashAttribute("message", "공지사항 수정이 완료되었습니다.");
            return "redirect:/notice/detail?notice_cd=" + noticeDTO.getNotice_cd(); // 수정 후 상세 페이지로 이동
        } catch (ServiceException e) {
            log.error("공지사항 수정 실패: " + e.getMessage(), e);
            redirectAttrs.addFlashAttribute("message", "공지사항 수정에 실패하였습니다.");
            return "redirect:/notice/modify?notice_cd=" + noticeDTO.getNotice_cd(); // 실패 시 수정 페이지로 다시 이동
        } catch (Exception e) {
            log.error("예상치 못한 공지사항 수정 오류: " + e.getMessage(), e);
            redirectAttrs.addFlashAttribute("message", "공지사항 수정 중 알 수 없는 오류가 발생했습니다.");
            return "redirect:/notice/modify?notice_cd=" + noticeDTO.getNotice_cd();
        }
    }
    
//	// 공지사항 수정 처리
//	@PostMapping("/modify")
//	public String noticeEdit(@ModelAttribute NoticeDTO noticeDTO, HttpSession session, RedirectAttributes redirectAttrs) {
//		log.info("공지사항 수정 완료");
//		StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
//		noticeDTO.setStaff_cd(staff.getStaff_cd());
//		noticeDTO.setUpdate_dt(LocalDateTime.now());
//		
//		boolean result = noticeService.modifyNotice(noticeDTO);
//		if (result) {
//			redirectAttrs.addFlashAttribute("message", "공지사항 수정이 완료되었습니다.");
//		} else {
//			redirectAttrs.addFlashAttribute("message", "공지사항 수정에 실패하였습니다.");
//		}
//		return "redirect:/notice";
//	}

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
