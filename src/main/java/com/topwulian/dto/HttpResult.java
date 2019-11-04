package com.topwulian.dto;


/**
 * @Author: szz
 * @Date: 2019/1/4 上午10:19
 * @Version 1.0
 * {"data":{"accessToken":"at.4up4jm407pk5xr4gb3lhwwbt9m82c97p-173203fz0m-0dlrqgm-wh15gpfl6","expireTime":1547177861638},"code":"200","msg":"操作成功!"}
 */
public class HttpResult {
    private int code;
    private String body;
    private Data data;

    public HttpResult(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}


