package com.monkey.finder.find.status;

public enum ResultStateEnum implements IResultStateEnum{
    OK(0,"成功"),
    PROGRAM_EXCEPTION(-1,"信号不太好，请稍后重试.."),
    CONNECT_REFUSE(-2,"连接拒绝"),
    SMS_INVOKE_FAIL(-3,"短信模块调用失败"),
    MAIL_INVOKE_FAIL(-4,"邮件模块调用失败"),
    NO_PERMISSION_FAIL(-5,"没有权限"),
    NO_LOGIN_FAIL(-6,"请登录"),
    EXEC_FAIL(-7,"运行失败"),
    USER_INFO_ERR(-8,"用户信息异常"),
    NOTFOUND_ACCOUNT(-9,"该账号不存在"),
    ERR_PASSWORD_ACCOUNT(-10,"密码错误"),
    NULL_ACCOUNT(-11,"账号密码不能为空"),
    ;
    private int state;
    private String msg;

    private ResultStateEnum(int state, String msg){
        this.state = state;
        this.msg = msg;
    }

    @Override
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultStatus toResultStatus(){
        return new ResultStatus(this.getState(), this.getMsg());
    }

    public ResultStatus toResultStatus(String msg){
        return new ResultStatus(this.getState(), msg);
    }
}
