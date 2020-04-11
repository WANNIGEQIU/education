package com.galaxy.course.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestCon {
    @Autowired
    private Test test;
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        int temp = 0;
        for (int i = 0; i < 2000; i++) { ;
            test.test(i);
            test.tet(i+1);
            temp = i;
        }
        return "hello"+temp;
    }
}
