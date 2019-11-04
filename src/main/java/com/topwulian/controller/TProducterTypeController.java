package com.topwulian.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;

import com.topwulian.dao.TProducterTypeDao;
import com.topwulian.model.TProducterType;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tProducterTypes")
public class TProducterTypeController {

    @Autowired
    private TProducterTypeDao tProducterTypeDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TProducterType save(@RequestBody TProducterType tProducterType) {
        tProducterTypeDao.save(tProducterType);

        return tProducterType;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TProducterType get(@PathVariable Long id) {
        return tProducterTypeDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TProducterType update(@RequestBody TProducterType tProducterType) {
        tProducterTypeDao.update(tProducterType);

        return tProducterType;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tProducterTypeDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TProducterType> list(PageTableRequest request) {
                return tProducterTypeDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        tProducterTypeDao.delete(id);
    }
}
