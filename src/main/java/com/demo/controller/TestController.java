package com.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class TestController {
    @RequestMapping("/t")
    public String t(HttpServletRequest request){
        String code = request.getParameter("code");
        System.out.println(code);
        return code;
    }

    @RequestMapping("/test")
    public void test(HttpServletResponse response) throws IOException {
        response.getWriter().println("ok");
    }

}
