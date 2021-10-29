package com.keafmd.service.impl;

import com.keafmd.dao.StudentDao;
import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;


    @Override
    public List<StudentInfo> queryStuList() {
        return studentDao.stuList();
    }
}
