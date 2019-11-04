package com.topwulian.dto;

import java.util.Date;

public class Data{
        private String accessToken;
        private Date expireTime;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Date getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Date expireTime) {
            this.expireTime = expireTime;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "accessToken='" + accessToken + '\'' +
                    ", expireTime=" + expireTime +
                    '}';
        }
    }