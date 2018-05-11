package com.tingxia.util;

import java.io.*;

public class FileUtil {

    public static boolean saveFile(String filePath, String fileName, FileInputStream fileInputStream){
        File file = new File(filePath);
        if(!file.exists()){
            if(!file.mkdirs()){
                System.err.println("文件夹"+file.getAbsolutePath()+"无法创建！");
                return false;
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath,fileName));
            byte[] flush = new byte[1024];
            int length;
            while ((length = fileInputStream.read(flush)) != -1){
                fileOutputStream.write(flush,0,length);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static FileInputStream readFile(String filePath,String filename){
        File file = new File(filePath,filename);
        if(file.exists() && file.isFile()){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
