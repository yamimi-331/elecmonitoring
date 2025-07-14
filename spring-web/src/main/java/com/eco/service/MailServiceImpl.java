package com.eco.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.eco.domain.vo.ASVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl  implements MailService  {
	private final JavaMailSender mailSender;

    // application.properties 혹은 secret.properties에서 읽어옴
    @Value("${spring.mail.username}")
    private String fromEmail;

    // 인증 코드 전송
    @Override
    public void sendAuthCode(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject("노후시설 AS 본인인증 코드 안내");
        message.setText("안녕하세요.\n본인인증 코드입니다: " + authCode);

        mailSender.send(message);
    }

    // 상태 전환 코드 발송
	@Override
	public void sendAsStatus(String to, ASVO vo) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(fromEmail);

		// 상태별 제목 생성
	    String subject = this.generateStatusSubject(vo);
	    message.setSubject(subject);

	    // 본문
		String mailContent = this.generateStatusMessage(vo);
		message.setText(mailContent);
		
		mailSender.send(message);
	}
	
	// 상태별 메일 제목
	private String generateStatusSubject(ASVO vo) {
	    String status = vo.getAs_status();
	    String subject = "노후시설 A/S 신고 안내";

	    switch (status) {
	        case "신고 접수":
	            subject = "[A/S 접수 완료] 신고가 정상 등록되었습니다.";
	            break;
	        case "AS 작업 중":
	            subject = "[A/S 처리중] 신고 건이 현재 처리중입니다.";
	            break;
	        case "작업 완료":
	            subject = "[A/S 완료] 신고 건이 처리 완료되었습니다.";
	            break;
	        case "예약 취소":
	            subject = "[A/S 예약 취소] 예약이 취소되었습니다.";
	            break;
	        case "작업 취소":
	            subject = "[A/S 작업 취소] 작업이 취소되었습니다.";
	            break;
	        default:
	            break;
	    }

	    return subject;
	}
	
	// 메일 본문 내용
	private String generateStatusMessage(ASVO vo) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("안녕하세요.\n");
	    sb.append("노후시설 AS 신고 서비스를 이용해 주셔서 진심으로 감사합니다.\n\n");
	    sb.append("신고 제목: ").append(vo.getAs_title()).append("\n");
	    sb.append("현재 진행 상황: ").append(vo.getAs_status()).append("\n\n");

	    switch (vo.getAs_status()) {
	        case "신고 접수":
	            sb.append("회원님께서 접수하신 A/S 신고가 정상적으로 등록되었습니다.\n");
	            sb.append("담당 직원이 신고 내용을 신속하게 확인하여 조치 예정이며,\n");
	            sb.append("필요 시 추가 연락을 드릴 수 있습니다.\n\n");
	            sb.append("처리 진행 상황은 언제든지 홈페이지의 마이페이지에서 확인하실 수 있습니다.\n");
	            break;

	        case "AS 작업중":
	            sb.append("회원님의 A/S 신고 건이 현재 담당 기사님에 의해 처리 중입니다.\n");
	            sb.append("안전하고 신속한 처리를 위해 최선을 다하고 있으니,\n");
	            sb.append("작업 진행 중 불편사항이 있으시면 언제든지 고객센터로 문의해 주세요.\n\n");
	            sb.append("작업 상태는 실시간으로 업데이트되며, 마이페이지에서도 확인 가능합니다.\n");
	            break;

	        case "작업 완료":
	            sb.append("회원님의 A/S 신고 건이 무사히 처리 완료되었습니다.\n");
	            sb.append("추가적으로 불편한 점이나 문의사항이 있으시면,\n");
	            sb.append("언제든지 고객센터로 연락 주시면 친절히 안내해 드리겠습니다.\n\n");
	            sb.append("소중한 시설물을 안전하게 유지할 수 있도록 앞으로도 최선을 다하겠습니다.\n");
	            break;

	        case "예약 취소":
	            sb.append("회원님께서 예약하신 A/S 일정이 요청에 따라 취소 처리되었습니다.\n");
	            sb.append("불가피한 상황으로 예약을 취소하신 경우,\n");
	            sb.append("언제든지 다시 예약하실 수 있으니 필요하실 때 편하게 신청해주세요.\n\n");
	            sb.append("추가로 궁금한 점이 있으시면 고객센터로 문의 부탁드립니다.\n");
	            break;

	        case "작업 취소":
	            sb.append("진행 중이던 A/S 작업이 부득이한 사정으로 인해 취소 처리되었습니다.\n");
	            sb.append("이용에 불편을 드려 대단히 죄송합니다.\n");
	            sb.append("관련 내용은 담당 직원이 별도로 연락드려 자세히 안내해 드릴 예정입니다.\n\n");
	            sb.append("불편을 최소화할 수 있도록 신속히 조치하겠습니다.\n");
	            break;

	        default:
	            sb.append("현재 상태에 대한 추가 안내가 필요하다면 고객센터로 문의해 주세요.\n");
	            break;
	    }

	    sb.append("\n늘 안전하고 쾌적한 시설 관리 서비스를 제공하기 위해 노력하겠습니다.\n");
	    sb.append("감사합니다.\n\n");
	    sb.append("- 노후시설 AS 신고 서비스 드림 -");

	    return sb.toString();
	}

}
