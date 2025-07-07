package com.eco.mapper;

import java.util.List;

import com.eco.domain.ASVO;

public interface AsMapper {
	// AS 신고내역 전체 조회
	public List<ASVO> selectAllAsList();
	// 기사에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByStaff(int staffCd);
	
}
