package com.left.common;

import org.springframework.ui.Model;

/**
 * 返回消息，由spring组装成json
 * {"result":0,"msg":"XXXX","data":{}}
 * Created by left on 16/1/4.
 */
public class Result {
    /**
     * 结果编号
     */
    public String result = "0";

    /**
     * 返回消息
     */
    public String msg;

    /**
     * 返回数据
     */
    public Model data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Model getData() {
        return data;
    }

    public void setData(Model data) {
        this.data = data;
    }

}
