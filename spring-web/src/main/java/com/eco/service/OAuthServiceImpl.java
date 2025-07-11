package com.eco.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eco.domain.vo.UserVO;
import com.eco.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
	// Google OAuth2 Info
	@Value("${google.client.id}")
	private String GOOGLE_CLIENT_ID;

	@Value("${google.client.secret}")
	private String GOOGLE_CLIENT_SECRET;

	@Value("${google.redirect.uri}")
	private String GOOGLE_REDIRECT_URI;

	// Naver OAuth2 Info
	@Value("${naver.client.id}")
	private String NAVER_CLIENT_ID;
	
	@Value("${naver.client.secret}")
	private String NAVER_CLIENT_SECRET;
	
	@Value("${naver.redirect.uri}")
	private String NAVER_REDIRECT_URI;
	
	// Kakao OAuth2 Info
	@Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect.uri}")
    private String KAKAO_REDIRECT_URI;
    
	private final UserService userService; // UserVO 관련 서비스

	// 구글 로그인 URL
	@Override
	public String getGoogleLoginUrl() {
		return "https://accounts.google.com/o/oauth2/v2/auth" + "?scope=email%20profile" + "&access_type=offline"
				+ "&include_granted_scopes=true" + "&response_type=code" + "&client_id=" + GOOGLE_CLIENT_ID
				+ "&redirect_uri=" + GOOGLE_REDIRECT_URI + "&prompt=select_account";
	}
	// 구글 로그인 (없는계정이면 회원가입 자동진행)
	@Override
	public UserVO processGoogleLogin(String code) {
		try {
			// 1. Access Token 요청
			URL url = new URL("https://oauth2.googleapis.com/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			String data = "code=" + code + "&client_id=" + GOOGLE_CLIENT_ID + "&client_secret=" + GOOGLE_CLIENT_SECRET
					+ "&redirect_uri=" + GOOGLE_REDIRECT_URI + "&grant_type=authorization_code";

			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			os.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();
			
			JSONObject json = new JSONObject(sb.toString());
			String accessToken = json.getString("access_token");

			// 2. 사용자 정보 요청
			URL userInfoUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
			HttpURLConnection userConn = (HttpURLConnection) userInfoUrl.openConnection();
			BufferedReader userReader = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
			StringBuilder userSb = new StringBuilder();
			String userLine;
			while ((userLine = userReader.readLine()) != null)
				userSb.append(userLine);
			userReader.close();
			
			JSONObject userInfo = new JSONObject(userSb.toString());
		
			String id = "google_" + userInfo.getString("id");
			String email = userInfo.getString("email");
			String name = userInfo.getString("name");

			// 3. 사용자 DB 처리
			String type = "Google";
			UserVO checkIdvo = new UserVO();
			checkIdvo.setUser_id(id);
			UserVO user = userService.checkId(checkIdvo);
			if (user == null) {
				user = new UserVO();
				user.setUser_id(id);
				user.setUser_pw("");
				user.setUser_nm(name);
				user.setUser_mail(email);
				user.setUser_social(type);
				user.setUser_addr("울산");
				userService.register(user);
			}
			return user;
		} catch (Exception e) {
			throw new ServiceException("사용자 구글 로그인 실패", e);
		}
	}

	@Override
	public String getNaverLoginUrl() {
		String state = UUID.randomUUID().toString();
		try {
			return "https://nid.naver.com/oauth2.0/authorize?response_type=code" + "&client_id=" + NAVER_CLIENT_ID
					+ "&redirect_uri=" + URLEncoder.encode(NAVER_REDIRECT_URI, "UTF-8") + "&state=" + state
					+ "&prompt=login";
		} catch (Exception e) {
			throw new ServiceException("인코딩 실패", e);
		}
	}

	@Override
	public UserVO processNaverLogin(String code, String state) {
		// TODO: 실제 서비스에서는 전달받은 state 값을 세션 등에 저장된 값과 비교해 검증해야 함
		try {
			// 1. Access Token 요청
			String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" + "&client_id="
					+ NAVER_CLIENT_ID + "&client_secret=" + NAVER_CLIENT_SECRET + "&code=" + code + "&state=" + state
					+ "&redirect_uri=" + URLEncoder.encode(NAVER_REDIRECT_URI, "UTF-8");

			URL url = new URL(tokenUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			JSONObject json = new JSONObject(sb.toString());
			String accessToken = json.getString("access_token");

			// 2. 사용자 정보 조회
			URL userInfoUrl = new URL("https://openapi.naver.com/v1/nid/me");
			HttpURLConnection userConn = (HttpURLConnection) userInfoUrl.openConnection();
			userConn.setRequestMethod("GET");
			userConn.setRequestProperty("Authorization", "Bearer " + accessToken);

			BufferedReader userBr = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
			StringBuilder userSb = new StringBuilder();
			String userLine;
			while ((userLine = userBr.readLine()) != null) {
				userSb.append(userLine);
			}
			userBr.close();

			JSONObject userInfoJson = new JSONObject(userSb.toString());
			JSONObject responseJson = userInfoJson.getJSONObject("response");
			
			String id = "naver_" + responseJson.getString("id");
			String email = responseJson.getString("email");
			String name = responseJson.getString("name");

			String type = "Naver";
			UserVO checkIdvo = new UserVO();
			checkIdvo.setUser_id(id);
			UserVO user = userService.checkId(checkIdvo);

			if (user == null) {
				user = new UserVO();
				user.setUser_id(id);
				user.setUser_pw("");
				user.setUser_nm(name);
				user.setUser_social(type);
				user.setUse_yn('Y');
				user.setUser_mail(email);
				user.setUser_addr("울산");
				userService.register(user);
			}
			return user;
		} catch (Exception e) {
			throw new ServiceException("네이버 로그인 실패 ", e);
		}
	}
	
	@Override
	public UserVO getKakaoUserInfo(String code) {
		try {
			// 1) 액세스 토큰 요청
			String tokenUrl = "https://kauth.kakao.com/oauth/token" + "?grant_type=authorization_code" + "&client_id="
					+ KAKAO_CLIENT_ID + "&redirect_uri=" + URLEncoder.encode(KAKAO_REDIRECT_URI, "UTF-8") + "&code="
					+ code;

			URL url = new URL(tokenUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();

			JSONObject tokenJson = new JSONObject(sb.toString());
			String accessToken = tokenJson.getString("access_token");

			// 2) 사용자 정보 조회
			URL userInfoUrl = new URL("https://kapi.kakao.com/v2/user/me");
			HttpURLConnection userConn = (HttpURLConnection) userInfoUrl.openConnection();
			userConn.setRequestMethod("GET");
			userConn.setRequestProperty("Authorization", "Bearer " + accessToken);

			BufferedReader userBr = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
			StringBuilder userSb = new StringBuilder();
			String userLine;
			while ((userLine = userBr.readLine()) != null)
				userSb.append(userLine);
			userBr.close();

			JSONObject userInfoJson = new JSONObject(userSb.toString());
			JSONObject kakaoAccount = userInfoJson.getJSONObject("kakao_account");
			String email = kakaoAccount.getString("email");
			String nickname = kakaoAccount.getJSONObject("profile").getString("nickname");

			// 3) 사용자 VO에 담아서 반환
			UserVO userVO = new UserVO();
			userVO.setUser_id(email);
			userVO.setUser_nm(nickname);
			userVO.setUser_social("KAKAO");

			return userVO;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}