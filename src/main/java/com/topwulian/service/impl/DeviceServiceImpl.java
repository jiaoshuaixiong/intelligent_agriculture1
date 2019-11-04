package com.topwulian.service.impl;

import com.topwulian.dao.DeviceDao;
import com.topwulian.dao.DeviceGatherDao;
import com.topwulian.dao.FarmDao;
import com.topwulian.dao.VedioDao;
import com.topwulian.dto.DeviceGatherCharts;
import com.topwulian.model.Device;
import com.topwulian.model.DeviceGather;
import com.topwulian.model.Farm;
import com.topwulian.model.Vedio;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.service.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/12/26 下午1:15
 * @Version 1.0
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private DeviceGatherDao deviceGatherDao;

    @Autowired
    private FarmDao farmDao;

    @Autowired
    private VedioDao vedioDao;


    /**
     * 获取采集到的实时数据,根据每个不同的农场分别进行返回
     * 页面需要将农场的id传过来
     * 每个农场的设备很多,此处应该获取所有设备的最新采集数据封装并且返回
     * @param farmId
     * @return
     */
    public List<DeviceGather> getRealTimeDataByFarmId(Long farmId) {
        List<DeviceGather> deviceGathers = new ArrayList<>();
        //根据农场id获取该农场的所有设备
        List<Device> deviceList = deviceDao.getByFarmId(farmId);
        for (Device device : deviceList) {
            //从deviceGather表中根据设备的id查询每个设备的最后一条采集记录
            //主键倒序取一条记录即可 ORDER BY id DESC LIMIT 1
            List<DeviceGather> deviceGatherList = deviceGatherDao.getByDeviceId(device.getId(),1);
            if (deviceGatherList!=null&&deviceGatherList.size()>0){
                //封装数据返回
                deviceGathers.add(deviceGatherList.get(0));
            }
        }
        return  deviceGathers;
    }

    /**
     * 得到历史采集数据
     * @param pageTableRequest
     * @param farmId
     * @return
     */
    @Override
    public List<DeviceGather> getHistoryDataByFarmId(PageTableRequest pageTableRequest,Long farmId) {
        List<DeviceGather> deviceGathers = new ArrayList<>();

        //如果设备id直接传递过来了,那就按设备id直接查询
        Object deviceIdObj = pageTableRequest.getParams().get("deviceId");
        if (deviceIdObj != null) {
            Long deviceId= (Long) deviceIdObj;
            deviceGathers = deviceGatherDao.list(pageTableRequest.getParams(), null, null);
            return deviceGathers;
        }

        //没有设备id的情况下,根据农场id获取该农场的所有设备,并全部返回
        List<Device> deviceList = deviceDao.getByFarmId(farmId);
        for (Device device : deviceList) {
            //不需要分页参数,并且设置设备id
            pageTableRequest.getParams().put("deviceId",device.getId());
            List<DeviceGather> deviceGatherList = deviceGatherDao.list(pageTableRequest.getParams(),null,null);
            if (deviceGatherList!=null&&deviceGatherList.size()>0){
                //封装数据返回
                for (DeviceGather deviceGather : deviceGatherList) {
                    deviceGathers.add(deviceGather);
                }
            }
        }

        return deviceGathers;
    }

    /**
     * 返回当前农场所拥有的所有设备的采集数据
     * @param userId
     * @param pageTableRequest
     * @return
     */
    @Override
    public Map<Long,List<DeviceGatherCharts> >  echartsShow(Long userId, PageTableRequest pageTableRequest) {
        String timeUnit = (String) pageTableRequest.getParams().get("timeUnit");
        if (StringUtils.isEmpty(timeUnit)||"undefined".equals(timeUnit)) {
            pageTableRequest.getParams().put("timeUnit","HOUR");//前端不传,按小时显示 MONTH YEAR
        }

        Map<Long,List<DeviceGatherCharts>> viewChartsData=new HashMap<>();
        List<Farm> farmList = farmDao.listByUserId(userId);
        String createTimeRangeStr ="";
        if (farmList != null && farmList.size() > 0) {
            //TODO 将用户拥有的第一个农场查询出来,这里是有问题的,需要优化,应该修改为根据前端传递过来的基地id进行切换
            Farm farm = farmList.get(0);
            Long farmId=farm.getId();
            List<Device> deviceList = deviceDao.getByFarmId(farmId);
            for (Device device : deviceList) {
                //根据每个设备分别查询对应的数据
                pageTableRequest.getParams().put("deviceId",device.getId());
                List<DeviceGatherCharts> DeviceGatherChartsList=deviceGatherDao.echartsShow(pageTableRequest);
                viewChartsData.put(device.getId(),DeviceGatherChartsList);
            }
        }
        return viewChartsData;
    }

    /**
     * 管理员统计所有设备情况
     * @param request
     * @return
     */
    @Override
    public Map devicesState(PageTableRequest request) {
        List<Device> deviceList = deviceDao.list(request.getParams(),null,null);
        List<Vedio> vedioList = vedioDao.list(request.getParams(),null,null);
        Map resultMao = new HashMap();
        resultMao.put("deviceList", deviceList);
        resultMao.put("vedioList", vedioList);
        return resultMao;
    }
}
