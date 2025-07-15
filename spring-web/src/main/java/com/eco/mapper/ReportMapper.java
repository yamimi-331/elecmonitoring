package com.eco.mapper;

import java.util.List;

import com.eco.domain.DTO.ReportDTO;

public interface ReportMapper {

	// 신고 리스트 전체 조회
	public List<ReportDTO> selectAllReport();

}
