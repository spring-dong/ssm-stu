package com.keafmd.dao;

import com.keafmd.entity.StudentInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {

    @Select("select * from student_info")
    public List<StudentInfo> stuList();

    @Select("select * from student_info  where stu_id=#{stuId} for update")
    public StudentInfo findByIdForUpdate(Integer stuId);


    @Update("update student_info set stu_name=#{stu_name} where stu_id=#{stu_id} ")
    public void update(StudentInfo studentInfo);



}
