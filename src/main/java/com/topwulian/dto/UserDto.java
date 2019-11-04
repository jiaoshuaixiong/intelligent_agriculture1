package com.topwulian.dto;

import com.topwulian.model.User;

import java.util.List;

public class UserDto extends User {

	private static final long serialVersionUID = -184009306207076712L;

	private List<Long> roleIds;

	private List<Long> farmIds;

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

    public List<Long> getFarmIds() {
        return farmIds;
    }

    public void setFarmIds(List<Long> farmIds) {
        this.farmIds = farmIds;
    }
}
