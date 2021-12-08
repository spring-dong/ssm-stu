package com.keafmd.controller;

import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stu")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("/list")
    public String getName(){
        studentService.queryStuList();
        return "hello world";
    }

}
