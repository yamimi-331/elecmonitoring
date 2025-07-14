package com.eco.service;

import com.eco.domain.vo.ASVO;

public interface MailService {
	// 인증 코드 전송
	public void sendAuthCode(String to, String authCode);
	
	// 상태 전환 코드 발송
	public void sendAsStatus(String to, ASVO vo);
}
