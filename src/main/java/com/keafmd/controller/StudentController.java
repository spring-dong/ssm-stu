package com.keafmd.controller;

import com.keafmd.dao.ClassDao;
import com.keafmd.entity.ClassInfo;
import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stu")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassDao classDao;

    @RequestMapping("/list")
    public List<StudentInfo> getName(){
        return studentService.queryStuList();
    }
    @RequestMapping("/clist")
    public List<ClassInfo> gectName(){
        return classDao.findAll();
    }
}
