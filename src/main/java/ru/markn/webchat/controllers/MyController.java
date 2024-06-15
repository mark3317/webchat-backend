package ru.markn.webchat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MyController {

    @RequestMapping("/index")
    public String showIndexView() {
        return "learnCss";
    }
}
