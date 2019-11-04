package com.topwulian.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.topwulian.dao.DeviceDao;
import com.topwulian.dao.DeviceGatherDao;
import com.topwulian.dao.FarmDao;
import com.topwulian.dto.DeviceGatherCharts;
import com.topwulian.dto.FarmDto;
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
import com.topwulian.utils.DateUtil;
import com.topwulian.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deviceGathers")
public class DeviceGatherController {

    @Autowired
    private DeviceGatherDao deviceGatherDao;

    @Autowired
    private FarmDao farmDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceDao deviceDao;

    @RequestMapping("/echartsShow")
    @ApiOperation(value = "echarts数据展示,要求按年月日")
    public Map<Long,List<DeviceGatherCharts> > echartsShow(PageTableRequest pageTableRequest){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setPassword(null);
        //按照farm和设备分别封装数据,一个农场的一个设备为一组数据
        return deviceService.echartsShow(user.getId(),pageTableRequest);
    }

    /**
     * 默认查询最近一个月的数据进行下载
     * 要按设备类型不同分别下载
     * 可以根据传入的时间范围进行下载
     * @param map
     * @param request
     * @param response
     * @param pageTableRequest
     */
    @RequestMapping("load")
    @ApiOperation(value = "历史数据下载")
    public void downloadByPoiBaseView(ModelMap map, HttpServletRequest request,
                                      HttpServletResponse response,PageTableRequest pageTableRequest) {
        FarmDto farmDto=new FarmDto();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setPassword(null);
        farmDto.setUser(user);
        //设备信息
        //基地id默认就是当前拥有的第一个基地,如有多个,在切换的时候动态更新数据
        //如果用户没有基地,那么farmId就为null,数据库也不会返回数据
        Long farmId = null;
        //根据用户获取拥有的农场
        List<Farm> farmList = farmDao.getFarmListByUserId(user.getId());
        List<DeviceGather> deviceGatherList=null;
        String createTimeRangeStr ="";
        if (farmList != null && farmList.size() > 0) {
            Farm farm = farmList.get(0);//将用户拥有的第一个农场查询出来,这里是有问题的,需要优化
            farmId=farm.getId();
            List<Device> deviceList = deviceDao.getByFarmId(farmId);
            //封装查询条件
            createTimeRangeStr = (String) pageTableRequest.getParams().get("createTimeRange");

            if (StringUtils.isNotEmpty(createTimeRangeStr)) {
                //2019-01-11 - 2019-02-03
                String[] createTimeRangeStrS = createTimeRangeStr.split(" - ");
                pageTableRequest.getParams().put("startTime", createTimeRangeStrS[0]);
                pageTableRequest.getParams().put("endTime", createTimeRangeStrS[1]);
            }else {
                //如果没有传时间,就显示最近一个月的
                String startTime=DateUtil.dateToString(DateUtil.getBeforeByMonth(1));
                String endTime=DateUtil.dateToString(new Date());
                pageTableRequest.getParams().put("startTime",startTime);
                pageTableRequest.getParams().put("endTime", endTime);
                createTimeRangeStr=startTime+" - "+endTime;
            }

            //将分页参数设置为空
            pageTableRequest.setLimit(null);
            pageTableRequest.setOffset(null);

            deviceGatherList=deviceService.getHistoryDataByFarmId(pageTableRequest,farmId);

        }

        ExportParams params = new ExportParams("采集数据显示", "概览", ExcelType.XSSF);
        params.setFreezeCol(2);
        map.put(NormalExcelConstants.DATA_LIST, deviceGatherList);
        map.put(NormalExcelConstants.CLASS, DeviceGather.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, createTimeRangeStr);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

    }

    @PostMapping
    @ApiOperation(value = "保存")
    public DeviceGather save(@RequestBody DeviceGather deviceGather) {
        deviceGatherDao.save(deviceGather);

        return deviceGather;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public DeviceGather get(@PathVariable Long id) {
        return deviceGatherDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public DeviceGather update(@RequestBody DeviceGather deviceGather) {
        deviceGatherDao.update(deviceGather);

        return deviceGather;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        String createTimeRangeStr = (String) request.getParams().get("createTimeRange");
        int userFarmId = UserUtil.getUserFarmId();
        if (StringUtils.isNotEmpty(createTimeRangeStr)) {
            //2019-01-11 - 2019-02-03
            String[] createTimeRangeStrS = createTimeRangeStr.split(" - ");
            request.getParams().put("startTime", createTimeRangeStrS[0]);
            request.getParams().put("endTime", createTimeRangeStrS[1]);
        }

        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return deviceGatherDao.count1(request.getParams(),userFarmId);
            }
        }, new ListHandler() {

            @Override
            public List<DeviceGather> list(PageTableRequest request) {
                return deviceGatherDao.list1(request.getParams(), request.getOffset(), request.getLimit(),userFarmId);
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        deviceGatherDao.delete(id);
    }
}
