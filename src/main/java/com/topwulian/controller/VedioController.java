package com.topwulian.controller;

import com.topwulian.config.FileServiceFactory;
import com.topwulian.dao.VedioDao;
import com.topwulian.dto.MultipartFileDto;
import com.topwulian.model.User;
import com.topwulian.model.Vedio;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vedios")
public class VedioController {

    @Autowired
    private VedioDao vedioDao;

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;

    @Autowired
    private FileServiceFactory fileServiceFactory;

    /**
     * 抓取图片,并上传oss
     * https://open.ys7.com/doc/zh/book/index/device_option.html#device_option-api4
     * 请求地址:https://open.ys7.com/api/lapp/device/capture
     * 请求方式:POST
     * 请求参数:accessToken=at.12xp95k63bboast3aq0g5hg22q468929&deviceSerial=427734888&channelNo=2
     * 返回数据
     * {
     *     "data": {
     *         "picUrl": "http://img.ys7.com//group2/M00/74/22/CmGdBVjBVDCAaFNZAAD4cHwdlXA833.jpg"
     *     },
     *     "code": "200",
     *     "msg": "操作成功!"
     * }
     * @param
     * @return
     *
     * http://localhost:9001/vedios/savePic?picUrl=111
     */
    @GetMapping("/savePic")
    @ApiOperation(value = "将抓取的图片保存到阿里云")
    public void capture(String picUrl)throws Exception{
        //System.out.println(picUrl);
        HttpGet httpGet = new HttpGet("https://img13.360buyimg.com/n5/jfs/t17446/352/1415143705/203490/68cdb4ee/5ac74800N0824a445.jpg");
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        if (httpEntity!=null) {
            System.out.println("ContentType:"+httpEntity.getContentType().getValue());
            InputStream inputStream = httpEntity.getContent();

            FileService fileService = fileServiceFactory.getFileService("ALIYUN");
            //InputStream转换MultipartFile
            MultipartFile multipartFile = new MultipartFileDto("temp.jpg","temp.jpg",httpEntity.getContentType().getValue(), inputStream);
            fileService.upload(multipartFile);
        }
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public Vedio save(@RequestBody Vedio vedio) {
        vedioDao.save(vedio);

        return vedio;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Vedio get(@PathVariable Long id) {
        return vedioDao.getById(id);
    }


    @GetMapping("/vedioListByUserId")
    @ApiOperation(value = "根据id获取")
    public List<Vedio> vedioListByUserId() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Vedio> vedioList = vedioDao.getVedioListByUserId(user.getId());
        return vedioList;
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Vedio update(@RequestBody Vedio vedio) {
        vedioDao.update(vedio);

        return vedio;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return vedioDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Vedio> list(PageTableRequest request) {
                return vedioDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        vedioDao.delete(id);
    }
}
