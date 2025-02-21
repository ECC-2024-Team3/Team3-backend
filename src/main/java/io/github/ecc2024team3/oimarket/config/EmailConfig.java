package io.github.ecc2024team3.oimarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:true}") // 기본값 true
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") // 기본값 true
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required:false}") // 기본값 false
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}") // 기본값 5000ms
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout:5000}") // 기본값 5000ms
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}") // 기본값 5000ms
    private int writeTimeout;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", String.valueOf(auth));  // ✅ boolean → String 변환
        properties.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnable)); // ✅ boolean → String 변환
        properties.put("mail.smtp.starttls.required", String.valueOf(starttlsRequired)); // ✅ boolean → String 변환
        properties.put("mail.smtp.connectiontimeout", String.valueOf(connectionTimeout)); // ✅ int → String 변환
        properties.put("mail.smtp.timeout", String.valueOf(timeout)); // ✅ int → String 변환
        properties.put("mail.smtp.writetimeout", String.valueOf(writeTimeout)); // ✅ int → String 변환

        return properties;
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // ✅ `resources/templates/` 경로 설정
        templateResolver.setSuffix(".html"); // ✅ `.html` 확장자 사용
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
