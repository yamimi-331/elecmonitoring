package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.ReportDTO;

public interface ReportService {

	// 신고 리스트 전체 조회
	public List<ReportDTO> getAllReportList();
	
	// 신고 게시글 상세 조회
	public ReportDTO getDetailReport(int report_cd);
	
	// 신고 글 등록
	public int registerReport(ReportDTO reportDTO);

}
