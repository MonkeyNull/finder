package com.monkey.finder.find.bo;

import com.monkey.finder.find.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="com.monkey.finder.find.bo.UserBo")
public class UserBo {

    @ApiModelProperty(value="userId用户id")
    private Long userId;

    @ApiModelProperty(value="userNick用户昵称")
    private String userNick;

    @ApiModelProperty(value="userPhone用户注册电话")
    private String userPhone;

    @ApiModelProperty(value="userScore用户积分")
    private Long userScore;

    /**
     * 前端附加的信息
     */
    @ApiModelProperty(value="extraInfo")
    private String extraInfo;

    @ApiModelProperty(value="userIcon用户头像")
    private String userIcon;

    @ApiModelProperty(value="token")
    private String token;



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
        this.userNick = userNick;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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
        this.extraInfo = extraInfo;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 将user转换为userBo
     * @param user
     */
    public void userToBo(User user){
        this.setExtraInfo(user.getExtraInfo());
        this.setUserIcon(user.getUserIcon());
        this.setUserId(user.getUserId());
        this.setUserNick(user.getUserNick());
        this.setUserPhone(user.getUserPhone());
        this.setUserScore(user.getUserScore());
    }

}
