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

import com.topwulian.dao.MeasurementUnitDao;
import com.topwulian.model.MeasurementUnit;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/measurementUnits")
public class MeasurementUnitController {

    @Autowired
    private MeasurementUnitDao measurementUnitDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public MeasurementUnit save(@RequestBody MeasurementUnit measurementUnit) {
        measurementUnitDao.save(measurementUnit);

        return measurementUnit;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public MeasurementUnit get(@PathVariable Long id) {
        return measurementUnitDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public MeasurementUnit update(@RequestBody MeasurementUnit measurementUnit) {
        measurementUnitDao.update(measurementUnit);

        return measurementUnit;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return measurementUnitDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<MeasurementUnit> list(PageTableRequest request) {
                return measurementUnitDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        measurementUnitDao.delete(id);
    }
}
