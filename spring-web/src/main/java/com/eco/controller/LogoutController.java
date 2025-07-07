package com.eco.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping("")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        session.removeAttribute("currentUserInfo");
        session.removeAttribute("userType");
        redirectAttrs.addFlashAttribute("message", "로그아웃이 완료되었습니다.");
        return "redirect:/"; 
    }

}
