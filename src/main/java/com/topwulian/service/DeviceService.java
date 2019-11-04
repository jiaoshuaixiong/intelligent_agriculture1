package com.topwulian.service;

import com.topwulian.dto.DeviceGatherCharts;
import com.topwulian.model.DeviceGather;
import com.topwulian.page.table.PageTableRequest;

import java.util.List;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/12/26 下午2:01
 * @Version 1.0
 */
public interface DeviceService {
    List<DeviceGather> getRealTimeDataByFarmId(Long farmId);
    List<DeviceGather> getHistoryDataByFarmId(PageTableRequest pageTableRequest, Long farmId);

    Map<Long,List<DeviceGatherCharts> > echartsShow(Long userId, PageTableRequest pageTableRequest);

    Map devicesState(PageTableRequest request);
}
