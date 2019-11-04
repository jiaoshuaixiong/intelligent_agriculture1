package com.topwulian.service;

import com.topwulian.model.RespEntiry;

public interface WxService {

    public RespEntiry login(String code, String phone, String pass);

    public String getToekn();
}
