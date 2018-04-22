package com.api.isswhu.demo.domain.other;

public class ResponData {
    /**
     * 返回的状态码
     * 默认 200-正常 201 ....
     */
    private int code;
    private String msg;
    private Object data;
    
    public ResponData() {
    }
    
    public ResponData(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public ResponData(RespCode respCode,Object data) {
        this(respCode);
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}
