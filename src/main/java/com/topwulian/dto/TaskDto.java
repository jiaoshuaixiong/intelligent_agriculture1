package com.topwulian.dto;

import com.topwulian.model.TTask;


public class TaskDto extends TTask {

	private static final long serialVersionUID = 4218495592167610193L;

	private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
