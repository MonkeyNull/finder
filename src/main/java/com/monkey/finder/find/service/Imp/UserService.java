package com.monkey.finder.find.service.Imp;


import com.monkey.finder.find.config.AppConfig;
import com.monkey.finder.find.dao.AccountMapper;
import com.monkey.finder.find.dao.UserMapper;
import com.monkey.finder.find.entity.Account;
import com.monkey.finder.find.entity.User;
import com.monkey.finder.find.service.IUserService;
import com.monkey.finder.find.status.ResultStateEnum;
import com.monkey.finder.find.utils.FindUtils;
import com.monkey.finder.find.utils.ImageUtil;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class UserService implements IUserService {



    @Resource
    UserMapper userMapper;

    @Resource
    AppConfig appConfig;

    @Resource
    AccountMapper accountMapper;

    /**
     * 对用户名和密码进行检测
     * @param userEmail
     * @param passWord
     * @return -9 该账号不存在 -10 密码错误 -11 账号密码为空
     */
    @Override
    public ResultStateEnum checkAccount(String userEmail, String passWord) {

        //判断数据是否为空
        if (StringUtils.isBlank(userEmail) || StringUtils.isBlank(passWord)){
            return ResultStateEnum.NULL_ACCOUNT;
        }
        if (!accountIsExist(userEmail)){
            return ResultStateEnum.NOTFOUND_ACCOUNT;
        }
        if (!checkPassWord(userEmail,passWord)){
            return ResultStateEnum.ERR_PASSWORD_ACCOUNT;
        }
        return ResultStateEnum.OK;
    }

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     */
    @Override
    public User selectByName(String userName) {
        if (userName == null){
            log.debug("userName is null");
            return null;
        }
        User user = userMapper.selctByName(userName);
        if (user == null){
            log.debug("user is null");
            return null;
        }
        return user;
    }

    /**
     * 根据邮箱查找用户
     * @param userEmail
     * @return
     */
    @Override
    public Account selectByEmail(String userEmail) {
        if (userEmail == null){
            log.debug("userEmail is null");
            return null;
        }
        return accountMapper.selctByEmail(userEmail);
    }



    /**
     * 判断用户密码是否正确
     * @param userEmail
     * @param passWord
     * @return true 正确 false 错误
     */
    private boolean checkPassWord(String userEmail, String passWord) {
        Account account = accountMapper.selctByEmail(userEmail);
        if (account == null){
            return false;
        }
        //todo 对密码进行验证
        log.debug("user{}",account);
        String checkStr = FindUtils.MD5(passWord + account.getSalt());
        log.debug("checkStr",checkStr);
        if (checkStr.equals(account.getUserPassword())){
            return true;
        }
        return false;
    }

    /**
     * 账户是否存在 邮箱是否以存在。
     * @param userEmail
     * @return true 存在 false 不存在
     */
    @Override
    public boolean accountIsExist(String userEmail) {
        Account account = accountMapper.selctByEmail(userEmail);
        if (account != null){
            log.debug("user{}",account);
            return true;
        }
        return false;
    }

    /**
     * 根据昵称判断账户是否存在
     * @param userNick
     * @return true 存在 false 不存在
     */
    @Override
    public boolean accountIsExistByNick(String userNick) {
        User user = userMapper.selctByNick(userNick);
        if (user != null){
            log.debug("user{}",user);
            return true;
        }
        return false;
    }

    /**
     * 根据昵称判断用户是否是当前用户
     * @param userNick
     * @return true 存在 false 不存在
     */
    @Override
    public boolean isCurrentUser(String userNick) {
        User user = userMapper.selctByNick(userNick);
        //当用户存在并且ID与当前用户ID一致的时候 则视为同一个用户在更新自己的信息
        if (user != null && user.getUserId().equals(ThreadLocalUtil.getUid())){
            log.debug("user{}",user);
            return true;
        }
        return false;
    }

    /**
     * 对密码进行加密
     * @param passWord
     * @return Map 包含salt 和 加密后的密码
     */
    @Override
    public Map encryptPass(String passWord) {
        if (passWord == null){
            log.debug("passWord is null");
            return null;
        }
        Map<String,String> map = new HashMap<>();
        String salt = UUID.randomUUID().toString().replaceAll("-","");
        String passToDb = FindUtils.MD5(passWord + salt);
        log.debug("paddToDb:{}",passToDb);
        if (passToDb == null){
            log.debug("MD5 生成密码失败！");
            return null;
        }
        map.put("salt",salt);
        map.put("passToDb",passToDb);
        return map;
    }

    /**
     * 像数据库中插入一条数据
     */
    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    /**
     * 像数据库中插入一条数据
     */
    @Override
    public void insertAccount(Account account) {
        accountMapper.insert(account);
    }

    /**
     * 根据用户id查找用户
     * @param uid
     * @return
     */
    @Override
    public User selectByUid(Long uid) {
        if (uid == null){
            log.debug("uid is null");
            return null;
        }
        User user = userMapper.selectByPrimaryKey(uid);
        if (user == null){
            log.debug("user is null");
            return null;
        }
        return user;
    }

    /**
     * 更新用户信息 如果数据库中没有该信息则添加一条记录
     * @param user
     * @return
     */
    @Override
    public int update(User user){
        if (user == null){
           log.debug("user is null");
           return 0;
        }
        if (selectByUid(user.getUserId()) == null){
            return userMapper.insert(user);
        }
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int updateAccount(Account account) {
        if (account == null){
            log.debug("user is null");
        }
        return accountMapper.updateByPrimaryKey(account);
    }

    @Override
    public String saveIcon(MultipartFile file){
        //图片存储的地址
        String img_target_path = appConfig.getUploadImagePath()+ "/" + ThreadLocalUtil.getUid() + "/icons/";
//        String img_target_path = "/img/" + ThreadLocalUtil.getUid() + "/icons/";
        String path = null;
        try {
            path = ImageUtil.saveImage(file,img_target_path);
            log.debug("saveImagepath{}", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回的地址需要经过处理 以/img开头
        path = path.replace("D://finder/", "");
        path = path.replace("/home/finder/finderProject", "");
        return path;
    }

    @Override
    public Account selectAccountByUid(Long uid) {
        if (uid == null){
            log.debug("uid is null");
            return null;
        }
        return accountMapper.selectByPrimaryKey(uid);
    }


//    public

}
