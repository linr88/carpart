package com.xiupeilian.carpart.controller;

import com.xiupeilian.carpart.constant.SysConstant;
import com.xiupeilian.carpart.util.AliyunOSSClientUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/upLoad")
public class Uploadcontriller {
    @RequestMapping("/toupload")
    public String toupload(){
        System.out.println("12121212");
        System.out.println("555555");
        return "upload/index";
    }
    @RequestMapping(value = "/photoupload", method = {RequestMethod.POST, RequestMethod.GET})
    public void myphotoupload(HttpServletRequest request, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File f = fi.getStoreLocation();
        System.err.println(AliyunOSSClientUtil.uploadObject2OSS(AliyunOSSClientUtil.getOSSClient(), f, SysConstant.BACKET_NAME, SysConstant.FOLDER));
        //String url = AliyunOSSClientUtil.uploadObject2OSS(AliyunOSSClientUtil.getOSSClient(), f, SysConstant.BACKET_NAME, SysConstant.FOLDER);
        System.out.println(AliyunOSSClientUtil.getUrl((SysConstant.FOLDER+f.getName())));
        //System.out.println("图片的访问地址"+"https://"+SysConstant.BACKET_NAME+"."+SysConstant.ENDPOINT+"/"+SysConstant.FOLDER+f.getName());
        String imgUrl="https://"+SysConstant.BACKET_NAME+"."+SysConstant.ENDPOINT+"/"+SysConstant.FOLDER+f.getName();
        response.getWriter().write(AliyunOSSClientUtil.getUrl((SysConstant.FOLDER+f.getName())));
        System.out.println(imgUrl);

    }
}
