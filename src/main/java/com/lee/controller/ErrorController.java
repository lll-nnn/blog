package com.lee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ErrorController {

    @RequestMapping("/error")
    public String error(Model model, HttpSession session){
        if (session.getAttribute("loginUser") != null){
            model.addAttribute("loginUser", session.getAttribute("loginUser"));
            model.addAttribute("isLogin", true);
        }
        return "error";
    }
}
