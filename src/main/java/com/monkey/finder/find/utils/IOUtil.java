package com.monkey.finder.find.utils;

import com.alibaba.fastjson.util.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Monkey on 2018/9/28.
 */
@Slf4j
public class IOUtil {
    
    //读取特定文件夹下得文件列表
    public static ArrayList<String> getFileList(String FilePath,String type){
        if(type == null){
            log.debug("未指定type");
            type = "";
        }
        if(FilePath == null){
            log.error("FilePath is null");
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(FilePath);
        if(!file.exists()){
            log.debug("文件夹不存在");
            return null;
        }
        final String TYPE = type;
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(TYPE);
            }
        };
        String[] files = file.list(filter);
        for (String file1 : files) {
            arrayList.add(FilePath+file1);
        }
        return arrayList;
    }
    //二维码生成工具
    public static String creatQRcode(String targetPath,String content,int width,int height,String type){
        HashMap<EncodeHintType,Object> hints = new HashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
        hints.put(EncodeHintType.MARGIN,0);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
            String name = UUID.randomUUID().toString().replace("-","");
            String pathName = targetPath+name+"."+type;
            File outputFile = new File(pathName);
            //判断路径是否存在，不存在创建一个
            if(!outputFile.getParentFile().exists()){
                outputFile.getParentFile().mkdirs();
            }
            MatrixToImageWriter.writeToFile(bitMatrix,type,outputFile);
            return pathName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //二维码生成工具
    public static String creatQRcodeTobase64(String content,int width,int height,String type) {
        HashMap<EncodeHintType, Object> hints = new HashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = null;
        String base64 = "";
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, type, outputStream);
            byte[] imageContents = outputStream.toByteArray();
            base64 = ImageUtil.base64Encoding(imageContents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("base64Image {}", base64);
        return base64;
    }

}
