package com.monkey.finder.find.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Monkey on 2018/10/24.
 */
@Component
public class AppConfig {

    /**
     * 图片资源
     */
    @Value("${finder.upload.base-image-path}")
    String uploadImagePath;

    /**
     * 邮箱用户名
     */
    @Value("${spring.mail.username}")
    String username;

    public String getUsername() {
        return username;
    }

    public String getUploadImagePath() {
        return uploadImagePath;
    }
}

