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

    public Integer getStu_id() {
        return stu_id;
    }

    public void setStu_id(Integer stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_sex() {
        return stu_sex;
    }

    public void setStu_sex(String stu_sex) {
        this.stu_sex = stu_sex;
    }

    public Date getBorn_data() {
        return born_data;
    }

    public void setBorn_data(Date born_data) {
        this.born_data = born_data;
    }

    public Integer getClass_no() {
        return class_no;
    }

    public void setClass_no(Integer class_no) {
        this.class_no = class_no;
    }

    public String getTele_number() {
        return tele_number;
    }

    public void setTele_number(String tele_number) {
        this.tele_number = tele_number;
    }

    public Date getRu_data() {
        return ru_data;
    }

    public void setRu_data(Date ru_data) {
        this.ru_data = ru_data;
    }

    public String getStu_address() {
        return stu_address;
    }

    public void setStu_address(String stu_address) {
        this.stu_address = stu_address;
    }

    public String getStu_comment() {
        return stu_comment;
    }

    public void setStu_comment(String stu_comment) {
        this.stu_comment = stu_comment;
    }
}

