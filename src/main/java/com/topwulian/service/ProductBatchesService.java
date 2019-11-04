package com.topwulian.service;

import com.topwulian.model.ProductBatches;
import com.topwulian.page.table.PageTableHandler;

import java.util.Map;

public interface ProductBatchesService extends  PageTableHandler.CountHandler,PageTableHandler.ListHandler {

    public void insert(ProductBatches productBatches, Map<String, String> map);

    public void update(ProductBatches productBatches, Map<String, String> tasks);

    public void deleteById(Long id);
}
