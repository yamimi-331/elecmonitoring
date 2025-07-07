package com.eco.mapper;

import java.util.List;

import com.eco.domain.ASVO;

public interface AsMapper {
	// AS 신고내역 전체 조회
	public List<ASVO> selectAsList();
	
}
