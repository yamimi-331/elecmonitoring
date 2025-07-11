package com.eco.service;

import com.eco.domain.vo.UserVO;

public interface OAuthService {
	// Google OAuth
	public String getGoogleLoginUrl();

	public UserVO processGoogleLogin(String code);

	// Naver OAuth
	public String getNaverLoginUrl();

	public UserVO processNaverLogin(String code, String state);
	
	// Kakao OAuth
	public UserVO getKakaoUserInfo(String code);
}
