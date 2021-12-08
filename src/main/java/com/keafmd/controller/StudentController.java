package com.keafmd.controller;

import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stu")
public class StudentController {
    //日志
    private Logger logger =LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StudentService studentService;

    @RequestMapping("/list")
    public String getName(@Valid @RequestBody StudentInfo student){
        studentService.queryStuList();
        return "hello world";
    }

}
