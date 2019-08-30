package com.xiupeilian.carpart.service.impl;

import com.xiupeilian.carpart.mapper.ItemsMapper;
import com.xiupeilian.carpart.model.Items;
import com.xiupeilian.carpart.service.ItemsSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemsSerivceimpl implements ItemsSerivce {
    @Autowired
    private ItemsMapper itemsMapper;
    @Override
    @Cacheable(value = "cache")
    public List<Items> findItemsByQueryVo(Items items) {
        return itemsMapper.findItemsByQueryVo(items);
    }
}
