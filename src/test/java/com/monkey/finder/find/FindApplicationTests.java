package com.monkey.finder.find;

import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.utils.EmailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindApplicationTests {

    @Resource
    IRedisService redisService;

    @Test
    public void contextLoads() {
        String path = "D://finder//img/38/img/f9f9e6bfbce04414a33604764839ae20.jpeg";
        path = path.replace("D://finder/", "");
        path = path.replace("/home/finder/finderProject", "");
        System.out.println(path);
//        redisService.set(FindRedisConstants.ONLINE_USER_TOKEN+"a1",1L+"", 1000, TimeUnit.DAYS);
    }



}

