package com.keafmd.dao;

import com.keafmd.entity.StudentInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {
    @Select("select * from student_info")
    @Results(id="classMap",value = {
            @Result(id = true,column = "stu_id",property = "stu_id"),
            @Result(column = "stu_name",   property = "stu_name"),
            @Result(column = "stu_sex",    property = "stu_sex"),
            @Result(column = "born_data",  property = "born_data"),
            @Result(column = "tele_number",property = "tele_number"),
            @Result(column = "ru_data",    property = "ru_data"),
            @Result(column = "stu_address",property = "stu_address"),
            @Result(column = "stu_comment",property = "stu_comment"),
            @Result(property = "classInfo",column = "class_no",one=@One(select="com.keafmd.dao.ClassDao.findById",fetchType= FetchType.EAGER))
    })
    public List<StudentInfo> stuList();


    @Select("select * from student_info where class_no=#{class_no}")
    public List<StudentInfo> findByClassNo(Integer class_no);
}
