package com.topwulian.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.topwulian.constants.WXConstants;
import com.topwulian.message.TemplateParam;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 发送微信消息模板
 */
public class SendWxTemplateMessageUtil {

    private SendWxTemplateMessageUtil(){}

    public static JSONObject sendMessage(Map<String,TemplateParam> param, String openid, String templateId, String access_token){

        JSONObject data =  new JSONObject();
        Set<Map.Entry<String, TemplateParam>> entries = param.entrySet();
        Iterator<Map.Entry<String, TemplateParam>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, TemplateParam> next = iterator.next();
            data.put(next.getKey(), JSON.toJSON(next.getValue()));
        }
        JSONObject requestParam = new JSONObject();
        requestParam.put("data",data);
        requestParam.put("template_id",templateId);
        requestParam.put("touser",openid);
        requestParam.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe4eed8c67a42c5f0&redirect_uri=http://x.topwulian.com/mobielogin.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
        //发送请求
        String request_message_url = MessageFormat.format(WXConstants.REQUEST_MESSAGE_URL, access_token);
        String resultStr = HttpUtil.post(request_message_url,requestParam.toString());
        JSONObject jsonResult = JSONObject.parseObject(resultStr);
        return jsonResult;
    }

}
