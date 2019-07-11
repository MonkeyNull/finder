package com.monkey.finder.find.utils;


import com.monkey.finder.find.config.AppConfig;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EmailUtil {


    private static JavaMailSender mailSender;


    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 发送验证邮件
     * @param to
     * @param subject
     * @param content
     */
    public static void sendMail(String to, String subject, String content){
        log.debug("to:{}", to);
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(appConfig.getUsername());
        message.setFrom("拾遗社区"); // 邮件发送者
        message.setTo(to); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(content); // 内容
        mailSender.send(message);
    }




}
