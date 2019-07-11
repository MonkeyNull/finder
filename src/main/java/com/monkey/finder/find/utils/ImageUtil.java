package com.monkey.finder.find.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * Created by B038 on 2018/7/20 0020.
 */
@Slf4j
public class ImageUtil {
    /**
     * 从图片文件中读取内容。
     *
     * @param path 图片文件的路径。
     * @return 二进制图片内容的byte数组。
     */
    public static byte[] readFile(Path path) {
        byte[] imageContents = null;

        try {
            imageContents = Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("读取文件出错了:" + path.toString(), e);
        }
        return imageContents;
    }

    /**
     * 编码图片文件，编码内容输出为{@code String}格式。
     *
     * @param imageContents 二进制图片内容的byte数组。
     * @return {@code String}格式的编码内容。
     */
    public static String base64Encoding(byte[] imageContents) {
        if (imageContents != null) {
            try {
                return new String(Base64.encodeBase64(imageContents), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 解码图片文件。
     *
     * @param imageContents 待解码的图片文件的字符串格式。
     * @return 解码后图片文件的二进制内容。
     */
    public static byte[] base64Decoding(String imageContents) {
        if (imageContents != null) {
            return Base64.decodeBase64(imageContents);
        } else {
            return null;
        }
    }

    /**
     * 将解码后的二进制内容写入文件中。
     *
     * @param path          写入的路径。
     * @param imageContents 解码后的二进制内容。
     */
    public static void writeFile(Path path, byte[] imageContents) {
        if (imageContents != null) {
            try {
                Files.write(path, imageContents, StandardOpenOption.CREATE);
            } catch (IOException e) {
                log.error("写入文件出错了...~zZ", e);
            }
        }
    }

    /**
     * 用于用户上传图片数据
     *
     * @param file       图片文件
     * @param targetPath 存放路径
     * @return 存放地址
     * @throws IOException
     */
    public static String saveImage(MultipartFile file, String targetPath) throws IOException {
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if (dotpos < 0) {
            return null;
        }
//      将后缀名拿出来，转换成小写
        String fileExt = file.getOriginalFilename().substring(dotpos + 1).toLowerCase();
        if (!PictureProcessingUtil.isFileAllowed(fileExt)) {
            log.error("上传图片格式异常");
            return null;
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + fileExt;
        //这个方法可以把file里的数据拷贝到数据中。
        String targetPath_DB = targetPath + filename;
        File file1 = new File(targetPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        compresssionImg(image,new File(targetPath_DB).toPath().toString());
//        Files.copy(file.getInputStream(), new File(targetPath_DB).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return (targetPath_DB);
    }

    /**
     * 用于用户上传图片数据
     *
     * @param file       图片文件
     * @param targetPath 存放路径
     * @return 存放地址
     * @throws IOException
     */
    public static String saveImage(byte[] file, String targetPath) throws IOException {
        if (file == null) {
            return null;
        }
        String fileExt = "png";
        String filename = "fenxiang" + "." + fileExt;
        //这个方法可以把file里的数据拷贝到数据中。
        String targetPath_DB = targetPath + filename;
        File file1 = new File(targetPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file));
        compresssionImg(image,new File(targetPath_DB).toPath().toString());
//        Files.write(new File(targetPath_DB).toPath(),file);
        return (ThreadLocalUtil.getUid() + "/" + filename);
    }

    /**
     * 用于用户上传图片数据
     *
     * @param bufferedImage 图片文件
     * @param targetPath    存放路径
     * @param fileExt       文件后缀
     * @return 存放地址
     * @throws IOException
     */
    public static String saveImage(BufferedImage bufferedImage, String fileExt, String targetPath) throws IOException {
        if (bufferedImage == null) {
            log.error("BufferedImage 数据为空");
            return null;
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + fileExt;
        String targetPath_DB = targetPath + filename;
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        ImageIO.write(bufferedImage, fileExt, new File(targetPath_DB));
        return (ThreadLocalUtil.getUid() + "card/resource/" + filename);
    }

    /**
     *对图片进行压缩处理
     * @param image 原图片
     * @param targetPath 图片存放地址
     */
    public static void compresssionImg(BufferedImage image,String targetPath){
        if(image == null){
            return;
        }
        try {
            int hei = (int) ((1080.0/image.getWidth())*image.getHeight());
            Thumbnails.of(image)
                    .forceSize(1080,hei)
                    .outputQuality(0.25f)
                    .toFile(targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器中的图片压缩
     * @param file
     */
    public static void func(File file){
        File[] fs = file.listFiles();
        for (File f : fs) {
            if(f.isDirectory()){
                func(f);
            }else {
                if (isImg(f.getName())) {
                    String targetname = f.getName();
                    try {
                        BufferedImage image = ImageIO.read(new FileInputStream(f.getPath()));
                        log.debug("path{}",f.getParent());
                        ImageUtil.compresssionImg(image,f.getParent()+"/"+targetname);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 判断是否为图片
     * @param name
     * @return
     */
    private static boolean isImg(String name){
        String reg="(?i).+?\\.(jpg|gif|bmp|png|jpeg)";
        if(name.matches(reg)){
            return true;
        }
        return false;
    }


}

