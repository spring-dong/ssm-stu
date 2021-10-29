package com.keafmd.controller;

import com.keafmd.entity.StudentInfo;
import com.keafmd.service.StudentService;
import com.keafmd.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guandezhi
 * @Date: 2019/3/9 11:18
 */
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletResponse response) throws Exception {
        String[] headers = {"学号","姓名","性别","出生日期","班级号", "联系电话", "入校日期", "家庭地址", "备注"};
        String fileName = "学生表";
        Map<String, Object> studentMap = new HashMap();
        List<StudentInfo> studentList = studentService.queryStuList();
        studentMap.put("headers", headers);
        studentMap.put("dataList", studentList);
        studentMap.put("fileName", fileName);
        List<Map> mapList = new ArrayList();
        mapList.add(studentMap);
        ExcelUtil.exportMultisheetExcel(fileName, mapList, response);
        return "success";
    }

//    @RequestMapping(value = "/readExcel")
//    public String readExcel() throws Exception {
//        String filePath = "F:\\student.xls";
//        List<Map<String, String>> mapList = ExcelUtil.readExcel(filePath, 0);
//        log.info("mapList:" + mapList);
//        return "success";
//    }

}