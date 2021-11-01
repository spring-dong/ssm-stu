package com.keafmd.util;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

/**
 * 导出Excel工具类
 *
 * @author 大脑补丁
 *
 */
@Slf4j
public class ExportExcelUtil<T> {

    // 新版Excel文件后缀
    private static final String EXCEL_SUFFIX = ".xlsx";

    /**
     * 导出多Sheet的Excel到HttpServletResponse流中
     *
     * @param fileName
     *            另存为文件名
     * @param sheetNames
     *            工作簿中的多张Sheet工作表的名称列表
     * @param titleName
     *            表格的标题名称（没有标题，则传null）
     * @param headers
     *            表头列表
     * @param dataList
     *            要导出的数据源
     * @param HttpServletResponse
     *            Http响应
     * @param pattern
     *            时间类型数据的格式,默认UTC格式
     * @param isExportNullField
     *            空字段是否导出（true：导出，false:不导出）
     * @see 导出Excel格式：表头内容居中，字体略大于正文，颜色深灰色。正文文本类型对齐方式居左，数字类型对齐方式居右。仅有数据* 的单元格，有边框环绕，实体类的属性顺序即为表头顺序
     */
    public static <T> void exportExcel(String fileName, List<String> sheetNames, String titleName, List<String> headers,
                                       List<List<T>> dataLists, HttpServletResponse response, String pattern, boolean isExportNullField) {
        XSSFWorkbook wb = exportAllExcel(sheetNames, titleName, headers, dataLists, pattern, isExportNullField);
        setResponseHeader(response, replaceSpecStr(fileName));
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出多Sheet动态列的Excel到HttpServletResponse流中
     *
     * @param fileName          另存为文件名
     * @param sheetNames        工作簿中的多张Sheet工作表的名称列表
     * @param titleName         表格的标题名称（没有标题，则传null）
     * @param headersList       多个sheet页的表头列表
     * @param dataLists         要导出的数据源
     * @param response          Http响应
     * @param pattern           时间类型数据的格式,默认UTC格式
     * @param isExportNullField 空字段是否导出（true：导出，false:不导出）
     */
    public static void exportDynamicExcel(String fileName, List<String> sheetNames, String titleName,
                                          List<List<String>> headersList, List<List<Map<String, Object>>> dataLists, HttpServletResponse response,
                                          String pattern, boolean isExportNullField) {
        XSSFWorkbook wb = exportDynamicExcelImpl(sheetNames, titleName, headersList, dataLists, pattern,
                isExportNullField);
        setResponseHeader(response, replaceSpecStr(fileName));
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 导出多个Excel并压缩到zip包中
     *
     * @param response          HttpServletResponse
     * @param excelList         多个Excel数据源
     * @param filePath          导出路径（文件夹）
     * @param zipName           压缩包名，如（ABC）
     * @param pattern           Excel中的时间格式
     * @param isExportNullField 是否导出空单元格（空单元格显示为"--"）
     */
    public static void exportExcelsToZip(HttpServletResponse response, List<Map<String, Object>> excelList,
                                         String filePath, String zipName, String pattern, boolean isExportNullField) {
        setZipResponseHeader(response, replaceSpecStr(zipName), true, ".zip");
        long timestamp = System.nanoTime();
        // 本次导出，生成一个excel文件的上级文件夹
        String targetPath = filePath + File.separator + "temp" + File.separator + timestamp + File.separator + zipName;
        for (Map<String, Object> excelMap : excelList) {
            XSSFWorkbook wb = exportDynamicExcelImpl((List<String>) excelMap.get("sheetNames"), null,
                    (List<List<String>>) excelMap.get("headers"),
                    (List<List<Map<String, Object>>>) excelMap.get("dataLists"), pattern, isExportNullField);

            // Excel输出路径示例：/usr/local/tomcat/excels/temp/1649149721911900/报表20210416/xx名称-20210416.xlsx
            String excelPath =
                    targetPath + File.separator + replaceSpecStr(excelMap.get("fileName").toString()) + EXCEL_SUFFIX;
            try {
                File file = new File(excelPath);
                FileUtils.forceMkdirParent(file);
                FileOutputStream outputStream = new FileOutputStream(file);
                wb.write(outputStream);
                outputStream.close();
                wb.close();
                log.info("成功导出Excel：" + file.getName());
            } catch (IOException e) {
                log.warn("导出Excel时，创建文件出错");
                e.printStackTrace();
            } finally {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 将所有Excel压缩，并写入到response中
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        try {
            // zip输出路径示例：/usr/local/tomcat/excels/temp/1649149721911900/日报20210416.zip
            String outputPath =
                    filePath + File.separator + "temp" + File.separator + timestamp + File.separator + zipName
                            + ZipUtils.ZIP_SUFFIX;
            ZipUtils.compress(targetPath, outputPath);
            File zipFile = new File(outputPath);
            fileInputStream = new FileInputStream(zipFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            out = response.getOutputStream();
            byte[] bytes = new byte[bufferedInputStream.available()];
            // 必须加此行代码，否则下载的Zip包损坏
            int read = bufferedInputStream.read(bytes);
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置响应的类型、编码和文件名称
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.reset();
            response.setContentType("application/msexcel");// 设置生成的文件类型
            response.setCharacterEncoding("UTF-8");// 设置文件头编码方式和文件名
            // 在浏览器中测试生效，postman中文件名为response,无法修改
            response.setHeader("Content-disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(replaceSpecStr(fileName) + EXCEL_SUFFIX, "UTF-8"))));
            // 此设置，可保证web端可以取到文件名
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置Zip下载的响应的类型、编码和文件名称
     *
     * @param response
     * @param fileName
     * @param urlEncode
     * @param zipSuffix
     */
    public static void setZipResponseHeader(HttpServletResponse response, String fileName, boolean urlEncode,
                                            String zipSuffix) {
        try {
            if (zipSuffix == null) {
                zipSuffix = ".zip";
            }
            String downloadName = urlEncode == true ?
                    String.valueOf(URLEncoder.encode(replaceSpecStr(fileName) + zipSuffix, "UTF-8")) :
                    String.valueOf(replaceSpecStr(fileName) + zipSuffix);
            response.reset();
            // 设置生成的文件类型
            response.setContentType("application/x-zip-compressed");
            //response.setContentType("application/octet-stream");
            // 设置文件头编码方式和文件名
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(downloadName));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置响应的类型、编码和文件名称
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName, boolean urlEncode) {
        try {
            String downloadName = urlEncode == true
                    ? String.valueOf(URLEncoder.encode(replaceSpecStr(fileName) + EXCEL_SUFFIX, "UTF-8"))
                    : String.valueOf(replaceSpecStr(fileName) + EXCEL_SUFFIX);
            response.reset();
            response.setContentType("application/msexcel");// 设置生成的文件类型
            response.setCharacterEncoding("UTF-8");// 设置文件头编码方式和文件名
            // 在浏览器中测试生效，postman中文件名为response,无法修改
            response.setHeader("Content-Disposition", "attachment;filename=".concat(downloadName));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 多Sheet导出实现
     *
     * @param sheetNames
     * @param titleName
     * @param headers
     * @param dataLists
     * @param pattern
     * @param isExportNullField
     * @return
     */
    private static <T> XSSFWorkbook exportAllExcel(List<String> sheetNames, String titleName, List<String> headers,
                                                   List<List<T>> dataLists, String pattern, boolean isExportNullField) {
        // 创建一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int i = 0; i < dataLists.size(); i++) {
            // 创建一个工作表
            XSSFSheet sheet = workbook.createSheet(replaceSpecStr(sheetNames.get(i)));
            // 设置单元格列宽度为16个字节
            sheet.setDefaultColumnWidth((short) 16);
            // 创建表头样式
            XSSFCellStyle headersStyle = workbook.createCellStyle();
            headersStyle.setBorderTop(BorderStyle.THIN);
            headersStyle.setBorderBottom(BorderStyle.THIN);
            headersStyle.setBorderLeft(BorderStyle.THIN);
            headersStyle.setBorderRight(BorderStyle.THIN);
            // 表头内容对齐方式：居中
            headersStyle.setAlignment(HorizontalAlignment.CENTER);
            XSSFFont headersFont = workbook.createFont();
            // 设置字体格式
            headersFont.setColor(new XSSFColor(java.awt.Color.DARK_GRAY));
            headersFont.setFontHeightInPoints((short) 14);
            // 表头样式应用生效
            headersStyle.setFont(headersFont);
            XSSFCellStyle dataSetStyle = workbook.createCellStyle();
            // 正文单元格边框样式
            dataSetStyle.setBorderBottom(BorderStyle.THIN);
            dataSetStyle.setBorderRight(BorderStyle.THIN);
            dataSetStyle.setBorderLeft(BorderStyle.THIN);
            // 数据内容对齐方式：居左
            // dataSetStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            XSSFFont dataSetFont = workbook.createFont();
            // 正文字体颜色
            dataSetFont.setColor(new XSSFColor(java.awt.Color.BLACK));
            // 为正文设置样式
            dataSetStyle.setFont(dataSetFont);
            if (titleName != null && titleName != "") {
                XSSFCellStyle titleStyle = workbook.createCellStyle();
                // 将首行合并居中作为标题栏
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
                XSSFFont titleFont = workbook.createFont();
                // 设置标题字体大小
                titleFont.setFontHeightInPoints((short) 20);
                // 设置标题字体样式
                titleStyle.setFont(titleFont);
                // 创建标题行并设置样式
                XSSFRow titleRow = sheet.createRow(0);
                XSSFCell titleCell = titleRow.createCell(0);
                titleCell.setCellStyle(titleStyle);
                titleCell.setCellValue(titleName);
            }
            int index = titleName == null || titleName.equals("") ? 0 : 1;
            // 创建表头并设置样式
            XSSFRow row = sheet.createRow(index);
            for (short j = 0; j < headers.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellStyle(headersStyle);
                XSSFRichTextString text = new XSSFRichTextString(headers.get(j));
                cell.setCellValue(text);
            }
            // 导出正文数据，并设置其样式
            Iterator<?> it = dataLists.get(i).iterator();
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                Object entity = it.next();
                // 利用反射，根据实体类属性的先后顺序，动态调用其getXxx()方法，得到属性值
                Field[] fields = entity.getClass().getDeclaredFields();
                for (short k = 0; k < fields.length; k++) {
                    XSSFCell cell = row.createCell(k);
                    Field field = fields[k];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        @SuppressWarnings("rawtypes")
                        Class entityClass = entity.getClass();
                        @SuppressWarnings("unchecked")
                        Method getMethod = entityClass.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(entity, new Object[] {});
                        String textValue = null;
                        // 如果是时间类型,格式化
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            pattern = pattern == null || pattern.equals("") ? "yyyy-MM-dd'T'HH:mm:ss'Z'" : pattern;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else {
                            // 若字段为空且允许导出空字段，则将null导出为""
                            textValue = value == null && isExportNullField ? "" : value.toString();
                        }
                        if (!textValue.equals("")) {
                            // 有数据时边框环绕
                            cell.setCellStyle(dataSetStyle);
                            // 正则判断是否为数值
                            Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if (matcher.matches()) {
                                // 是数字当作double处理，整型也不会补充小数点
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                // 不是数字类型作为文本输出
                                cell.setCellValue(textValue);
                            }
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * 多Sheet导出动态列到Excel实现
     *
     * @param sheetNames
     * @param titleName
     * @param headersList       多个Sheet页的表头
     * @param dataLists
     * @param pattern           时间格式
     * @param isExportNullField 是否导出空字段
     * @return
     */
    private static XSSFWorkbook exportDynamicExcelImpl(List<String> sheetNames, String titleName,
                                                       List<List<String>> headersList, List<List<Map<String, Object>>> dataLists, String pattern,
                                                       boolean isExportNullField) {
        // 创建一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int i = 0; i < dataLists.size(); i++) {
            // 创建一个工作表
            XSSFSheet sheet = workbook.createSheet(replaceSpecStr(sheetNames.get(i)));
            // 设置单元格列宽度为16个字节
            sheet.setDefaultColumnWidth((short) 16);
            // 创建表头样式
            XSSFCellStyle headersStyle = workbook.createCellStyle();
            headersStyle.setBorderTop(BorderStyle.THIN);
            headersStyle.setBorderBottom(BorderStyle.THIN);
            headersStyle.setBorderLeft(BorderStyle.THIN);
            headersStyle.setBorderRight(BorderStyle.THIN);
            // 表头内容对齐方式：居中
            headersStyle.setAlignment(HorizontalAlignment.CENTER);
            XSSFFont headersFont = workbook.createFont();
            // 设置字体格式
            headersFont.setColor(new XSSFColor(java.awt.Color.DARK_GRAY, new DefaultIndexedColorMap()));
            headersFont.setFontHeightInPoints((short) 12);
            // 表头样式应用生效
            headersStyle.setFont(headersFont);
            // 设置单元格内内容换行
            headersStyle.setWrapText(true);
            XSSFCellStyle dataSetStyle = workbook.createCellStyle();
            // 正文单元格边框样式
            dataSetStyle.setBorderBottom(BorderStyle.THIN);
            dataSetStyle.setBorderRight(BorderStyle.THIN);
            dataSetStyle.setBorderLeft(BorderStyle.THIN);
            // 数据内容对齐方式：居左
            // dataSetStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            XSSFFont dataSetFont = workbook.createFont();
            // 正文字体颜色
            dataSetFont.setColor(new XSSFColor(java.awt.Color.BLACK, new DefaultIndexedColorMap()));
            // 为正文设置样式
            dataSetStyle.setFont(dataSetFont);
            // 获取当前Sheet页的表头
            List<String> headers = headersList.get(i);
            if (titleName != null && titleName != "") {
                XSSFCellStyle titleStyle = workbook.createCellStyle();
                // 将首行合并居中作为标题栏
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
                XSSFFont titleFont = workbook.createFont();
                // 设置标题字体大小
                titleFont.setFontHeightInPoints((short) 20);
                // 设置标题字体样式
                titleStyle.setFont(titleFont);
                // 创建标题行并设置样式
                XSSFRow titleRow = sheet.createRow(0);
                XSSFCell titleCell = titleRow.createCell(0);
                titleCell.setCellStyle(titleStyle);
                titleCell.setCellValue(titleName);
            }
            int index = titleName == null || titleName.equals("") ? 0 : 1;
            // 创建表头并设置样式
            XSSFRow row = sheet.createRow(index);
            for (short j = 0; j < headers.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellStyle(headersStyle);
                XSSFRichTextString text = new XSSFRichTextString(headers.get(j));
                cell.setCellValue(text);
            }
            // 导出正文数据，并设置其样式
            ListIterator<Map<String, Object>> it = dataLists.get(i).listIterator();
            while (it.hasNext()) {
                try {
                    index++;
                    row = sheet.createRow(index);
                    Map<String, Object> map = it.next();
                    headers = new ArrayList<String>(map.keySet());
                    List<Object> values = new ArrayList<Object>(map.values());
                    for (int k = 0; k < map.keySet().size(); k++) {
                        try {
                            XSSFCell cell = row.createCell(k);
                            cell.setCellStyle(dataSetStyle);
                            String textValue = null;
                            Object value = values.get(k);
                            // 如果是时间类型,格式化
                            if (value instanceof Date) {
                                Date date = (Date) value;
                                pattern = pattern == null || pattern.equals("") ? "yyyy-MM-dd'T'HH:mm:ss'Z'" : pattern;
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                                textValue = sdf.format(date);
                            } else {
                                // 若字段为空且用户允许导出空字段，则将null导出为"--"
                                textValue = value == null && isExportNullField ? "--" : value.toString();
                            }
                            if (!textValue.equals("")) {
                                // 有数据时边框环绕
                                //cell.setCellStyle(dataSetStyle);
                                // 正则判断是否为数值
                                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                                Matcher matcher = p.matcher(textValue);
                                if (matcher.matches()) {
                                    // 是数字当作double处理，整型也不会补充小数点
                                    cell.setCellValue(Double.parseDouble(textValue));
                                } else {
                                    // 不是数字类型作为文本输出
                                    cell.setCellValue(textValue);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;

    }

    /**
     * 导出Excel到HttpServletResponse流中
     *
     * @param fileName
     *            另存为文件名
     * @param sheetName
     *            工作簿中的一张工作表的名称
     * @param titleName
     *            表格的标题名称（无需创建，则传null）
     * @param headers
     *            表头列表
     * @param dataList
     *            要导出的数据源
     * @param response
     *            Http响应
     * @param pattern
     *            时间类型数据的格式,默认UTC格式
     * @param isExportNullField
     *            空字段是否导出（true：导出，false:不导出）
     */
    @Deprecated
    public static void exportOneSheetExcel(String fileName, String sheetName, String titleName, List<String> headers,
                                           Collection<Collection<? extends Object>> dataList, HttpServletResponse response, String pattern,
                                           boolean isExportNullField) {
        XSSFWorkbook wb = exportExcelToWorkbook(sheetName, titleName, headers, dataList, pattern, isExportNullField);
        setResponseHeader(response, replaceSpecStr(fileName));
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出核心实现
     *
     * @param sheetName
     * @param titleName
     * @param headers
     * @param dataList
     * @param pattern
     * @param isExportNullField
     * @return XSSFWorkbook
     */
    @Deprecated
    private static XSSFWorkbook exportExcelToWorkbook(String sheetName, String titleName, List<String> headers,
                                                      Collection<?> dataList, String pattern, boolean isExportNullField) {
        // 创建一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        XSSFSheet sheet = workbook.createSheet(replaceSpecStr(sheetName));
        // 设置单元格列宽度为16个字节
        sheet.setDefaultColumnWidth((short) 16);
        // 创建表头样式
        XSSFCellStyle headersStyle = workbook.createCellStyle();
        headersStyle.setBorderTop(BorderStyle.THIN);
        headersStyle.setBorderBottom(BorderStyle.THIN);
        headersStyle.setBorderLeft(BorderStyle.THIN);
        headersStyle.setBorderRight(BorderStyle.THIN);
        // 表头内容对齐方式：居中
        headersStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont headersFont = workbook.createFont();
        // 设置字体格式
        headersFont.setColor(new XSSFColor(java.awt.Color.DARK_GRAY));
        headersFont.setFontHeightInPoints((short) 14);
        // 表头样式应用生效
        headersStyle.setFont(headersFont);
        XSSFCellStyle dataSetStyle = workbook.createCellStyle();
        // 正文单元格边框样式
        dataSetStyle.setBorderBottom(BorderStyle.THIN);
        dataSetStyle.setBorderRight(BorderStyle.THIN);
        dataSetStyle.setBorderLeft(BorderStyle.THIN);
        // 数据内容对齐方式：居左
        // dataSetStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        XSSFFont dataSetFont = workbook.createFont();
        // 正文字体颜色
        dataSetFont.setColor(new XSSFColor(java.awt.Color.BLACK));
        // 为正文设置样式
        dataSetStyle.setFont(dataSetFont);
        if (titleName != null && titleName != "") {
            XSSFCellStyle titleStyle = workbook.createCellStyle();
            // 将首行合并居中作为标题栏
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
            XSSFFont titleFont = workbook.createFont();
            // 设置标题字体大小
            titleFont.setFontHeightInPoints((short) 20);
            // 设置标题字体样式
            titleStyle.setFont(titleFont);
            // 创建标题行并设置样式
            XSSFRow titleRow = sheet.createRow(0);
            XSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(titleName);
        }
        int index = titleName == null || titleName.equals("") ? 0 : 1;
        // 创建表头并设置样式
        XSSFRow row = sheet.createRow(index);
        for (short i = 0; i < headers.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(headersStyle);
            XSSFRichTextString text = new XSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
        }
        // 导出正文数据，并设置其样式
        Iterator<?> it = dataList.iterator();
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Object entity = it.next();
            // 利用反射，根据实体类属性的先后顺序，动态调用其getXxx()方法，得到属性值
            Field[] fields = entity.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                XSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    @SuppressWarnings("rawtypes")
                    Class entityClass = entity.getClass();
                    @SuppressWarnings("unchecked")
                    Method getMethod = entityClass.getMethod(getMethodName, new Class[] {});
                    Object value = getMethod.invoke(entity, new Object[] {});
                    String textValue = null;
                    // 如果是时间类型,格式化
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        pattern = pattern == null || pattern.equals("") ? "yyyy-MM-dd'T'HH:mm:ss'Z'" : pattern;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 若字段为空且允许导出空字段，则将null导出为""
                        textValue = value == null && isExportNullField ? "" : value.toString();
                    }
                    if (!textValue.equals("")) {
                        // 有数据时边框环绕
                        cell.setCellStyle(dataSetStyle);
                        // 正则判断是否为数值
                        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理，整型也不会补充小数点
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            // 不是数字类型作为文本输出
                            cell.setCellValue(textValue);
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }

    /**
     * 正则替换所有特殊字符
     *
     * @param orgStr
     * @return
     */
    public static String replaceSpecStr(String orgStr) {
        if (null != orgStr && !"".equals(orgStr.trim())) {
            String regEx = "[\\\\|:/\"<>?*\\[\\] ]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(orgStr);
            return m.replaceAll("_");
        }
        return null;
    }

}

