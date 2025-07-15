package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.ReportDTO;

public interface ReportService {

	// 신고 리스트 전체 조회
	public List<ReportDTO> getAllReportList();

}
