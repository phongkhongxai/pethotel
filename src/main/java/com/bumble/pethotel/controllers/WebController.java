package com.bumble.pethotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/success")
    public String paymentSuccess() {
        return "success"; // Render trang success.html
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "cancel"; // Render trang cancel.html
    }
}
