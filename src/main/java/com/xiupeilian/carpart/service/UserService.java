package com.xiupeilian.carpart.service;

import com.xiupeilian.carpart.model.Menu;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.vo.LoginVo;
import com.xiupeilian.carpart.vo.StaffVo;

import java.util.List;

public interface UserService {
    SysUser findUserByLoginNameAndPassword(LoginVo vo);

    List<Menu> findMenusByUserId(Integer id);

    SysUser findUserByLoginNameAndEmail(LoginVo vo);

    void updateUser(SysUser user);

    List<SysUser> findUserByLoginName(StaffVo vo);

    SysUser getUserByLoginName(String loginName);

    SysUser findUserByPhone(String telnum);

    SysUser findUserByEmail(String email);
}
