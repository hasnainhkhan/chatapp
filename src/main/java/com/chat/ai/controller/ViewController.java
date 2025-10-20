package com.chat.ai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/new")
    public String home() {
        return "chat";
    }
    @GetMapping("/")
    public String dashboard() {
    	return "dashboard";
    }
}