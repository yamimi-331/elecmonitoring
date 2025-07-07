package com.eco.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;
import com.eco.domain.StaffVO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.domain.UserVO;
import com.eco.service.AsService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/as")
public class AsController {
	private final AsService asService;

	// as신청폼 접속
	@GetMapping("/form")
	public String asForm(@RequestParam(required = false) String selectedDate, HttpSession session,
			RedirectAttributes redirectAttrs) {
		log.info("as신청 페이지 요청");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		if (user == null) {
			redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
			return "redirect:/login"; // 💡 로그인 페이지 URL에 맞게 수정
		}
		return "/as/asForm";
	}

	// 예약 차 있는 시간 선택 비활성화
	@GetMapping("/form/booked-times")
	@ResponseBody
	public List<String> getBookedTimes(@RequestParam String selectedDate) {
		LocalDate date = LocalDate.parse(selectedDate);
		return asService.getTotalAs(date);
	}

	// 일반회원의 as신청
	@PostMapping("/insertCommon")
	public String registerAsByCommon(ASVO vo, @RequestParam String reserve_time, @RequestParam String reserve_date,
			@RequestParam(required = false) String as_facility_custom,
			@RequestParam(required = false) String as_title_custom, HttpSession session,
			RedirectAttributes redirectAttrs) {
		log.info("일반회원의 as신청");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		vo.setUser_cd(user.getUser_cd());

		// 예약 날짜 시간 합치기
		LocalDate localDate = LocalDate.parse(reserve_date);
		LocalDateTime combinedDateTime = LocalDateTime.parse(localDate.toString() + "T" + reserve_time + ":00");
		vo.setAs_date(combinedDateTime);

		// 기타 입력 처리
		if ("기타".equals(vo.getAs_facility()) && as_facility_custom != null && !as_facility_custom.isBlank()) {
			vo.setAs_facility(as_facility_custom);
		}
		if ("기타".equals(vo.getAs_title()) && as_title_custom != null && !as_title_custom.isBlank()) {
			vo.setAs_title(as_title_custom);
		}

		boolean result = asService.registerAsByCommon(vo);
		log.info("insert 결과: " + result);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "AS 신고가 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "AS 신고에 실패하였습니다.");
		}
		return "redirect:/as/detail";
	}

	// as신고 내역
	@GetMapping("/detail")
	public String asDetail(HttpSession session, RedirectAttributes redirectAttrs, Model model) {
		log.info("as신청 내역 상세 페이지 요청");
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		if (user == null) {
			redirectAttrs.addFlashAttribute("message", "로그인 후 이용해주세요.");
			return "redirect:/login"; // 💡 로그인 페이지 URL에 맞게 수정
		}
		int user_cd = user.getUser_cd();
		List<ASVO> asvo = asService.getUserAsList(user_cd);

		model.addAttribute("userList", asvo);
		return "/as/asDetail";
	}

	// as신고 수정 화면
	@GetMapping("/edit")
	public String asEdit(@RequestParam("as_cd") int as_cd, Model model) {
		ASVO asvo = asService.readAsDetailByUser(as_cd);
		model.addAttribute("asVO", asvo);
		return "/as/asEdit";
	}

	// 일반회원의 as수정
	@PostMapping("/updateCommon")
	public String eidtAsByCommon(ASVO vo, @RequestParam String reserve_time, @RequestParam String reserve_date,
			@RequestParam(required = false) String as_facility_custom,
			@RequestParam(required = false) String as_title_custom, HttpSession session,
			RedirectAttributes redirectAttrs) {
		UserVO user = (UserVO) session.getAttribute("currentUserInfo");
		vo.setUser_cd(user.getUser_cd());

		// 예약 날짜 시간 합치기
		LocalDate localDate = LocalDate.parse(reserve_date);
		LocalDateTime combinedDateTime = LocalDateTime.parse(localDate.toString() + "T" + reserve_time + ":00");
		vo.setAs_date(combinedDateTime);

		// 기타 입력 처리
		if ("기타".equals(vo.getAs_facility()) && as_facility_custom != null && !as_facility_custom.isBlank()) {
			vo.setAs_facility(as_facility_custom);
		}
		if ("기타".equals(vo.getAs_title()) && as_title_custom != null && !as_title_custom.isBlank()) {
			vo.setAs_title(as_title_custom);
		}

		boolean result = asService.editAsListByCommon(vo);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "AS 수정이 완료되었습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "AS 수정에 실패하였습니다.");
		}
		return "redirect:/as/detail";
	}

	// 일반 회원 신고 삭제
	@PostMapping("/cancleCommon")
	public String asCancle(@RequestParam("as_cd") int as_cd, RedirectAttributes redirectAttrs) {
		boolean result = asService.cancleAsListByCommon(as_cd);
		if (result) {
			redirectAttrs.addFlashAttribute("message", "AS 신청을 취소하였습니다.");
		} else {
			redirectAttrs.addFlashAttribute("message", "AS 신청 취소를 실패하였습니다.");
		}
		return "redirect:/as/detail";
	}

	@GetMapping("/order")
	public String asOderPage(HttpSession session, Model model) {
		log.info("asOrder 페이지로 이동");

		String userType = (String) session.getAttribute("userType");
		int staffCd = 0;

		if ("staff".equals(userType)) {
			// staffVO의 staff_cd 가져오기
			StaffVO staff = (StaffVO) session.getAttribute("currentUserInfo");
			staffCd = staff.getStaff_cd();
		}

		List<ASListDTO> asList = asService.getAsDtoList(userType, staffCd);
		for (ASListDTO dto : asList) {
			if (dto.getAs_date() != null) {
				dto.setAs_time(dto.getAs_date().toLocalTime().toString().substring(0, 5));
			}
		}

		// 시간 오름차순 정렬
		asList.sort(Comparator.comparing(ASListDTO::getAs_time));

		model.addAttribute("asList", asList);

		return "/as/asOrder";
	}

	// AS List 표의 하나의 행의 상세정보 버튼 클릭
	@GetMapping(value = "/task/{as_cd}", produces = "application/json")
	@ResponseBody
	public ASListDTO getAsTask(@PathVariable("as_cd") int as_cd) {
		ASListDTO result = asService.getAsTask(as_cd);
		log.info(result);

		return result;
	}

	// 상태정보 업데이트
	@PostMapping("/updateStatus")
	@ResponseBody
	public String updateStatus(@RequestParam("as_cd") int as_cd, @RequestParam("as_status") String as_status) {
		log.info("AS 상태 업데이트 요청: " + as_cd + ", 새 상태: " + as_status);
		asService.updateStatus(as_cd, as_status);
		return "success";
	}

	// 기간별 조회
	@GetMapping("/schedule")
	@ResponseBody
	public List<ASListDTO> getScheduleByDate(@RequestParam String date, HttpSession session) {
		Object obj = session.getAttribute("currentUserInfo");

		if (obj == null) {
			// 로그인 정보 없으면 빈 리스트 반환 (또는 예외처리)
			return List.of();
		}

		log.info(date);
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		log.info(localDate);

		if (obj instanceof UserVO) {
			UserVO user = (UserVO) obj;
			return asService.getScheduleByUserAndDate(user.getUser_cd(), localDate);
		} else if (obj instanceof StaffVO) {
			StaffVO staff = (StaffVO) obj;
			return asService.getScheduleByStaffAndDate(staff.getStaff_cd(), localDate);
		} else {
			// 예외 케이스 처리, 빈 리스트 반환
			return List.of();
		}
	}

}
