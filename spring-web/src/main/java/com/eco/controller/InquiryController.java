package com.eco.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.InquiryDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.InquiryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/inquiry")
@AllArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;
	
	// 문의게시판으로 이동
	@GetMapping("")
	public String inquiryPage(Model model, HttpSession session) {
		log.info("전기 재해 신고 목록 페이지로 이동");
		
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	    	session.setAttribute("userType", "staff");
	        model.addAttribute("currentUserInfo", (StaffVO) user);
	    } else if (user instanceof UserVO) {
	    	session.setAttribute("userType", "common");
	        model.addAttribute("currentUserInfo", (UserVO) user);
	    } else {
	        // 로그인하지 않은 경우 처리할 수도 있음 (예: 비회원도 조회 가능 시)
	        model.addAttribute("userType", "guest");
	    }
		
		return "/notice/inquiry";
	}
	
	// 문의 게시판 목록 호출
	@GetMapping("/inquiryList")
	@ResponseBody
	public List<InquiryDTO> getInquiryList(@RequestParam(value = "search_word", required = false) String search_word, @RequestParam(value = "user_cd", required = false) Integer userCd){
		if (userCd != null) {
			return inquiryService.getPersonalInquiry(userCd, search_word);
		} else if (search_word != null && !search_word.isEmpty()) {
			return inquiryService.getInquiryBySearch(search_word);
		} else {
			return inquiryService.getAllInquiry();
		}
	}
	
	// 문의 게시판 상세 페이지 이동
	@GetMapping("/detail")
	public String inquiryDetailPage(@RequestParam("inquiry_cd") int inquiryCd, Model model, HttpSession session) {
		log.info("문의 게시판 상세 페이지 이동");
		log.info(inquiryCd);
		Object user = session.getAttribute("currentUserInfo");
	    
	    if (user instanceof StaffVO) {
	        model.addAttribute("currentUserInfo", (StaffVO) user);
	        if(((StaffVO) user).getStaff_role() == "staff") {
	        	session.setAttribute("userType", "staff");
	        } else {
	        	session.setAttribute("userType", "admin");
	        }
	    } else if (user instanceof UserVO) {
	        session.setAttribute("userType", "common");
	        model.addAttribute("currentUserInfo", (UserVO) user);
	    } else {
	        // 로그인하지 않은 경우 처리할 수도 있음 (예: 비회원도 조회 가능 시)
	        model.addAttribute("userType", "guest");
	    }
		
		InquiryDTO inquiry = inquiryService.getDetailInquiry(inquiryCd);
		log.info(inquiry);
		
		if (inquiry != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedCreatedtDt = inquiry.getCreated_dt().format(formatter);
	        String formattedUpdateDt = inquiry.getUpdate_dt() != null ? inquiry.getUpdate_dt().format(formatter) : "-";

	        model.addAttribute("inquiry", inquiry);
	        model.addAttribute("createdDt", formattedCreatedtDt);
	        model.addAttribute("updateDt", formattedUpdateDt);
	    }
		
		return "/notice/inquiryDetail";
	}
	
	// 문의 게시글 작성 페이지 이동
	@GetMapping("/form")
	public String inquiryFormPage(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		Object user = session.getAttribute("currentUserInfo");
        // 비정상적 루트로 접근 제한
        boolean accessAllow = false;
        if (user instanceof UserVO) {
        	accessAllow = true;
        	model.addAttribute("currentUserInfo", (UserVO) user);
        } else if(user instanceof StaffVO) {
        	accessAllow = false;
        } else {
        	accessAllow = false;
        }
        
        if (accessAllow) {
        	log.info("문의게시글 작성 페이지로 이동");
        	return "/notice/inquiryForm";
        } else {
        	redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	return "redirect:/inquiry";
        }
	}
	
	// 문의 게시글 등록
	@PostMapping("/register")
	public String registerNewInquiry(@ModelAttribute InquiryDTO inquiryDTO, HttpSession session, RedirectAttributes redirectAttrs) {
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
	    if (user == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }
	    
	    inquiryDTO.setUser_cd(user.getUser_cd());
        boolean result = inquiryService.registerInquiry(inquiryDTO);
		
		if (result) {
        	log.info("문의게시글 등록 성공");
        	redirectAttrs.addFlashAttribute("message", "문의 게시글이 등록되었습니다.");
        	return "redirect:/inquiry";
        } else {
        	redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
        	return "redirect:/inquiry";
        }
	}
	
	// 문의 게시글 수정 화면 진입
	@GetMapping("/modify")
	public String reportEditPage(@RequestParam("inquiry_cd") int inquiryCd, Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("문의 게시글 수정 페이지로 이동");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
	    if (user == null) {
	        redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/login";
	    }

	    InquiryDTO inquiry = inquiryService.getDetailInquiry(inquiryCd);

	    // 본인 확인 로직
	    boolean authorized = false;
	    int user_cd = user.getUser_cd();
	    if (inquiry != null && inquiry.getUser_cd() == user_cd) {
	        authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/inquiry";
	    }
		
	    model.addAttribute("inquiry", inquiry);
	    model.addAttribute("currentUserInfo", user);
		return "/notice/inquiryEdit";
	}
	
	// 문의 게시글 수정
	@PostMapping("/modify")
	public String reportEdit(@ModelAttribute InquiryDTO inquiryDTO, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("문의 게시글 수정 완료");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		inquiryDTO.setUser_cd(user.getUser_cd());
		inquiryDTO.setUpdate_dt(LocalDateTime.now());
		
		boolean result = inquiryService.modifyInquiry(inquiryDTO);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "문의 게시글 수정이 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "문의 게시글 수정에 실패하였습니다.");
		}
		return "redirect:/inquiry";
	}
	
	// 문의 게시글 삭제
	@PostMapping("/remove")
	public String reportEdit(@RequestParam("inquiry_cd") int inquiryCd, HttpSession session, RedirectAttributes redirectAttrs) {
		log.info("문의 게시글 삭제 완료");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		InquiryDTO inquiry = inquiryService.getDetailInquiry(inquiryCd);
		// 본인 확인 로직
	    boolean authorized = false;
	    int user_cd = user.getUser_cd();
	    if (inquiry != null && inquiry.getUser_cd() == user_cd) {
	        authorized = true;
	    }

	    if (!authorized) {
	        redirectAttrs.addFlashAttribute("message", "유효하지 않은 요청이거나 권한이 없습니다.");
	        return "redirect:/inquiry";
	    }
		
		boolean result = inquiryService.removeInquiry(inquiryCd);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "문의 게시글 삭제가 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "문의 게시글 삭제에 실패하였습니다.");
		}
		return "redirect:/inquiry";
	}
}
