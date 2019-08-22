package com.xiupeilian.carpart.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//发邮件任务类
public class MailTask implements  Runnable {

    private JavaMailSenderImpl mailSender;
    private SimpleMailMessage message;

    @Override
    public void run() {
        mailSender.send(message);
    }

    public MailTask(JavaMailSenderImpl mailSender, SimpleMailMessage message) {
        this.mailSender = mailSender;
        this.message = message;
    }
}
