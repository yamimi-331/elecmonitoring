package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.ReportDTO;
import com.eco.domain.DTO.ReportListResponseDTO;
import com.eco.domain.DTO.ReportStatsDTO;

public interface ReportService {

	// 신고 리스트 전체 조회
//	public List<ReportDTO> getAllReportList();
	public ReportListResponseDTO getAllReportList(int page, int size);

	/* public List<ReportDTO> getLocalReportList(String local); */
	
	public ReportListResponseDTO getLocalReportList(String local, int page, int size);

	// 신고 게시글 상세 조회
	public ReportDTO getDetailReport(int report_cd);

	// 신고 글 등록
	public boolean registerReport(ReportDTO reportDTO);

	// 신고 글 수정
	public boolean modifyReport(ReportDTO reportDTO);

	// 신고 글 삭제
	public boolean removeReport(int report_cd);
	
	// 신고글 지역별로 top 5 뽑기
	public List<ReportStatsDTO> getTop5LocalReportStats();
}
