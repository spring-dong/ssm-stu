package com.keafmd.service;

import com.keafmd.entity.StudentInfo;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {

    public List<StudentInfo> queryStuList();



}
