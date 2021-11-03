package com.keafmd.entity;

import lombok.*;

import java.util.Date;
import java.io.Serializable;

/**
 * (StudentInfo)实体类
 *
 * @author makejava
 * @since 2021-10-28 14:47:22
 */
@SuppressWarnings("serial")
@Data
public class StudentInfo implements Serializable { /**
     * 学号
     */
    private Integer stu_id;
    /**
     * 姓名
     */

    private String stu_name;
    /**
     * 性别
     */

    private String stu_sex;
    /**
     * 出生日期
     */

    private Date born_data;
    /**
     * 班级号
     */

    private Integer class_no;
    /**
     * 联系电话
     */

    private String tele_number;
    /**
     * 入校日期
     */

    private Date ru_data;
    /**
     * 家庭住址
     */
    private String stu_address;
    /**
     * 备注
     */

    private String stu_comment;

    private ClassInfo classInfo;
}

