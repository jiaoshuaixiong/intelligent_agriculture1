package com.topwulian.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.topwulian.constants.WXConstants;
import com.topwulian.dao.TProducterDao;
import com.topwulian.dao.WxPlatformDao;
import com.topwulian.exception.WxAccessTokenException;
import com.topwulian.model.RespEntiry;
import com.topwulian.model.TProducter;
import com.topwulian.model.WxPlatform;
import com.topwulian.service.WxService;
import com.topwulian.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WxServiceImpl implements WxService {



    @Autowired
    private WxPlatformDao wxPlatformDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TProducterDao tProducterDao;

    @Override
    public RespEntiry login(String code, String phone, String pass) {
        checkRequestParams(phone,pass);
        TProducter producter = tProducterDao.getByPhone(phone);
        if(producter == null){
            return RespEntiry.fail("手机号用户不存在!");
        }
        if(!pass.equals(producter.getPassword())){
            return RespEntiry.fail("手机号或密码错误!");
        }

        if(!StringUtils.isEmpty(producter.getOpenid())){
            producter.setPassword("");
            return RespEntiry.success("登陆成功",producter);
        }
        //获取openid  绑定用户openid
        if(StringUtils.isEmpty(code)){
            return RespEntiry.fail("绑定用户获取code失败!");
        }

        int farmId = producter.getFarmId();
        WxPlatform platform = wxPlatformDao.getByFarmId(new Long(farmId));
        String access_token_web = MessageFormat.format(WXConstants.REQUEST_ACCESS_TOKEN_URL_WEB,platform.getAPPID(),platform.getAPPSECRET(),code);
        JSONObject result = JSONObject.parseObject(HttpUtil.get(access_token_web));

        String openid = result.getString("openid");
        if(StringUtils.isEmpty(openid)){
            throw new WxAccessTokenException("绑定用户失败,获取openid错误!");
        }
        producter.setOpenid(openid);
        tProducterDao.updateOpenid(producter);
        producter.setPassword("");

        return RespEntiry.success("登陆成功",producter);
    }

    private void checkRequestParams(String phone, String pass) {
        if(StringUtils.isEmpty(phone)){
            throw new IllegalArgumentException("请求参数错误!");
        }
        if(StringUtils.isEmpty(pass)){
            throw new IllegalArgumentException("请求参数错误!");
        }
    }

    @Override
    public String getToekn() {
        //int farmId = UserUtil.getUserFarmId();
        int farmId = 35;//暂时默认
        String access_token = redisTemplate.opsForValue().get("farmId:" + farmId + "--access_token");
        //没有找到缓存信息
        if(StringUtils.isEmpty(access_token)){
            WxPlatform platform = wxPlatformDao.getByFarmId(new Long(farmId));
            String access_token_url = MessageFormat.format(WXConstants.REQUEST_ACCESS_TOKEN_URL, platform.getAPPID(), platform.getAPPSECRET());
            JSONObject result = JSONObject.parseObject(HttpUtil.get(access_token_url));
            String accessToken = result.getString("access_token");
            if(StringUtils.isEmpty(accessToken)){
                throw new WxAccessTokenException("获取access_token错误!");
            }
            access_token = accessToken;
            redisTemplate.opsForValue().set("farmId:" + farmId + "--access_token",access_token,result.getLongValue("expires_in"), TimeUnit.SECONDS);
            log.info("获取token成功,token:{}",access_token);
        }
        return access_token;
    }
}
