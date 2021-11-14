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
    public List<StudentInfo> getName(){
        return studentService.queryStuList();
    }


    @RequestMapping("/time")
    public Long time() throws InterruptedException {
        studentService.updateWithTimePessimistic();
        return System.currentTimeMillis();
    }

    @RequestMapping("/notime")
    public Long notime() throws InterruptedException {
        studentService.updatePessimistic();
        return System.currentTimeMillis();
    }
}
