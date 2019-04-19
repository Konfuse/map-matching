package com.konfuse.mapmatching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Konfuse
 * @Date: 2019/4/17 14:34
 */
@Controller
public class MapIndexController {
    @RequestMapping("/")
    public String homePage() {
        return "index";
    }
}
