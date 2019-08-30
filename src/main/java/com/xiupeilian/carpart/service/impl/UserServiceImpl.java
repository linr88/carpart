package com.xiupeilian.carpart.service.impl;

import com.xiupeilian.carpart.constant.SysConstant;
import com.xiupeilian.carpart.mapper.CompanyMapper;
import com.xiupeilian.carpart.mapper.MenuMapper;
import com.xiupeilian.carpart.mapper.RoleMapper;
import com.xiupeilian.carpart.mapper.SysUserMapper;
import com.xiupeilian.carpart.model.Company;
import com.xiupeilian.carpart.model.Menu;
import com.xiupeilian.carpart.model.Role;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.util.SHA1Util;
import com.xiupeilian.carpart.vo.LoginVo;
import com.xiupeilian.carpart.vo.RegisterVo;
import com.xiupeilian.carpart.vo.StaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public SysUser findUserByLoginNameAndPassword(LoginVo vo) {
        return sysUserMapper.findUserByLoginNameAndPassword(vo);
    }

    @Override
    public List<Menu> findMenusByUserId(Integer id) {
        return menuMapper.findMenusByUserId(id);
    }

    @Override
    public SysUser findUserByLoginNameAndEmail(LoginVo vo) {
        return sysUserMapper.findUserByLoginNameAndEmail(vo);
    }

    @Override
    public void updateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<SysUser> findUserByLoginName(StaffVo vo) {
        return sysUserMapper.findUserByLoginName(vo);
    }

    @Override
    public SysUser getUserByLoginName(String loginName) {
        return sysUserMapper.getUserByLoginName(loginName);
    }

    @Override
    public SysUser findUserByPhone(String phone1) {
        return sysUserMapper.findUserByPhone(phone1);
    }

    @Override
    public SysUser findUserByEmail(String email) {
        return sysUserMapper.findUserByEmail(email);
    }

    @Override
    public void addRegister(RegisterVo vo) {
        //先插入企业表
        Company company=new Company();
        company.setAddress(vo.getAddress());
        company.setCity(vo.getCity());
        company.setCompanyCode(UUID.randomUUID().toString());
        company.setCompanyName(vo.getCompanyname());
        company.setCounty(vo.getContry());
        company.setCreateTime(new Date());
        company.setLeader(vo.getUsername());
        company.setMain(vo.getMain());
        company.setPhone(vo.getPhone());
        company.setZone1(vo.getZone1());
        company.setZone2(vo.getZone2());
        company.setTel1(vo.getTel1());
        company.setTel2(vo.getTel2());
        company.setPrime(vo.getPrime());
        company.setSingleParts(vo.getSingleParts());
        company.setQq(vo.getQq());
        companyMapper.insertSelective(company);

        //再插入用户表
        SysUser user=new SysUser();
        user.setPassword(SHA1Util.encode(vo.getPassword()));
        user.setCompanyId(company.getId());
        user.setCreateTime(new Date());
        user.setEmail(vo.getEmail());
        user.setLoginName(vo.getLoginName());
        user.setManageLevel(3);
        user.setPhone(vo.getPhone());
        user.setRoleId(SysConstant.ROLE_ADMIN);
        user.setUsername(vo.getUsername());
        sysUserMapper.insertSelective(user);
    }

    @Override
    public Role findRoleByRoleId(Integer roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<Menu> findMenusById(Integer id) {
        return menuMapper.findMenusByUserId(id);
    }


}
