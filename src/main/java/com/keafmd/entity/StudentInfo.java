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
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo implements Serializable {
    private static final long serialVersionUID = 903888550955912447L;
    /**
     * 学号
     */
    private Integer stu_id;
    /**
     * 姓名
     */
    @NonNull
    private String stu_name;
    /**
     * 性别
     */
    @NonNull
    private String stu_sex;
    /**
     * 出生日期
     */
    @NonNull
    private Date born_data;
    /**
     * 班级号
     */
    @NonNull
    private Integer class_no;
    /**
     * 联系电话
     */
    @NonNull
    private String tele_number;
    /**
     * 入校日期
     */
    @NonNull
    private Date ru_data;
    /**
     * 家庭住址
     */
    @NonNull
    private String stu_address;
    /**
     * 备注
     */
    @NonNull
    private String stu_comment;
}

