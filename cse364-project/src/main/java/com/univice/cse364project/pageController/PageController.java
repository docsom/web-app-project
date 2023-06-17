package com.univice.cse364project.pageController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/mypage")
    public String myPage(){
        return "my-page";
    }

    @GetMapping("/devicelist")
    public String devicesPage(){
        return "device";
    }
}
