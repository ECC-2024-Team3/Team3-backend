package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private static final String senderEmail = "sanbyul1@naver.com";
    private final TemplateEngine templateEngine;

    // 인증 코드 생성 (6자리 랜덤 문자열)
    private String createCode() {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    // 이메일 내용 구성
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createCode();
        log.info("생성된 인증 코드: {}", authCode); // 인증 코드 로그 출력

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("안녕하세요. 인증번호입니다.");
        helper.setFrom(senderEmail);
        helper.setText(setContext(authCode), true); // HTML 형식 적용

        // Redis 에 해당 인증코드 저장 (유효 시간 30분)
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }

    // 인증 코드 이메일 발송
    public void sendEmail(String toEmail) {
        try {
            // 기존 코드가 있으면 삭제
            if (redisUtil.existData(toEmail)) {
                redisUtil.deleteData(toEmail);
            }

            MimeMessage emailForm = createEmailForm(toEmail);
            javaMailSender.send(emailForm);
            log.info("인증 이메일 전송 완료: {}", toEmail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }
    }

    // 인증 코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        String storedCode = redisUtil.getData(email);
        log.info("저장된 인증 코드: {}", storedCode);

        if (storedCode == null) {
            return false;
        }
        return storedCode.equals(code);
    }
}

