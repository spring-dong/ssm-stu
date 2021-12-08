package com.keafmd.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yanlin
 * @version v1.3
 * @date 2018-10-18 下午3:16
 * @since v8.0
 **/
public class StudentInfo implements Serializable {
    private static final long serialVersionUID = 7003907324788760110L;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Min(value = 2, message = "不能小于2")
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public StudentInfo() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}