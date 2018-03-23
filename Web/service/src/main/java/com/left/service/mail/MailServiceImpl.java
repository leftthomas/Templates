package com.left.service.mail;

import com.left.common.mail.MailSenderInfo;
import com.left.common.mail.SimpleMailSender;

/**
 * Created by left on 16/1/4.
 */
public class MailServiceImpl implements MailService {

    @Override
    public boolean sendTextMail(String name, String email, String title, String text) {
        // 设置邮件服务器信息
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);

        // 邮箱用户名
        mailInfo.setUserName(name);
        // 邮箱密码
        //mailInfo.setPassword("password");
        // 发件人邮箱
        mailInfo.setFromAddress(email);
        // 收件人邮箱
        mailInfo.setToAddress("xxx@qq.com");
        // 邮件标题
        mailInfo.setSubject(title);
        // 邮件内容
        mailInfo.setContent(text);
        // 发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        // 发送文体格式
        sms.sendTextMail(mailInfo);
        return false;
    }
}
