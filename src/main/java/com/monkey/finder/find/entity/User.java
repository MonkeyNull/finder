package com.monkey.finder.find.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="User")
public class User {


    @ApiModelProperty(value="userId用户id")
    private Long userId;


    @ApiModelProperty(value="userNick用户昵称")
    private String userNick;


    @ApiModelProperty(value="userPhone用户注册电话")
    private String userPhone;


    @ApiModelProperty(value="userScore用户积分")
    private Long userScore;


    @ApiModelProperty(value="extraInfo")
    private String extraInfo;


    @ApiModelProperty(value="userIcon用户头像")
    private String userIcon;


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserNick() {
        return userNick;
    }


    public void setUserNick(String userNick) {
        this.userNick = userNick == null ? null : userNick.trim();
    }


    public String getUserPhone() {
        return userPhone;
    }


    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }


    public Long getUserScore() {
        return userScore;
    }


    public void setUserScore(Long userScore) {
        this.userScore = userScore;
    }


    public String getExtraInfo() {
        return extraInfo;
    }


    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }


    public String getUserIcon() {
        return userIcon;
    }


    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon == null ? null : userIcon.trim();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userNick=").append(userNick);
        sb.append(", userPhone=").append(userPhone);
        sb.append(", userScore=").append(userScore);
        sb.append(", extraInfo=").append(extraInfo);
        sb.append(", userIcon=").append(userIcon);
        sb.append("]");
        return sb.toString();
    }
}