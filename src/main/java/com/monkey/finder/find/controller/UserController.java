package com.monkey.finder.find.controller;


import com.monkey.finder.find.bo.UserBo;
import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.entity.Account;
import com.monkey.finder.find.entity.User;
import com.monkey.finder.find.service.IMailService;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.service.IUserService;
import com.monkey.finder.find.status.FinderResultStateEnum;
import com.monkey.finder.find.status.ResultStateEnum;
import com.monkey.finder.find.status.ResultStatus;
import com.monkey.finder.find.utils.EmailUtil;
import com.monkey.finder.find.utils.FindUtils;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "用户controller")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @Resource
    IRedisService redisService;

    @Resource
    IMailService mailService;

    @ApiOperation(value = "登陆", notes = "用户登陆")
    @ApiImplicitParams({@ApiImplicitParam(name = "userEmail",value = "用户邮箱",dataType = "String",required = true,paramType = "form")
                        ,@ApiImplicitParam(name = "passWord", value = "用户密码",dataType = "String",required = true,paramType = "form")})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultStatus login(@RequestParam(required = true) String userEmail,
                              @RequestParam(required = true) String passWord){
        //检测账号密码是否有效
        ResultStateEnum resultAccountState = userService.checkAccount(userEmail,passWord);
        //todo 对checkAccount返回来的参数进行分类处理
        if (resultAccountState.getState() == 0){
            Account account = userService.selectByEmail(userEmail);
            if (account.getState() == 0){
                return FinderResultStateEnum.ACCOUNT_NOTCHECK_ERROR.toResultStatus();
            }
            String token = FindUtils.getToken();
            String oldToken = redisService.getStr(FindRedisConstants.ONLINE_USER_UID+account.getUserId());
            if (!StringUtils.isEmpty(oldToken) && !oldToken.equals(token)) {
                redisService.delete(FindRedisConstants.ONLINE_USER_TOKEN+token);
            }
            //将user和token绑定在一起
            redisService.set(FindRedisConstants.ONLINE_USER_UID+account.getUserId(),token, 7, TimeUnit.DAYS);
            redisService.set(FindRedisConstants.ONLINE_USER_TOKEN+token,account.getUserId()+"", 1, TimeUnit.DAYS);
            ThreadLocalUtil.setUid(account.getUserId());
            //将用户信息返回
            User user = userService.selectByUid(account.getUserId());
            if (user == null){
                return FinderResultStateEnum.MISSING_USERINFO_ERROR.toResultStatus().setInfo(account.getUserId());
            }
            UserBo userBo = new UserBo();
            userBo.userToBo(user);
            userBo.setToken(token);
            return FinderResultStateEnum.OK.toResultStatus().setInfo(userBo);
        }else {
            return resultAccountState.toResultStatus();
        }
    }

    @ApiOperation(value = "注册", notes = "用户注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "userEmail",value = "用户邮箱",dataType = "String",required = true,paramType = "form")
                        ,@ApiImplicitParam(name = "passWord", value = "用户密码",dataType = "String",required = true,paramType = "form")})
    @RequestMapping(value = "/regis", method = RequestMethod.POST)
    public ResultStatus regis(@RequestParam(required = true) String userEmail,
                              @RequestParam(required = true) String passWord){
        //常规字符检测 检测是否为空
        if (StringUtils.isBlank(userEmail) && StringUtils.isBlank(passWord)){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        //判断用户输入的邮箱是否是符合规范
        if (!EmailUtil.checkEmail(userEmail)){
            return FinderResultStateEnum.EMAIL_FORMAT_ERROR.toResultStatus();
        }
        //判断该用户是否注册
        if (userService.accountIsExist(userEmail)){
            return FinderResultStateEnum.EXIST_ACCOUT.toResultStatus();
        }
        //开始注册逻辑实现
        Account account = new Account();
        account.setUserEmail(userEmail);
        //进行密码处理
        Map<String,String> passMap = userService.encryptPass(passWord);
        if (passMap == null){
            return FinderResultStateEnum.REGIS_FAIL_ERROR.toResultStatus();
        }
        account.setSalt(passMap.get("salt"));
        account.setUserPassword(passMap.get("passToDb"));
        //将state设置为 0 表示还未进行邮箱验证
        account.setState(0);
        //用户的注册时间
        account.setRegisDate(new Date());
        userService.insertAccount(account);
        //邮箱验证
//        String checkText = "欢迎您来到拾遗社区，点击这个链接完成验证"+"http://62.234.6.224:16001/user/checkEmail?userId="+account.getUserId();
        String checkText = "欢迎您来到拾遗社区，点击这个链接完成验证"+"http://152.136.147.110/activate";
        //todo 19/5/5 因为浏览器会发生断开连接问题，因此异步发送邮箱验证码
        log.debug(Thread.currentThread().getName());
        mailService.sendSimpleMail(userEmail, "验证您的拾遗账号", checkText);
        return FinderResultStateEnum.OK.toResultStatus().setInfo(account.getUserId());
    }

    //用户基础信息填写界面
    @ApiOperation(value = "用户信息填写", notes = "用户基础信息填写")
    @ApiImplicitParam(name = "user", value = "用户基础信息", dataType = "User", required = true)
    @RequestMapping(value = "/setUserInfo", method = RequestMethod.POST)
    public ResultStatus setUserInfo(@RequestBody User user){
        if (user == null || user.getUserNick() == null){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        //检测昵称是否重复
        if (userService.accountIsExistByNick(user.getUserNick())){
            if (!userService.isCurrentUser(user.getUserNick())) {
                //昵称重复并且与当前用户的ID不一致 则不能保证用户昵称的唯一性
                return FinderResultStateEnum.REPEAT_NICK_ERROR.toResultStatus();
            }
        }
//        user.setUserId(ThreadLocalUtil.getUid());
        log.debug("user :{}",user);
        //将更新后的userupdate到数据库中
        userService.update(user);
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //用户上传头像
    @ApiOperation(value = "上传用户头像", notes = "用户头像上传")
    @RequestMapping(value = "/uplodeIcon", method = RequestMethod.POST)
    public ResultStatus uplodeIcon(@RequestParam("file") MultipartFile file){
        String path = userService.saveIcon(file);
        if (StringUtils.isEmpty(path)){
            log.debug("上传图片失败");
            return FinderResultStateEnum.UPLODEICONIMAGE_FAIL_ERROR.toResultStatus();
        }
        log.debug("icon path:{}",path);
        User user = userService.selectByUid(ThreadLocalUtil.getUid());
        user.setUserIcon(path);
        userService.update(user);
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //用户邮箱验证
    @ApiOperation(value = "用户邮箱验证", notes = "用户邮箱验证")
    @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "Long", required = true,paramType = "query")
    @RequestMapping(value = "/checkEmail", method = RequestMethod.GET)
    public ResultStatus checkEmail(@RequestParam Long userId){
        if (userId == null){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        Account account = userService.selectAccountByUid(userId);
        //将账号设置为 已验证状态
        account.setState(1);
        userService.updateAccount(account);
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //重置密码（管理员使用）
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "userEmail",value = "用户邮箱",dataType = "String",required = true,paramType = "form")
            ,@ApiImplicitParam(name = "passWord", value = "用户密码",dataType = "String",required = true,paramType = "form")})
    @RequestMapping(value = "/resetPassWord", method = RequestMethod.POST)
    public ResultStatus resetPassWord(@RequestParam String userEmail,
                                      @RequestParam String passWord){
        if (StringUtils.isBlank(userEmail) || StringUtils.isBlank(passWord)){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        Account account = userService.selectByEmail(userEmail);
        log.debug("account :{}",account);
        if (account == null){
            //该账号没有进行注册
            return FinderResultStateEnum.NOT_FOUND_ERROR.toResultStatus();
        }
        //进行密码处理
        Map<String,String> passMap = userService.encryptPass(passWord);
        if (passMap == null){
            return FinderResultStateEnum.REGIS_FAIL_ERROR.toResultStatus();
        }
        account.setSalt(passMap.get("salt"));
        account.setUserPassword(passMap.get("passToDb"));
        userService.updateAccount(account);
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //设置一个token和uid关联
    @ApiOperation(value = "设置管理员账户", notes = "设置管理员账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "uid",value = "",dataType = "Long",required = true,paramType = "form")
            ,@ApiImplicitParam(name = "token", value = "",dataType = "String",required = true,paramType = "form")})
    @RequestMapping(value = "/Admini", method = RequestMethod.POST)
    public ResultStatus Admini(@RequestParam Long uid,
                               @RequestParam String token){
        redisService.set(FindRedisConstants.ONLINE_USER_TOKEN+token,uid+"", 1000, TimeUnit.DAYS);
        log.debug("uid :{} token :{}", uid,token);
        return FinderResultStateEnum.OK.toResultStatus();
    }

}
