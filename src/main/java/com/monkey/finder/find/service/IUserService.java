package com.monkey.finder.find.service;

import com.monkey.finder.find.entity.Account;
import com.monkey.finder.find.entity.User;
import com.monkey.finder.find.status.ResultStateEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IUserService {

    ResultStateEnum checkAccount(String userName, String passWord);

    User selectByName(String userName);

    Account selectByEmail(String userEmail);

    boolean accountIsExist(String userName);

    boolean accountIsExistByNick(String userNick);

    boolean isCurrentUser(String userNick);

    Map encryptPass(String passWord);

    void insert(User user);

    void insertAccount(Account account);

    User selectByUid(Long uid);

    int update(User user);

    String saveIcon(MultipartFile file);

    Account selectAccountByUid(Long userId);

    int updateAccount(Account account);
}
