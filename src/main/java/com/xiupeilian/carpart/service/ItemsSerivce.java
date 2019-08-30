package com.xiupeilian.carpart.service;

import com.xiupeilian.carpart.model.Items;

import java.util.List;

public interface ItemsSerivce {
    List<Items> findItemsByQueryVo(Items items);
}
