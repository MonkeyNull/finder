package com.monkey.finder.find.status;

import java.util.HashMap;
import java.util.Map;

public class ResultStatus {
    /**
     * 状态
     */
    private int state;
    /**
     * 描述
     */
    private String msg;

    private Object info;

    private boolean ok;

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public ResultStatus() {
    }

    public ResultStatus(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

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

    public Object getInfo() {
        return info;
    }

    public ResultStatus setInfo(Object info) {
        this.info = info;
        return this;
    }

    public ResultStatus put(String key, Object value) {
        if(this.info==null){
            this.info = new HashMap<String, Object>();
        }
        Map<String, Object> map = (Map<String, Object>)this.info;
        map.put(key, value);
        return this;
    }
    public boolean isOk(){
        if(this.state==ResultStateEnum.OK.getState()){
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "ResultStatus{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                ", info=" + info +
                ", ok=" + ok +
                '}';
    }
}
