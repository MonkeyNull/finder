package com.monkey.finder.find.interceptor;


import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.status.ResultStateEnum;
import com.monkey.finder.find.utils.ResponseUtil;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: 王志伟
 * @date: 2018/3/5 16:51
 * @desc:
 */
@Slf4j
@Component
public class FirstInterceptor implements HandlerInterceptor {
    private IRedisService redisService;

    public FirstInterceptor(IRedisService redisService) {
        this.redisService=redisService;
    }

    private static final List<String> NO_NEED_LOGIN_START_URI = Arrays.asList(
            "/swagger-resources",
            "/v2/api-docs",
            "/swagger-ui.html",
            "/webjars/",
            "/login",
            "/user/login",
            "/user/regis",
            "/user/checkEmail",
            "/user/setUserInfo",
            "/user/Admini",
            "/img/",
            "/findcore/getItemInfo"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age","3600");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            response.setContentLength(0);
            return false;
        }
        String requestUri = request.getRequestURI();
        log.debug("requestUri:{},method:{}", requestUri, request.getMethod());
        if(StringUtils.isEmpty(requestUri)){
            return true;
        }
        for (String str: NO_NEED_LOGIN_START_URI) {
            if(requestUri.startsWith(str)){
                return true;
            }
        }
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            log.error("没有token");
            ResponseUtil.rspJsonString(response, ResultStateEnum.NO_LOGIN_FAIL.toResultStatus());
            return false;
        }

        Long uid = redisService.getUid(FindRedisConstants.ONLINE_USER_TOKEN+token);
        if(uid==null){
            ResponseUtil.rspJsonString(response, ResultStateEnum.PROGRAM_EXCEPTION.toResultStatus());
            return false;
        }
        redisService.set(FindRedisConstants.ONLINE_USER_UID+uid,token, 10, TimeUnit.DAYS);
        redisService.set(FindRedisConstants.ONLINE_USER_TOKEN+token,uid+"", 10, TimeUnit.DAYS);
        ThreadLocalUtil.setUid(uid);

        log.debug("uid:{},token:{}", uid, token);
        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalUtil.clear();
    }

}
