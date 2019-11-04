package com.topwulian.controller;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSSClient;
import com.topwulian.model.RespEntiry;
import com.topwulian.model.User;
import com.topwulian.service.ProductBatchesService;
import com.topwulian.service.impl.ProductBatchesServiceImpl;
import com.topwulian.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;

import com.topwulian.dao.ProductBatchesDao;
import com.topwulian.model.ProductBatches;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/productBatchess")
@Slf4j
public class ProductBatchesController {


    @Autowired
    private ProductBatchesDao productBatchesDao;

    @Autowired
    private ProductBatchesService productBatchesService;


    @Autowired
    private OSSClient ossClient;

    @Value("${file.aliyun.domain}")
    private String domain;


    @Value("${file.aliyun.bucketName}")
    private String bucketName;

    @PostMapping
    @ApiOperation(value = "保存")
    public RespEntiry save(@RequestParam(name = "img") MultipartFile[] uploadFiles, HttpServletRequest request) {
        try {
            log.info("进入插入产品批次");
            //新建实体
            ProductBatches productBatches = new ProductBatches();
            String url = "";
            for (MultipartFile uploadFile : uploadFiles) {
                String originalFilename = uploadFile.getOriginalFilename();
                //获取图片后缀
                String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                //生成图片名称
                String imageName = IdUtil.simpleUUID();
                //上传SSO返回图片url地址
                 url = file_upload(imageName+"."+extName, uploadFile.getInputStream());
                log.info("上传图片返回Url:{}",url);
            }

            //组装参数
            productBatches.setProductName(request.getParameter("productName"));
            productBatches.setImg(url);
            productBatches.setFeature(request.getParameter("feature"));
            productBatches.setRecoverydate(DateUtil.parse(request.getParameter("recoverydate"),"yyyy-MM-dd") );
            productBatches.setArea( Integer.parseInt(request.getParameter("area")) );
            productBatches.setProductBatch(request.getParameter("productBatch"));
            //获取当前登录用户,并设置UserID
            User user = getCurrentUser();
            productBatches.setSysUserId(user.getId().intValue());
            //获取全部任务
            Map<String, String> tasks = getTasks(request);
            //TODO 下发给service 处理
            productBatchesService.insert(productBatches,tasks);
            log.info("插入产品批次成功.");
            return RespEntiry.success();
        }catch (RuntimeException e) {
            log.info("插入产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail(e.getMessage());
        } catch (Exception e) {
            log.info("插入产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public ProductBatches get(@PathVariable Long id) {
        log.info("查询产品批次id:{}",id);
        return productBatchesDao.getById(id);
    }

    @GetMapping("/productBatchList")
    @ApiOperation(value = "获取可用产品批次")
    public List<Map<String,Object>> getProductBatchList() {

        int farmId = UserUtil.getUserFarmId();

        List<Map<String,Object>> list =  productBatchesDao.getProductBatchList(farmId);
        log.info("list:{}",list);
        return list;
    }

    @GetMapping("/productBatchListByActive")
    @ApiOperation(value = "获取可用产品批次")
    public List<Map<String,Object>> productBatchListByActive() {

        int farmId = UserUtil.getUserFarmId();

        List<Map<String,Object>> list =  productBatchesDao.getProductBatchByActive(farmId,'Y');
        log.info("list:{}",list);
        return list;
    }
    //匹配单个
    @InitBinder
    public void initData(WebDataBinder wdb){
        wdb.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));

    }
    @PutMapping
    @ApiOperation(value = "修改")
    public RespEntiry update(@RequestParam(name = "img") MultipartFile uploadFile,
                             ProductBatches productBatches,
                             HttpServletRequest request) {
        try {
            log.info("进入修改产品批次");
            //新建实体
            String originalFilename = uploadFile.getOriginalFilename();
            //获取图片后缀
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //生成图片名称
            String imageName = IdUtil.simpleUUID();
            //上传SSO返回图片url地址
            String url = file_upload(imageName+"."+extName, uploadFile.getInputStream());
            log.info("上传图片返回Url:{}",url);
            productBatches.setImg(url);
            //获取当前登录用户,并设置UserID
            //获取全部任务
            Map<String, String> tasks = getTasks(request);
            //TODO 下发给service 处理
            productBatchesService.update(productBatches,tasks);
            log.info("修改产品批次成功.");
            return RespEntiry.success();
        }catch (RuntimeException e) {
            log.info("修改产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail(e.getMessage());
        } catch (Exception e) {
            log.info("修改产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail();
        }
    }

    @RequestMapping("/updateImg")
    @ApiOperation(value = "修改")
    public RespEntiry updateNoImg(ProductBatches productBatches,
                             HttpServletRequest request) {
        try {
            log.info("进入修改产品批次");
            //获取当前登录用户,并设置UserID
            //获取全部任务
            Map<String, String> tasks = getTasks(request);
            //TODO 下发给service 处理
            productBatchesService.update(productBatches,tasks);
            log.info("修改产品批次成功.");
            return RespEntiry.success();
        }catch (RuntimeException e) {
            log.info("修改产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail(e.getMessage());
        } catch (Exception e) {
            log.info("修改产品批次失败.");
            e.printStackTrace();
            return RespEntiry.fail();
        }
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(
                productBatchesService,productBatchesService).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        productBatchesDao.delete(id);
    }




    private String file_upload(String filename,InputStream in){
        ossClient.putObject(bucketName, "upload/"+filename, in);

        String url = getUrl("upload/" + filename);
        return url;
    }


    /**
     * 组装task task是动态的
     * @param request
     * @return
     */
    private Map<String,String> getTasks(HttpServletRequest request){

        Map<String,String> map = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            String key = "task" + i;
            String result = request.getParameter(key);
            if(request != null && !"null".equals(result+"") && !"".equals(result)){
                map.put(key,result);
            }
        }

        return map;
    }

    public User getCurrentUser(){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        return user;
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


}
