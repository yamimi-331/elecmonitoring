package com.eco.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl  implements MailService  {
	private final JavaMailSender mailSender;

    // application.properties 혹은 secret.properties에서 읽어옴
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendAuthCode(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject("노후시설 AS 본인인증 코드 안내");
        message.setText("안녕하세요.\n본인인증 코드입니다: " + authCode);

        mailSender.send(message);
    }
}
