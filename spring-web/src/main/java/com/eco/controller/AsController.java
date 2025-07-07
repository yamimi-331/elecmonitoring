package com.eco.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        List<ASListDTO> asList = asService.getAsList(userType, staffCd);
        model.addAttribute("asList", asList);

		return "/as/asOrder";
	}
	
	// 기간별 조회
	@GetMapping("/schedule")
	@ResponseBody
	public List<ASVO> getScheduleByDate(@RequestParam String date, HttpSession session) {
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


	// AS List 표의 하나의 행의 상세정보 버튼 클릭
	@GetMapping(value = "/detail/{as_cd}", produces = "application/json")
	@ResponseBody
	public ASListDTO getAsDetail(@PathVariable("as_cd") int as_cd) {
		ASListDTO result =  asService.getAsDetail(as_cd);
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
	
	
}
