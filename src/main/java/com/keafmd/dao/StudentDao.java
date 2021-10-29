package com.keafmd.dao;

import com.keafmd.entity.StudentInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {

    @Select("select * from student_info")
    public List<StudentInfo> stuList();
}
