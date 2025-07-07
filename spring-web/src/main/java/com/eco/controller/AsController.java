package com.eco.controller;

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
import com.eco.domain.StaffVO;
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

	// 클릭시 
	@GetMapping("/detail/{as_cd}")
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
