package com.monkey.finder.find.controller;


import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.entity.Demo;
import com.monkey.finder.find.entity.User;
import com.monkey.finder.find.service.IDemoService;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.service.IUserService;
import com.monkey.finder.find.service.Imp.RedisService;
import com.monkey.finder.find.status.FinderResultStateEnum;
import com.monkey.finder.find.status.ResultStatus;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(value = "测试controller")
@RestController
@RequestMapping("/Demo")
public class demo {

    @Resource
    IDemoService demoService;

    @Resource
    IUserService userService;



    @ApiOperation(value = "查看当前用户所有信息",notes = "demo")
    @RequestMapping(value = "/getDemoinfo", method = RequestMethod.GET)
    public ResultStatus getDemoinfo(){
        User user = userService.selectByUid(ThreadLocalUtil.getUid());
        return FinderResultStateEnum.OK.toResultStatus().setInfo(user);
    }




}
