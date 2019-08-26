package com.xiupeilian.carpart.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiupeilian.carpart.model.Notice;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.vo.StaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class Staffcontroller {

    @Autowired
    private UserService userService;
    @RequestMapping("/staffList")
    public String staffList(StaffVo vo,HttpServletRequest request){

        if(vo.getPageNo()==null){
            vo.setPageNo(1);
        }
        if(vo.getPageSize()==null){
            vo.setPageSize(10);
        }
        PageHelper.startPage(vo.getPageNo(),vo.getPageSize());
        List<SysUser> staffList=userService.findUserByLoginName(vo);
        PageInfo<SysUser> page=new PageInfo<>(staffList);
        request.setAttribute("page",page);
        request.setAttribute("staffList",staffList);
        request.setAttribute("vo",vo);
        return  "index/staffList";
    }
}
