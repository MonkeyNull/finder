package com.monkey.finder.find.service;

import org.springframework.mail.MailException;

public interface IMailService {
    void sendSimpleMail(String to, String subject, String content) throws MailException;
}
