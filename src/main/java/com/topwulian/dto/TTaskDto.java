package com.topwulian.dto;

import com.topwulian.model.TTask;

public class TTaskDto extends TTask {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
