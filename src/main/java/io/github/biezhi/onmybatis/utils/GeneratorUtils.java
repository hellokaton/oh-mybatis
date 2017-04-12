package io.github.biezhi.onmybatis.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratorUtils {

    private static final int BUFFER = 2048;

    /**
     * 执行压缩操作
     *
     * @param srcPathName 被压缩的文件/文件夹
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File file = new File(sourceFilePath);
        if (!file.exists()) {
            throw new RuntimeException(sourceFilePath + "不存在！");
        }
        try {
            File baseZipPath = new File(zipFilePath);
            if (!baseZipPath.exists()) {
                baseZipPath.mkdirs();
            }
            File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compressByType(file, out, basedir);
            out.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
     *
     * @param file
     * @param out
     * @param basedir
     */
    public static void compressByType(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     *
     * @param dir
     * @param out
     * @param basedir
     */
    public static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compressByType(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     *
     * @param file
     * @param out
     * @param basedir
     */
    public static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            String[] arr = basedir.split("/");
            String dirStr = "src/";
            if (arr[0].contains("src")) {
                for (int i = 1; i < arr.length; i++) {
                    dirStr += arr[i] + "/";
                }
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dirStr + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
