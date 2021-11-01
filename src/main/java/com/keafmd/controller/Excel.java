package com.keafmd.controller;

import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import com.keafmd.util.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class Excel {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/myevent/export")
    public void exportEvent(HttpServletResponse response) {

        List<StudentInfo> studentInfoList = studentService.queryStuList();
        List<List<StudentInfo>> dataLists=new ArrayList<>();
        dataLists.add(studentInfoList);
        if (dataLists != null && dataLists.size() > 0) {
            try {

                List<String> sheetNames = Arrays.asList("sheet页1");
                List < String > headers = new ArrayList<String>(Arrays.asList("学号","姓名","性别","出生日期","班级号", "联系电话", "入校日期", "家庭地址", "备注"));
                String excelName = "你的Excel名称";
                ExportExcelUtil.exportExcel(excelName, sheetNames, null, headers, dataLists, response, null, true);
            } catch (Exception e) {
                System.out.println("sksksk");
            }
        }
    }



}
