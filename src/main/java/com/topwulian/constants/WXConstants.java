package com.topwulian.constants;

public interface WXConstants {

    //获取access_token Url
    String REQUEST_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    //发送模板请求url
    String REQUEST_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";

    //获取网页授权的access_token Url
    String REQUEST_ACCESS_TOKEN_URL_WEB = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    //任务通知模板id
    String TASK_MESSAGE_ID = "HZBE4uB87zy4YkgUHVJwSDjbFLQ6S7A2qfCh3RuMwzg";
}
