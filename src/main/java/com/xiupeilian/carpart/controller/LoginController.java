package com.xiupeilian.carpart.controller;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.xiupeilian.carpart.constant.SysConstant;
import com.xiupeilian.carpart.model.*;
import com.xiupeilian.carpart.service.BrandService;
import com.xiupeilian.carpart.service.CityService;
import com.xiupeilian.carpart.service.CompanyService;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.task.MailTask;
import com.xiupeilian.carpart.util.SHA1Util;
import com.xiupeilian.carpart.vo.LoginVo;
import com.xiupeilian.carpart.vo.RegisterVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RedisTemplate jedis;
   @RequestMapping("/toLogin")
    public String toLogin(){
        return "login/login";
    }
    @RequestMapping("/login")
    public void login(LoginVo vo, HttpServletRequest request, HttpServletResponse response) throws  Exception{
            //判断验证码是否正确
            String code=(String) request.getSession().getAttribute(SysConstant.VALIDATE_CODE);
        if(vo.getValidate().toUpperCase().equals(code.toUpperCase())){
            //验证码正确
            Subject subject= SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(vo.getLoginName(),vo.getPassword());
            try {
                subject.login(token);
            }catch (Exception e){
                //用户名密码错误
                response.getWriter().write(e.getMessage());
                return ;
            }
            //获取存入的用户信息
            SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
            //存spring-session
            request.getSession().setAttribute("user",user);
            response.getWriter().write("3");
        }else{
            //验证码错误
            response.getWriter().write("1");
        }


    }
    @RequestMapping("/noauth")
    public String noauth(){
       return "exception/noauth";
    }
    @RequestMapping("/forgetPassword")
    public String forgetPassword(){

       return "login/forgetPassword";
    }

    @RequestMapping("/getPassword")
    public void getPassword(HttpServletResponse response,LoginVo vo) throws IOException {

       SysUser user=userService.findUserByLoginNameAndEmail(vo);

       if (user==null){
           response.getWriter().write("1");
       }else {
           //生成新密码
            String password=new Random().nextInt(899999)+100000+"";

           //修改数据库
            user.setPassword(SHA1Util.encode(password));
            userService.updateUser(user);
           //发送邮件
           SimpleMailMessage message=new SimpleMailMessage();
           message.setFrom("ryb969696@sina.com");
           message.setTo(user.getEmail());
           message.setSubject("修配连汽配市场密码找回：");
           message.setText("您的新密码是"+password);

            mailSender.send(message);
            //创建了有一个任务交给spring的线程池就可以了
           MailTask mailTask=new MailTask(mailSender,message);
           executor.execute(mailTask);
            response.getWriter().write("2");
            System.out.println(1212);
           System.out.println(1212);
       }
    }
    //初始化页面数据
    @RequestMapping("/toRegister")
    public String toRegister(HttpServletRequest request){
        List<Brand> brandList=brandService.findBrandAll();
        List<Parts> partsList=brandService.findPartsAll();
        List<Prime> primelist=brandService.findPrimeAll();
        request.setAttribute("brandList",brandList);
        request.setAttribute("partsList",partsList);
        request.setAttribute("primeList",primelist);

        return  "login/register";
    }
    //检查用户名
    @RequestMapping("/checkLoginName")
    public void  checkLoginName(String loginName,HttpServletResponse response)throws Exception{
       SysUser user= userService.getUserByLoginName(loginName);
       if(user==null){
           response.getWriter().write("1");
       }else{
           response.getWriter().write("2");
       }
    }
    //检查手机号
    @RequestMapping("/checkPhone")
    public void checkPhone(String phone1,HttpServletResponse response)throws Exception{
       System.out.println("telnum"+phone1);
        SysUser user=userService.findUserByPhone(phone1);
        if(null==user){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }
    //检查邮箱
    @RequestMapping("/checkEmail")
    public void checkEmail(String email,HttpServletResponse response)throws Exception{
        SysUser user=userService.findUserByEmail(email);
        if(null==user){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }
    //检查企业名称
    @RequestMapping("/checkCompanyname")
    public void checkCompanyname(String companyname,HttpServletResponse response)throws Exception{
      Company company=companyService.findCompanyByCompanyname(companyname);
        if(null==company){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }
    //三级联动
    @RequestMapping("/getCity")
    public @ResponseBody List<City> getCity(Integer parentId){
        parentId=parentId==null?SysConstant.CITY_CHINA_ID:parentId;
       List<City> cityList=cityService.findCityByParentId(parentId);
       return  cityList;
    }
    //存储注册
    @RequestMapping("/register")
    public String register(RegisterVo vo){
       userService.addRegister(vo);
    return "redirect:toLogin";
    }
    @RequestMapping("/sms")
    public String sms(){

        return "login/sms";
    }
    @RequestMapping("/smsControllter")
    public void yanzhengma(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI9OcjAmlmmSMb", "qfNcdE1oPHKyaVbWRqyFd57HDHlebZ");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "\u6c7d\u914d\u8fde");
        request.putQueryParameter("TemplateCode", "SMS_172883701");
        String yzm=new Random().nextInt(899999)+100000+"";
        request.putQueryParameter("TemplateParam", "{'code':"+yzm+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            jedis.boundValueOps(phone).set(yzm);
            jedis.expire(phone,1, TimeUnit.MINUTES);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/checkCode")
    public void checkCode(String code,String phone,HttpServletResponse response)throws Exception{

            String code1=(String) jedis.boundValueOps(phone).get();
             System.out.println("code:"+code);
            System.out.println("code1:"+code1);
            if(code1==null){
                response.getWriter().write("1");
            }
            if(code.equals(code1)){
                response.getWriter().write("2");
            }else{
                response.getWriter().write("3");
            }
    }



}
