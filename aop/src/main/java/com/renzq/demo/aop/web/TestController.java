package com.renzq.demo.aop.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 16:35
 * Description:
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "echo....";
    }
}