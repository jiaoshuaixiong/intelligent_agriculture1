package com.topwulian.controller;

import com.topwulian.dao.DeviceDao;
import com.topwulian.dao.FarmDao;
import com.topwulian.model.Device;
import com.topwulian.model.DeviceGather;
import com.topwulian.model.Farm;
import com.topwulian.model.User;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.service.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private FarmDao farmDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/devicesState")
    @ApiOperation(value = "统计所有设备状态情况")
    public Map devicesState(PageTableRequest request,String state){
        if (state==null) {
            return deviceService.devicesState(request);
        }
        if (!state.equals('0') || !state.equals('1')) {
            request.getParams().put("state", state);
            return deviceService.devicesState(request);
        }
        return deviceService.devicesState(request);
    }

    @GetMapping("/realTimeData")
    @ApiOperation(value = "设备的实时数据,展示在面板上")
    public List<DeviceGather> realTimeData(Long farmId){
        User User = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = User.getId();
        //基地id默认就是当前拥有的第一个基地,如有多个,在切换的时候更新数据
        if (farmId == null) {
            //根据用户获取拥有的农场
            List<Farm> farmList = farmDao.getFarmListByUserId(userId);
            if (farmList != null && farmList.size() > 0) {
                farmId=farmList.get(0).getId();
            }
        }
        List<DeviceGather> deviceGathers=deviceService.getRealTimeDataByFarmId(farmId);
        return deviceGathers;
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public Device save(@RequestBody Device device) {
        deviceDao.save(device);
        redisTemplate.opsForHash().put("deviceThreshold",device.getId(),device);
        return device;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Device get(@PathVariable Long id) {
        return deviceDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Device update(@RequestBody Device device) {
        deviceDao.update(device);
        redisTemplate.opsForHash().put("deviceThreshold",device.getId(),device);
        return device;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return deviceDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Device> list(PageTableRequest request) {
                return deviceDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        deviceDao.delete(id);
        redisTemplate.opsForHash().delete("deviceThreshold",id);
    }
}
