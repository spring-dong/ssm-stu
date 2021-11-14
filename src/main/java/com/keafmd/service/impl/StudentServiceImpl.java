package com.keafmd.service.impl;

import com.keafmd.dao.StudentDao;
import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;


    @Override
    public List<StudentInfo> queryStuList() {
        return studentDao.stuList();
    }

    @Override
    public StudentInfo findByIdForUpdate(Integer stuId) {
        return null;
    }

    @Override
    public void update(StudentInfo studentInfo) {
        studentDao.update(studentInfo);
    }

    @Transactional
    public StudentInfo getUserInfoByUserNamePessimistic() {
        return studentDao.findByIdForUpdate(33);
    }

    @Transactional
    public void updateWithTimePessimistic() throws InterruptedException {

        StudentInfo student = studentDao.findByIdForUpdate(33);

        Thread.sleep(50000);
        student.setStu_name("aptexdjjd");

        studentDao.update(student);



    }

    @Transactional
    public void updatePessimistic() throws InterruptedException {

        StudentInfo student = studentDao.findByIdForUpdate(33);
        student.setStu_name("jjjjjjjjj");
        studentDao.update(student);


    }


}
