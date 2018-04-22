package com.api.isswhu.demo.domain.other;

public enum RespCode {

    SUCCESS(200, "请求成功"),
    WARN(201, "网络异常，请稍后重试"),
    ERROR(203, "获取失败"),
    PARAM_ERROR(204,"参数错误"),
    PARAM_LOWCASE_ERROR(205,"参数大小写错误"),
    PARAM_IMAGE_DUPLICATE(206,"镜像名重复");
    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
