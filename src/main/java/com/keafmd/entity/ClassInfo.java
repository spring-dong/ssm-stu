package com.keafmd.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * (ClassInfo)实体类
 *
 * @author makejava
 * @since 2021-10-19 18:07:36
 */
@Data
public class ClassInfo implements Serializable {
    private static final long serialVersionUID = 854851427173717968L;
    /**
     * 班级号
     */
    private Integer class_no;
    /**
     * 班级名称
     */
    private String class_name;
    /**
     * 班主任
     */
    private String class_direcor;
    /**
     * 专业
     */
    private String class_profession;

   private List<StudentInfo> studentInfo;


}

