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

import com.topwulian.dao.SysYs7AccountDao;
import com.topwulian.model.SysYs7Account;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sysYs7Accounts")
public class SysYs7AccountController {

    @Autowired
    private SysYs7AccountDao sysYs7AccountDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public SysYs7Account save(@RequestBody SysYs7Account sysYs7Account) {
        sysYs7AccountDao.save(sysYs7Account);

        return sysYs7Account;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public SysYs7Account get(@PathVariable Long id) {
        return sysYs7AccountDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public SysYs7Account update(@RequestBody SysYs7Account sysYs7Account) {
        sysYs7AccountDao.update(sysYs7Account);

        return sysYs7Account;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return sysYs7AccountDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<SysYs7Account> list(PageTableRequest request) {
                return sysYs7AccountDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        sysYs7AccountDao.delete(id);
    }
}
