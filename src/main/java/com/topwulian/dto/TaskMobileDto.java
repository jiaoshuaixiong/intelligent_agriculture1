package com.topwulian.dto;

import com.topwulian.model.DeviceGather;
import com.topwulian.model.Farm;
import com.topwulian.model.ProductBatches;

import java.util.List;

public class TaskMobileDto {

    private List<DeviceGather> deviceGathers;

    private List<TTaskDto> producterIdList;

    private ProductBatches productBatches;

    private Farm farm;

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public ProductBatches getProductBatches() {
        return productBatches;
    }

    public void setProductBatches(ProductBatches productBatches) {
        this.productBatches = productBatches;
    }

    public List<DeviceGather> getDeviceGathers() {
        return deviceGathers;
    }

    public void setDeviceGathers(List<DeviceGather> deviceGathers) {
        this.deviceGathers = deviceGathers;
    }

    public List<TTaskDto> getProducterIdList() {
        return producterIdList;
    }

    public void setProducterIdList(List<TTaskDto> producterIdList) {
        this.producterIdList = producterIdList;
    }
}
