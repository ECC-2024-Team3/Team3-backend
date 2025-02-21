package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.EmailDTO;
import io.github.ecc2024team3.oimarket.service.EmailService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;


    // 인증코드 메일 발송
    @PostMapping("/send")
    public ResponseEntity<String> mailSend(@RequestBody EmailDTO emailDto) {
        log.info("EmailController.mailSend(), email: {}", emailDto.getMail());

        try {
            emailService.sendEmail(emailDto.getMail());
            log.info(" 인증 코드 이메일 전송 성공");
            return ResponseEntity.ok("인증코드가 발송되었습니다.");
        } catch (Exception e) {  //  Exception으로 변경
            log.error("메일 전송 실패", e);
            return ResponseEntity.internalServerError().body("메일 전송 실패: " + e.getMessage());
        }
    }

    // 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody EmailDTO emailDto) {
        log.info("EmailController.verify(), email: {}", emailDto.getMail());

        boolean isVerify = emailService.verifyEmailCode(emailDto.getMail(), emailDto.getVerifyCode());
        if (isVerify) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증 실패하셨습니다.");
        }
    }
}
