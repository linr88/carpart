package com.xiupeilian.carpart.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiupeilian.carpart.model.Dymsn;
import com.xiupeilian.carpart.model.Menu;
import com.xiupeilian.carpart.model.Notice;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.DymsnService;
import com.xiupeilian.carpart.service.UserService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private UserService userService;
   @Autowired
   private DymsnService dymsnService;

    @RequestMapping("/index")
    public String toIndex(){
        return "index/index";
    }
    @RequestMapping("/top")
    public String top(HttpServletRequest request){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        request.setAttribute("now",sdf.format(new Date()));
        return "index/top";
    }
    @RequestMapping("/navigation")
    public String navigation(HttpServletRequest request){
        SysUser user=(SysUser)request.getSession().getAttribute("user");
        List<Menu> menuList=userService.findMenusByUserId(user.getId());
        request.setAttribute("menuList",menuList);
        return "index/navigation";
    }
    @RequestMapping("/dymsn")
    public String dymsn(HttpServletRequest request){
        System.out.println(1212121);
        List<Dymsn> list=dymsnService.findDymsn();

        System.out.println(list.size());
         request.setAttribute("list",list);
        return "index/dymsn";
    }
    @RequestMapping("/notice")
    public String notice(Integer pageSize,Integer pageNum,HttpServletRequest request){
        pageSize=pageSize==null?10:pageSize;
        pageNum=pageNum==null?1:pageNum;
        PageHelper.startPage(pageNum,pageSize);
        //查询全部
        List<Notice> list=dymsnService.findNotice();
        PageInfo<Notice> page=new PageInfo<>(list);
        request.setAttribute("page",page);
        return "index/notice";
    }
}
