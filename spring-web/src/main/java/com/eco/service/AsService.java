package com.eco.service;

import java.util.List;

import com.eco.domain.ASVO;

public interface AsService {

	// 기사, 관리자의 AS 리스트 가져오는 함수
	public List<ASVO> getAsList(String userType, int staffCd);
}
