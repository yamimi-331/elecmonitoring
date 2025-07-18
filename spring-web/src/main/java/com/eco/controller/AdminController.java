package com.eco.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.DTO.StaffPageResponseDTO;
import com.eco.domain.DTO.UserPageResponseDTO;
import com.eco.domain.vo.StaffVO;
import com.eco.domain.vo.UserVO;
import com.eco.service.StaffService;
import com.eco.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;
	private final StaffService staffService;
	
	// 계정관리 페이지 접속
	@GetMapping("/account")
	public String adminPage(HttpSession session, RedirectAttributes redirectAttrs) {
		Object currentUser = session.getAttribute("currentUserInfo");
		// 비정상적 루트로 접근 제한
		boolean accessAllow = false;
		if (currentUser instanceof UserVO) {
			accessAllow = false;
		} else if(currentUser instanceof StaffVO) {
			StaffVO vo = (StaffVO) currentUser;
			if("admin".equals(vo.getStaff_role())) {
				accessAllow = true;
			}else {
				accessAllow = false;
			}
		} else {
			accessAllow = false;
		}
		
		if (accessAllow) {
			log.info("관리자 페이지 이동");
			return "/admin/account";
		} else {
			redirectAttrs.addFlashAttribute("message", "잘못된 접근입니다.");
			return "redirect:/";
		}
	}
	
	//이름으로 직원 정보 조회
	@GetMapping("/search-users")
	@ResponseBody
	public List<StaffVO> searchStaffbyName(@RequestParam("staffId") String staff_id) {
		log.info("직원 정보 조회함수 진입");
		StaffVO staffVO = new StaffVO();
		staffVO.setStaff_id(staff_id);
		return staffService.getStaffList(staffVO);
	}
	
	// 새로운 메서드: 이름으로 직원 정보 조회 (페이징 적용)
    @GetMapping("/search-users-paged") // 새로운 엔드포인트 이름
    public ResponseEntity<StaffPageResponseDTO> searchStaffbyNamePaged(
            @RequestParam("staffId") String staffId,
            @RequestParam(defaultValue = "0") int page, // 0-based 페이지 번호
            @RequestParam(defaultValue = "10") int size) { // 페이지당 항목 수
        log.info("직원 정보 조회함수 진입 (페이징): staffId="+staffId+", page="+page+", size="+size);
        try {
            StaffPageResponseDTO response = staffService.getStaffListPaged(staffId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("페이징된 직원 정보 조회 실패: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StaffPageResponseDTO());
        }
    }
	
	// 비활성화 계정 조회
	@GetMapping("/search-deleted-users")
	@ResponseBody
	public List<?> searchDeleteUsers(@RequestParam("userType") String userType, @RequestParam("id") String id) {
		log.info("비활성화 계정 조회함수 진입");
		if ("common".equals(userType)) {
			UserVO userVO = new UserVO();
			userVO.setUser_id(id);
			List<UserVO> result = userService.getUserForRecover(userVO);
			return result;	
		} else if("staff".equals(userType)) {
			StaffVO staffVO = new StaffVO();
			staffVO.setStaff_id(id);
			List<StaffVO> result = staffService.getStaffForRecover(staffVO);
			return result;
		} else {
			log.info("알수없는 user type");
			return null;
		}
	}
	// 새로운 메서드: 비활성화 계정 조회 (페이징 적용)
    @GetMapping("/search-deleted-users-paged") // 새로운 엔드포인트 이름
    public ResponseEntity<?> searchDeleteUsersPaged(
            @RequestParam("userType") String userType,
            @RequestParam("id") String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
		log.info("비활성화 계정 조회함수 진입 (페이징): userType=" + userType + ", id=" + id + ", page=" + page + ", size=" + size);
        try {
            if ("common".equals(userType)) {
                UserPageResponseDTO response = userService.getUserForRecoverPaged(id, page, size);
                return ResponseEntity.ok(response);
            } else if ("staff".equals(userType)) {
                StaffPageResponseDTO response = staffService.getStaffForRecoverPaged(id, page, size);
                return ResponseEntity.ok(response);
            } else {
                log.warn("알 수 없는 user type: " + userType);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("알 수 없는 사용자 유형입니다.");
            }
        } catch (Exception e) {
            log.error("페이징된 비활성화 계정 조회 실패: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("페이징된 비활성화 계정 조회 실패");
        }
    }
	// 계정 복구 버튼
	@PostMapping("/restore-account")
	@ResponseBody
	public void restoreAccount(@RequestParam("userType") String userType, @RequestParam("id") String id) {
		log.info("계정 복구 버튼 클릭 함수 진입");
		if ("common".equals(userType)) {
			UserVO userVO = new UserVO();
			userVO.setUser_id(id);
			userService.recoverAccount(userVO);
		} else if("staff".equals(userType)) {
			StaffVO staffVO = new StaffVO();
			staffVO.setStaff_id(id);
			staffService.recoverAccount(staffVO);
		} else {
			log.info("복구할 계정의 타입 오류 발생");
		}
	}
	
	// 직원 정보 수정
	@PostMapping("/update-staff")
	@ResponseBody
	public String  updateStaff(StaffVO staffVO) {
		log.info("직원 정보 수정 버튼 클릭 함수 진입");
		boolean result = false;
		result = staffService.modifyRegion(staffVO);
		if(!result) {
			log.info("직원 정보 수정 중 오류 발생");
			  return "fail"; // 실패면 fail
		}
		 return "success"; // 
	}
	
	// 아이디 중복체크(GET /signup/checkId?user_id=xxx)
	@GetMapping(value = "/checkId", produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> checkId(StaffVO vo) {
		log.info("아이디 중복 체크 요청: " +  vo.getStaff_id());
		StaffVO staff = staffService.checkId(vo.getStaff_id());
		if (staff == null) {
			return ResponseEntity.ok("사용가능");
		} else {
			return ResponseEntity.ok("이미 사용 중인 아이디입니다.");
		}
	}
	
	// 직원 계정 생성
	@PostMapping("/staff")
	public String registerNewStaff(StaffVO vo, RedirectAttributes redirectAttrs) {
		log.info("직원 생성 요청");
		boolean result = staffService.register(vo);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "직원 계정이 생성되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "계정 생성 실패");
		}
		return "redirect:/admin/account";
	}
	
	
	// 관리자 계정 생성
	@PostMapping("/admin")
	public String guestLogin(StaffVO vo, RedirectAttributes redirectAttrs) {
		log.info("직원 생성 요청");
		boolean result = staffService.register(vo);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "관리자 계정이 생성되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "계정 생성 실패");
		}
		return "redirect:/admin/account";
	}
}
