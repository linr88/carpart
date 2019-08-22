package com.xiupeilian.carpart.controller;


import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.task.MailTask;
import com.xiupeilian.carpart.util.SHA1Util;
import com.xiupeilian.carpart.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private ThreadPoolTaskExecutor executor;
   @RequestMapping("/toLogin")
    public String toLogin(){
        return "login/login";
    }
    @RequestMapping("/login")
    public void login(LoginVo vo, HttpServletRequest request, HttpServletResponse response) throws  Exception{

       String code=(String) request.getSession().getAttribute("validate");
       if(vo.getValidate().toUpperCase().equals(code.toUpperCase())){
           vo.setPassword(SHA1Util.encode(vo.getPassword()));
           SysUser user=userService.findUserByLoginNameAndPassword(vo);
           if(user==null){
               response.getWriter().write("2");
           }else{
               request.getSession().setAttribute("user",user);
               response.getWriter().write("3");
           }
       }else{
           response.getWriter().write("1");
       }

    }
    @RequestMapping("/noauth")
    public String noauth(){
       return "exception/noauth";
    }
    @RequestMapping("/forgetPassword")
    public String forgetPassword(){
       System.out.println(212121212);
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
       }
    }

}
