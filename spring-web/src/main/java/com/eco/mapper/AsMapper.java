package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.ASListDTO;

public interface AsMapper {
	// AS 신고내역 전체 조회
	public List<ASListDTO> selectAllAsList();
	// 기사에 해당하는 AS 신고내역 조회
	public List<ASListDTO> selectAsListByStaff(@Param("staffCd") int staffCd);
	// 관리자/기사의 상황 업데이트
	public void updateStatusByCd(@Param("as_cd") int as_cd, @Param("as_status") String as_status);
	// ASCD값으로 항목 상세정보 조회
	public ASListDTO selectAsDetail(int as_cd);
	
}
