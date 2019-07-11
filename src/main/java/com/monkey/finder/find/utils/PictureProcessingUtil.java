package com.monkey.finder.find.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by HXC249 on 2018/7/23.
 */
@Slf4j
public class PictureProcessingUtil {
//    图像处理工具，将头像变为圆头
    public static BufferedImage roundImage(BufferedImage image, int targetSize, int cornerRadius) {
        if(image == null ){
            return null;
        }
        BufferedImage outputImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, targetSize, targetSize, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        //确定要截取的坐标
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }

//    根据传入的path来传入图片


    public static final BufferedImage getImg(String path){
        BufferedImage bufferedImage = null ;
        if(StringUtils.isEmpty(path)){
            return bufferedImage;
        }
        if(StringUtils.containsIgnoreCase(path,"http")){
            try {
                URL url = new URL(path);
                bufferedImage = ImageIO.read(url);
            } catch (IOException e) {
                log.error("path:"+path, e);
            }
        }else{
            try {
                bufferedImage = ImageIO.read(new File(path));
            } catch (IOException e) {
                log.error("path:"+path, e);
            }
        }
        return bufferedImage;
    }

    public static String[] IMAGE_FILE_EXT = new String[] {"png","bmp","jpg","jpeg"};
    public static boolean isFileAllowed(String filext){
        for(String ext : IMAGE_FILE_EXT){
            if(ext.equals(filext))
                return true;
        }
        return false;
    }

    public static BufferedImage compressPicture(BufferedImage image,int w,int h) {
        if(image == null){
            return null;
        }
        BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics g = tag.getGraphics();

        g.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH),0,0,null);

        return tag;

    }

    private static final String[] strSplit(String content) {
        /**
         * @description 可以对心语进行长度调整，调整每行的长度
         * @method  strSplit
         * @param content
         * @return java.lang.String[]
         * @date: 2018/8/2 15:56
         * @author:Monkey
         */
        if (content == null ){
            return null;
        }
        int n = (content.length()/15);
        StringBuffer buffer = new StringBuffer(content);
        for (int i = 1 ; i <= n ; i++){
            buffer.insert(15*i+(i-1),"|");
        }
        String[] strs = StringUtils.split(buffer.toString(),"|");

        return strs;
    }
    public static BufferedImage changeFileToBufImg(MultipartFile file) {
        BufferedImage srcImage = null;
        try {
            FileInputStream in = (FileInputStream) file.getInputStream();
            srcImage = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
        }
        return srcImage;
    }
    public static String saveBytes(byte[] bytes,String targetPath){
        FileChannel fc = null;
        try {
            File file = new File(targetPath);
            if (!file.exists()){
                file.createNewFile();
            }
            fc = new FileOutputStream(file).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            fc.write(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "saveBytes is OK";
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
            MatrixToImageWriter.writeToFile(bitMatrix,type,outputFile);
            return pathName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
