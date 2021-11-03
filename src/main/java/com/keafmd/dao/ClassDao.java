package com.keafmd.dao;

import com.keafmd.entity.ClassInfo;
import com.keafmd.entity.StudentInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ClassDao {

    @Select("select * from class_info where class_no =#{class_no}")
    public ClassInfo findById(String class_no);


    @Select("select * from class_info")
    @Results(id="userMap",value={
            @Result(id = true,column = "class_no",property = "class_no"),
            @Result(column = "class_no",property = "class_no"),
            @Result(column = "class_name",property = "class_name"),
            @Result(column = "class_direcor",property = "class_direcor"),
            @Result(column = "class_profession",property = "class_profession"),
            @Result(property = "studentInfo" ,column = "class_no",
                    many = @Many(select = "com.keafmd.dao.StudentDao.findByClassNo",
                            fetchType = FetchType.EAGER))

    })
    List<ClassInfo> findAll();



}
