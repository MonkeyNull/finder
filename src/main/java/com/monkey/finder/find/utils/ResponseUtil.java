package com.monkey.finder.find.utils;


import com.alibaba.fastjson.JSON;
import com.monkey.finder.find.config.DecodeConfig;
import com.monkey.finder.find.status.ResultStateEnum;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author: 王志伟
 * @date: 2018/3/8 12:45
 * @desc:
 */
public class ResponseUtil {
    /** json格式 */
    public static final String OUTPUT_JSON_CONTENT_TYPE = "application/json";
    /** html格式 */
    public static final String OUTPUT_HTML_CONTENT_TYPE = "text/html";

    /**
     * 向前端输出Json数据
     *
     * @param response
     * @param str
     */
    public static void rspHtmlString(HttpServletResponse response, String str) {
        rspString(response, str, OUTPUT_HTML_CONTENT_TYPE , DecodeConfig.DEFAULT_FILE_ENCODING);
    }
    /**
     * 向前端输出Json数据
     *
     * @param response
     * @param obj
     */
    public static void rspJsonString(HttpServletResponse response, Object obj) {
        String str = JSON.toJSONString(ResultStateEnum.NO_LOGIN_FAIL.toResultStatus());
        rspString(response, str, OUTPUT_JSON_CONTENT_TYPE , DecodeConfig.DEFAULT_FILE_ENCODING);
    }
    /**
     * 向前端输出Json数据
     *
     * @param response
     * @param str
     */
    public static void rspJsonString(HttpServletResponse response, String str) {
        rspString(response, str, OUTPUT_JSON_CONTENT_TYPE , DecodeConfig.DEFAULT_FILE_ENCODING);
    }
    /**
     * 向前端输出内容
     *
     * @param response
     * @param str
     */
    public static void rspString(HttpServletResponse response, String str,
                                 String contentType, String characterEncoding) {
        response.setContentType(contentType);
        response.setCharacterEncoding(characterEncoding);
        java.io.PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    /**
     * 向前端输出内容
     *
     * @param response
     * @param bytes
     */
    public static void rspBytes(HttpServletResponse response, byte[] bytes,
                                String contentType) {
        response.setContentType(contentType);
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
