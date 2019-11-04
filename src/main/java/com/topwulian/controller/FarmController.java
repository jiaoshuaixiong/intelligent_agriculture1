package com.topwulian.controller;

import com.google.common.collect.Maps;
import com.topwulian.dao.*;
import com.topwulian.dto.FarmDto;
import com.topwulian.model.*;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.service.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/farms")
public class FarmController {

    @Autowired
    private FarmDao farmDao;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private DeviceLogDao deviceLogDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private VedioDao vedioDao;

    @Autowired
    private UserDao userDao;

    @GetMapping("/info/{noticeRows}/{deviceLogRows}")
    @ApiOperation(value = "我的基地页面显示各种详情")
    public FarmDto farmInfo(@PathVariable Integer noticeRows,@PathVariable Integer deviceLogRows){
        FarmDto farmDto=new FarmDto();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setPassword(null);
        farmDto.setUser(user);
        //农情通知,显示最近条数,由前端传入
        List<Notice> noticeList = noticeDao.recentlyNotice(0, noticeRows);
        //设备报警
        List<DeviceLog> deviceLogList = deviceLogDao.deviceNotice(0, deviceLogRows, user.getId());

        //根据用户id获取拥有的基地,在farm_user表中配置了用户和基地的关系
        //前端默认显示用户拥有的第一个基地,可以通过下拉列表来切换基地

        //设备信息
        //基地id默认就是当前拥有的第一个基地,根据前端传递过来的farmId进行切换
        //根据用户获取拥有的农场
        Long farmId = null;
        List<Farm> farmList = farmDao.listByUserId(user.getId());
        if (farmList != null && farmList.size() > 0) {
            Farm farm = farmList.get(0);
            farmId=farm.getId();
            farmDto.setFarm(farm);
            List<Device> deviceList = deviceDao.getByFarmId(farmId);
            //当前基地的设备实时数据
            List<DeviceGather> deviceGathers=deviceService.getRealTimeDataByFarmId(farmId);

            //当前基地的摄像头列表
            List<Vedio> vedioList=vedioDao.getVedioListByFarmId(farmId);
            farmDto.setDeviceList(deviceList);
            farmDto.setRealTimeDataList(deviceGathers);
            farmDto.setVedioList(vedioList);
        }

        farmDto.setRecentlyNoticeList(noticeList);
        farmDto.setDeviceNoticeList(deviceLogList);

        return farmDto;
    }

    @PostMapping
    @ApiOperation(value = "保存")
    @Transactional
    public Farm save(@RequestBody Farm farm){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //给基地设置管理员,其实就是farm中的userId
        farm.setUserId(user.getId());
        //mybatis保存到数据库后获取主键
        farmDao.save(farm);
        //同时设置基地和用户的关联关系
        List<Long> farmIds=new ArrayList<>();
        farmIds.add(farm.getId());
        userDao.saveUserFarms(user.getId(),farmIds);
        return farm;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Farm get(@PathVariable Long id) {
        return farmDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Farm update(@RequestBody Farm farm) {
        farmDao.update(farm);
        return farm;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return farmDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Farm> list(PageTableRequest request) {
                return farmDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    @Transactional
    public void delete(@PathVariable Long id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        farmDao.delete(id);
        userDao.deleteUserIdAndFarmId(user.getId(),id);
    }

    @GetMapping("/all")
    @ApiOperation(value = "所有角色")
    public List<Farm> farms() {
        return farmDao.list(Maps.newHashMap(), null, null);
    }

    @GetMapping(params = "userId")
    @ApiOperation(value = "根据用户id去user_farm中间表获取拥有的基地")
    public List<Farm> farms(Long userId) {
        return farmDao.listByUserId(userId);
    }

}
