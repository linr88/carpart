package com.xiupeilian.carpart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/staff")
public class Staffcontroller {

    @RequestMapping("/staffList")
    public String staffList(){
    return  "index/staffList";
    }
}
