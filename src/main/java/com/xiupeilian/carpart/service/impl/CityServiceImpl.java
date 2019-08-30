package com.xiupeilian.carpart.service.impl;

import com.xiupeilian.carpart.mapper.CityMapper;
import com.xiupeilian.carpart.model.City;
import com.xiupeilian.carpart.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityMapper cityMapper;

    @Override
    //指定该方法的返回值会被spring-cache进行缓存，需要指定使用哪一个缓存
    @Cacheable(value = "cache")
    public List<City> findCityByParentId(Integer parentId) {
        return cityMapper.findCityByParentId(parentId);
    }


}
