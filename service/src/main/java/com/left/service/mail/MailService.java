package com.left.service.mail;

/**
 * Created by left on 16/1/4.
 */
public interface MailService {

    /**
     * 发送纯文本格式邮件给开发人员
     *
     * @param name
     * @param email
     * @param title
     * @param text
     * @return
     */
    boolean sendTextMail(String name, String email, String title, String text);
}
