package com.example.multidatasource.demo.controller;

import com.example.multidatasource.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("demoV")
public class DemoViewController {

    @Autowired
    DemoService demoService;

    @RequestMapping("index")
    public String index() {
        List<Map<String, String>> list = demoService.getStudent();
        System.out.println(list.toString());
        return "demo/index";
    }
}
