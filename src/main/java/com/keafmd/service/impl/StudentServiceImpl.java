package com.keafmd.service.impl;

import com.keafmd.dao.StudentDao;
import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    


    @Override
    public List<StudentInfo> queryStuList() {
        List<StudentInfo> studentInfoList=new ArrayList<>();
        return studentInfoList ;
    }





}
