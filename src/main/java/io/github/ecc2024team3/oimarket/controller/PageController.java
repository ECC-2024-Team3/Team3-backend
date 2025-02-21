package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.EmailDTO;
import io.github.ecc2024team3.oimarket.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page/email")
public class PageController {
    private final EmailService emailService;

    // 인증코드 메일 발송
    @PostMapping("/send")
    public String mailSend(EmailDTO emailDto){
        log.info("EmailController.mailSend()");
        emailService.sendEmail(emailDto.getMail()); // 예외 처리가 필요 X
        return "이메일이 성공적으로 발송되었습니다.";
    }

    // 인증코드 인증
    @PostMapping("/verify")
    public String verify(EmailDTO emailDto) {
        log.info("EmailController.verify()");
        boolean isVerify = emailService.verifyEmailCode(emailDto.getMail(), emailDto.getVerifyCode());
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}


