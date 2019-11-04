package com.topwulian.controller;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.topwulian.constants.WXConstants;

import com.topwulian.dao.FarmDao;
import com.topwulian.dao.ProductBatchesDao;
import com.topwulian.dao.TProducterDao;
import com.topwulian.dto.TTaskDto;
import com.topwulian.dto.TaskDto;
import com.topwulian.dto.TaskMobileDto;
import com.topwulian.message.TemplateParam;

import com.topwulian.model.*;

import com.topwulian.service.DeviceService;
import com.topwulian.service.WxService;
import com.topwulian.utils.ParseBase64ImgUtil;
import com.topwulian.utils.SendWxTemplateMessageUtil;
import com.topwulian.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;

import com.topwulian.dao.TTaskDao;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/tTasks")
@Slf4j
public class TTaskController {

    @Autowired
    private TTaskDao tTaskDao;

    @Autowired
    private TProducterDao tProducterDao;

    @Autowired
    private ProductBatchesDao productBatchesDao;

    @Autowired
    private WxService wxService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private FarmDao farmDao;

    @Autowired
    private OSSClient ossClient;

    @Value("${file.aliyun.domain}")
    private String domain;


    @Value("${file.aliyun.bucketName}")
    private String bucketName;


    @PostMapping
    @ApiOperation(value = "保存")
    public TTask save(@RequestBody TTask tTask) {
        tTaskDao.save(tTask);

        return tTask;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TTask get(@PathVariable Long id) {
        return tTaskDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TTask update(@RequestBody TTask tTask) {
        tTaskDao.update(tTask);

        return tTask;
    }

    @PostMapping("/push")
    public RespEntiry pushTask(@RequestBody Map<String, String> map) {
        try {
            List<String> imgs = new ArrayList();
            //生成图片名称
            for (int i = 1; i <= 3; i++) {
                String img = map.get("img" + i);
                InputStream in = ParseBase64ImgUtil.parse(img);
                String imageName = IdUtil.simpleUUID();
                //上传SSO返回图片url地址
                String url = file_upload(imageName + ".jpeg", in);
                log.info("上传图片返回Url:{}", url);
                imgs.add(url);
            }
            String id = map.get("id");//获取id
            String area = map.get("message"); //获取描述
            //主装参数
            TTask tTask = new TTask();
            tTask.setId(Long.parseLong(id));
            tTask.setArea(area);
            tTask.setImg1(imgs.get(0));
            tTask.setImg2(imgs.get(1));
            tTask.setImg3(imgs.get(2));
            tTaskDao.updatePush(tTask);
            return RespEntiry.success("推送任务成功!");
        } catch (RuntimeException e) {
            log.info("推送任务失败.");
            e.printStackTrace();
            return RespEntiry.fail(e.getMessage());
        } catch (Exception e) {
            log.info("系统异常!");
            e.printStackTrace();
            return RespEntiry.fail();
        }

    }

    @PostMapping("/pull")
    @Transactional
    public RespEntiry pullTask(@RequestBody Map<String, String> map) {
        try {
            log.info("请求下发任务参数:{}", map);
            String productorId = map.get("productor");
            String productorBatchId = map.get("productBatch");
            TProducter producter = tProducterDao.getById(Long.parseLong(productorId));
            if (StringUtils.isEmpty(producter.getOpenid())) {
                return RespEntiry.fail("该生产者未绑定公众号,请先绑定!");
            }
            String taskId = map.get("ttask");
            String finish_date = map.get("finish_date");
            String desc = map.get("desc");
            TTask task = tTaskDao.getById(Long.parseLong(taskId));
            task.setProducterId(Integer.parseInt(productorId));
            task.setDescContent(desc);
            task.setActivityDate(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd"));
            task.setFinishDate(DateUtil.parse(finish_date, "yyyy-MM-dd"));
            task.setCreateDate(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
            tTaskDao.updatePull(task);
            JSONObject jsonObject = sendMessage(task, producter);
            log.info("发送消息模板结果:{}", jsonObject);
            if (jsonObject.getIntValue("errcode") != 0) return RespEntiry.fail(jsonObject.getString("发送消息模板失败!"));

            return RespEntiry.success("下发任务成功!");
        } catch (Exception e) {
            throw new RuntimeException("下发任务失败!");
        }
    }

    @GetMapping("/productBatchList/{product_batches_id}")
    List<Map<String, Object>> getProductBatchList(@PathVariable Integer product_batches_id) {

        int farmId = UserUtil.getUserFarmId();
        List<Map<String, Object>> taskContentList = tTaskDao.getTaskByProducter(farmId, product_batches_id);

        log.info("taskContentList----list:{}", taskContentList);

        return taskContentList;
    }

    @RequestMapping("/producterIdList")
    RespEntiry producterIdList(Integer producter_id, Integer state, @RequestParam(value = "p", defaultValue = "1") int p, @RequestParam(value = "psize", defaultValue = "5") int psize) {
        Map<String, Object> map = new HashMap();
        map.put("producter_id", producter_id);
        map.put("state", state);
        List<TTask> producterIdList = tTaskDao.getByProducterIdList(map, ((p - 1) * psize), psize);
        log.info("producterIdList----list:{}", producterIdList);
        return RespEntiry.success("查询成功!", producterIdList);
    }

    @RequestMapping("/imgList")
    RespEntiry imgList(Integer product_batches_id) {
        int farmId = UserUtil.getUserFarmId();
        List<Map<String, Object>> imgList = tTaskDao.getImgsList(farmId, product_batches_id);
        log.info("producterIdList----list:{}", imgList);
        return RespEntiry.success("查询成功!", imgList);
    }

    @ApiOperation(value = "生产人员任务列表")
    @RequestMapping("/listByProducterId")
    public PageTableResponse listByProducterId(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {
            @Override
            public int count(PageTableRequest request) {
                return tTaskDao.countByProducterId(request.getParams());
            }
        }, new ListHandler() {
            @Override
            public List<TTask> list(PageTableRequest request) {
                return tTaskDao.getTaskByProducterId(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @RequestMapping("/producterIdCount")
    RespEntiry producterIdCount(Integer producter_id, Integer state) {
        Map<String, Object> map = new HashMap();
        map.put("producter_id", producter_id);
        map.put("state", state);
        Integer count = tTaskDao.getByProducterIdCount(map);
        log.info("producterIdCount----count:{}", count);
        return RespEntiry.success("查询成功!", (Object) count);
    }

    @ApiOperation(value = "产品批次任务列表")
    @RequestMapping("/listByBatchId")
    public PageTableResponse listByBatchId(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {
            @Override
            public int count(PageTableRequest request) {

                int farmId = UserUtil.getUserFarmId();
                Map<String, Object> params = request.getParams();
                if (params == null) {
                    params = new HashMap();
                }
                params.put("farm_id", farmId);
                request.setParams(params);
                return tTaskDao.countByBatchId(request.getParams());
            }
        }, new ListHandler() {
            @Override
            public List<TaskDto> list(PageTableRequest request) {
                int farmId = UserUtil.getUserFarmId();
                Map<String, Object> params = request.getParams();
                if (params == null) {
                    params = new HashMap();
                }
                params.put("farm_id", farmId);
                request.setParams(params);
                return tTaskDao.getTaskAllList(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @ApiOperation(value = "扫码显示溯源码")
    @RequestMapping("/mobile")
    RespEntiry mobile(String id, String farmid) {
        try {
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(farmid)) {
                return RespEntiry.fail("查询参数错误!");
            }

            Farm farm = farmDao.getById(Long.parseLong(farmid));

            ProductBatches productBatches = productBatchesDao.getById(Long.parseLong(id));
            //当前基地的设备实时数据
            List<DeviceGather> deviceGathers = deviceService.getRealTimeDataByFarmId(Long.parseLong(farmid));
            List<TTaskDto> producterIdList = tTaskDao.getByMobileList(Long.parseLong(id));
            for (TTaskDto tTaskDto : producterIdList) {
                TProducter producter = tProducterDao.getById(new Long(tTaskDto.getProducterId()));
                tTaskDto.setUserName(producter.getUsername());
            }
            log.info("producterIdList----list:{}", producterIdList);
            TaskMobileDto taskMobileDto = new TaskMobileDto();
            taskMobileDto.setFarm(farm);
            taskMobileDto.setDeviceGathers(deviceGathers);
            taskMobileDto.setProducterIdList(producterIdList);
            taskMobileDto.setProductBatches(productBatches);
            return RespEntiry.success("查询成功!", taskMobileDto);
        } catch (Exception e) {
            log.info("系统异常:{}", e);
            return RespEntiry.fail("系统异常!");
        }

    }

    @GetMapping
    @ApiOperation(value = "农事审核列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {
            @Override
            public int count(PageTableRequest request) {

                int farmId = UserUtil.getUserFarmId();
                Map<String, Object> params = request.getParams();
                if (params == null) {
                    params = new HashMap();
                }
                params.put("farm_id", farmId);
                request.setParams(params);
                return tTaskDao.count(request.getParams());
            }
        }, new ListHandler() {
            @Override
            public List<TaskDto> list(PageTableRequest request) {
                int farmId = UserUtil.getUserFarmId();
                Map<String, Object> map = new HashMap();
                map.put("farm_id", farmId);
                request.setParams(map);
                return tTaskDao.getTaskDtoList(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @GetMapping("success")
    public RespEntiry pcSuccess(@RequestParam(value = "p", defaultValue = "1") int p, @RequestParam(value = "p", defaultValue = "10") int psize) {
        int farmId = UserUtil.getUserFarmId();
        Map<String, Object> map = new HashMap();
        map.put("farm_id", farmId);
        List<TaskDto> dtoList = tTaskDao.getTaskDtoList(map, (p - 1) * psize, psize);
        return RespEntiry.success(dtoList);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        tTaskDao.delete(id);
    }

    private String file_upload(String filename, InputStream in) {
        ossClient.putObject(bucketName, "upload/" + filename, in);
        String url = getUrl("upload/" + filename);
        return url;
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    private String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    private JSONObject sendMessage(TTask tTask, TProducter producter) {
        Map<String, TemplateParam> map = new HashMap<String, TemplateParam>();
        map.put("first", new TemplateParam("您好，您有新的任务需要处理!", "#FF3333"));
        map.put("keyword1", new TemplateParam(producter.getUsername(), "#0044BB"));
        map.put("keyword2", new TemplateParam(DateUtil.format(new Date(), "yyyy-MM-dd"), "#0044BB"));
        map.put("keyword3", new TemplateParam(tTask.getContent(), "#0044BB"));
        map.put("remark", new TemplateParam(tTask.getDescContent(), "#0044BB"));
        return SendWxTemplateMessageUtil.sendMessage(map, producter.getOpenid(), WXConstants.TASK_MESSAGE_ID, wxService.getToekn());
    }
}
