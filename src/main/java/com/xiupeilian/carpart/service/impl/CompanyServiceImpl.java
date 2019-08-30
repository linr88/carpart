package com.xiupeilian.carpart.service.impl;

import com.xiupeilian.carpart.mapper.CompanyMapper;
import com.xiupeilian.carpart.model.Company;
import com.xiupeilian.carpart.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyMapper companyMapper;
    @Override
    public Company findCompanyByCompanyname(String companyname) {
        return companyMapper.findCompanyByCompanyname(companyname);
    }
}
