package com.monkey.finder.find.bo;

import com.monkey.finder.find.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="UserInfoBo")
public class UserInfoBo {

    @ApiModelProperty(value="userNick用户昵称")
    private String userNick;

    @ApiModelProperty(value="userPhone用户注册电话")
    private String userPhone;

    /**
     * 前端附加的信息
     */
    @ApiModelProperty(value="extraInfo")
    private String extraInfo;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public User toUser(User user){
        user.setUserNick(this.getUserNick());
        user.setUserPhone(this.getUserPhone());
        user.setExtraInfo(this.getExtraInfo());
        return user;
    }
}
