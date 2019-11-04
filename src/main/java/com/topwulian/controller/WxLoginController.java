package com.topwulian.controller;

import com.topwulian.model.RespEntiry;
import com.topwulian.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("wxlogin")
public class WxLoginController {




    @Autowired
    private WxService wxService;
    /**
     *
     * @return
     */
    @RequestMapping("login")
    public RespEntiry login(@RequestBody Map<String,String> param){
        try{
            return wxService.login(param.get("code"),param.get("phone"),param.get("pass"));
        }catch (RuntimeException e){
            return RespEntiry.fail(e.getMessage());
        }catch (Exception e){
            return RespEntiry.fail("系统异常!");
        }
    }


}
