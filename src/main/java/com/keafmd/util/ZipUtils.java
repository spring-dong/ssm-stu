package com.keafmd.util;

/**
 * @description: 压缩工具类
 * @author: 大脑补丁
 * @create: 2021-04-16 15:51
 */

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class ZipUtils {
    static final int BUFFER = 8192;
    static final String ZIP_SUFFIX = ".zip";

    /**
     * 压缩文件
     *
     * @param srcPath 要压缩的文件或文件夹（如：/usr/local/目录）
     * @param zipPath 输出的zip文件路径（如：/usr/local/abc.zip）
     * @throws IOException
     */
    public static void compress(String srcPath, String zipPath) throws IOException {
        File srcFile = new File(srcPath);
        File zipFile = new File(zipPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }
        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } finally {
            if (null != zipOut) {
                zipOut.close();
                out = null;
            }

            if (null != out) {
                out.close();
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File directory, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + directory.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }

    /**
     * 解压文件
     *
     * @param zipFile
     * @param dstPath
     * @throws IOException
     */
    public static void decompress(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                ;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }
        }
        zip.close();
    }

    public static void main(String[] args) throws Exception {
        String targetFolderPath = "/Users/test/zipFile/zipFolder";
        String rawZipFilePath = "/Users/test/zipFile/raw.zip";
        String newZipFilePath = "/Users/test/zipFile/new.zip";

        //将Zip文件解压缩到目标目录
        decompress(rawZipFilePath, targetFolderPath);

        //将目标目录的文件压缩成Zip文件
        compress(targetFolderPath, newZipFilePath);

    }
}


