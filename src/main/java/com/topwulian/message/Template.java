package com.topwulian.message;

import com.topwulian.constants.WXConstants;

import java.util.List;

/**
 * 模板消息实体类
 * */
public class Template {
    // 消息接收方
    private String toUser;
    // 模板id
    private String templateId;
    // 模板消息详情链接
    private String url;
    // 消息顶部的颜色
    private String topColor;
    // 参数列表
    private List<TemplateParam> templateParamList;
    //url 为微信公众号token和模板ID
    public  Template(String ToUser,List<TemplateParam> paras){
        this.toUser = ToUser;
        this.templateId = WXConstants.TASK_MESSAGE_ID;//消息模板ID
        this.templateParamList = paras;
    }

    public String getToUser() {
        return toUser;
    }
    public String getTemplateId() {
        return templateId;
    }
    public String getUrl() {
        return url;
    }
    public String getTopColor() {
        return topColor;
    }
    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }
    public List<TemplateParam> getTemplateParamList() {
        return templateParamList;
    }
}
