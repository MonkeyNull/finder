package com.monkey.finder.find.status;


/**
 * 下行信息
 */
public enum FinderResultStateEnum implements IResultStateEnum {
    OK(0,"成功"),
    PERMISSION_DENIED(-101,"无操作权限"),
    NOT_FOUND_ERROR(-102,"未找到对应信息"),
    FORMAT_ERROR(-103,"数据格式错误"),
    REQUEST_TIMEOUT_ERROR(-201,"请求超时"),
    REQUEST_EXCEPTION_ERROR(-202,"请求异常"),
    NO_HANDLER_FOUND_ERROR(-203,"未找到对应的处理方法"),
    METHOD_NOT_SUPPORTED_ERROR(-204,"请求方法不支持"),
    MISSING_PARAMETER_ERROR(-205,"缺少参数"),
    PARAMETER_TYPE_MISMATCH_ERROR(-206,"参数类型不对或无法转换"),
    DATA_TOO_LONG_ERROR(-111,"数据太长"),
    UPLODE_IMG_ERROR(-116,"上传图片失败"),
    PARAMETER_NOT_VALID_ERROR(-207,"参数校验异常"),
    UNFOLLOW_ERROR(-211,"未关注"),
    LOGIN_FAIL_ERROR(-301,"登录失败"),
    UPLODEICONIMAGE_FAIL_ERROR(-302,"上传头像失败"),
    EXIST_ACCOUT(-303,"账号已存在"),
    REGIS_FAIL_ERROR(-304,"注册失败"),
    REPEAT_NICK_ERROR(-305,"昵称重复"),
    EMAIL_FORMAT_ERROR(-306,"邮箱格式错误"),
    ACCOUNT_NOTCHECK_ERROR(-307,"账号还未进行邮箱认证"),
    MISSING_USERINFO_ERROR(-308,"用户信息未填写")
    ;
    private int state;
    private String msg;

    private FinderResultStateEnum(int state, String msg){
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
