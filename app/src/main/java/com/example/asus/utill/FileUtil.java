package com.example.asus.utill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ASUS on 2016/7/10.
 */
public class FileUtil {
    /**
     * 打开SD卡中一个文件夹下的所有的png,jpg文件
     * @param address
     * @return
     */
    public static List<String> getAllPngJpgFiles(String address) {

        List<String> list = new ArrayList<String>();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return null;

        String SDCardPath = Environment.getExternalStorageDirectory().toString();
        String filePath = SDCardPath + "/" + address;
        File file = null;

        try {
            file = new File(filePath);
            if (file != null) {
                File[] imgFiles = file.listFiles();
                for (File imgFile : imgFiles) {
                    String fileName = imgFile.getName();
                    Pattern pattern = Pattern.compile(".*\\.(jpg)|(png)$");
                    Matcher matcher = pattern.matcher(fileName);
                    if (matcher.matches()) {
                        String imgFilePath = imgFile.getPath();
                        list.add(imgFilePath);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 缩小一张Bitmap到一定尺寸
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap small(Bitmap bitmap, int newWidth, int newHeight) {
        Matrix matrix = new Matrix();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
