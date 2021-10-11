package com.hd.permissiondetect.util;

import android.os.Build;

import androidx.annotation.NonNull;

import com.hd.permissiondetect.LOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Created by Administrator on 2017/9/19.
 * <p>
 * 文件基本辅助类：
 * 只进行大小，删除，重命名,移动，新建，获取文件基本信息操作
 * 一般扩充性文件类：FileExtUtil
 * 文件大小等格式化：FileFormatUtil
 * 文件读写：FileRWExtUtil
 * PCM文件操作：PcmFileUtils
 * 文件流操作类:IOUtil
 */

public class FileUtils {
    private FileUtils() {
    }

    private static final String TAG = "FileUtils";

    /**
     * 更新文件时间
     * 解决：sonar scanner 扫描Bug提示
     *
     * @param file 文件
     */
    public static void setLastModified(@NonNull File file) {
        boolean result = file.setLastModified(System.currentTimeMillis());
        if (result) {
            // sonar scanner 扫描提示
        }
    }

    /**
     * 判断时间间隔是否超过24h
     *
     * @param millionSecond 毫秒
     * @return
     */
    public static boolean timeRangeIsLarge24(long millionSecond) {
        boolean flag = false;
        if (System.currentTimeMillis() - millionSecond >= TimeConst.DAY) {
            flag = true;
        }
        return flag;
    }


    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean safeDelete(File file) {
        try {
            if (file != null && file.exists()) {
                return deleteFileByNio(file);
            }
        } catch (Exception e) {
            // 文件权限未授予
        }
        return false;
    }


    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        return getFileSize(new File(filePath));
    }

    public static long getFileSize(File file) {
        return file.length();
    }


    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     */
    public static long getFileSizes(File f) {
        long size = 0;
        File[] listFiles = f.listFiles();
        if (listFiles != null) {
            for (File subFile : listFiles) {
                if (subFile.isDirectory()) {
                    size = size + getFileSizes(subFile);
                } else {
                    size = size + getFileSize(subFile);
                }
            }
        }
        return size;
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        LOG.d(TAG, "删除文件 file:" + file.getAbsolutePath());
        if (file.isFile()) {
            deleteFileByNio(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    deleteFile(value);
                }
            }
            deleteFileByNio(file);
        }
    }

    /**
     * Path就是取代File的
     * 使用java最小nio文件库删除文件
     *
     * @param file
     */
    public static boolean deleteFileByNio(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.delete(file.toPath());
            } catch (Exception e) {
                return false;
            }
        } else {
            return file.delete();
        }
        return true;
    }

    /**
     * 清理文件夹,文件夹不存在，新建该文件夹。   文件夹存在，则删除所有子文件
     *
     * @param file
     */
    public static void clearFolderSubFiles(File file) {
        if (!file.exists()) {
            file.mkdirs();
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File cache : files) {
                        safeDelete(cache);
                    }
                }
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static boolean renameFile(String oldPath, String newPath) {
        LOG.d(TAG, "renameFile oldPath:" + oldPath + " newPath:" + newPath);
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        // 执行重命名
        return oleFile.renameTo(newFile);
    }


    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     * @throws IOException
     */
    public static boolean copyFile(File source, File dest) {
        try (FileChannel inputChannel = new FileInputStream(source).getChannel()) {
            try (FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getTrackLogDetail(String filePath) {
        StringBuffer fileStr = new StringBuffer();
        File file = new File(filePath);
        BufferedReader reader = null;

        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            int line = 1;

            for (String tempString = null; (tempString = reader.readLine()) != null; ++line) {
                fileStr.append(tempString).append("\n");
                System.out.println("line " + line + ": " + tempString);
            }

            reader.close();
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var14) {
                }
            }

        }

        return fileStr.toString();
    }
}
