package com.eco.service;

import java.util.List;

import com.eco.domain.ASListDTO;

public interface AsService {

	// 기사, 관리자의 AS 리스트 가져오는 함수
	public List<ASListDTO> getAsList(String userType, int staffCd);
	// 기사 관리자의 상태 업데이트
	public void updateStatus(int as_cd, String as_status);
	// as_cd로 정보 가져오기
	public ASListDTO getAsDetail(int as_cd);
}
