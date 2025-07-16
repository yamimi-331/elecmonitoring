package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.ReportDTO;

public interface ReportMapper {

	// 신고 리스트 전체 조회
	public List<ReportDTO> selectAllReport();
	public List<ReportDTO> selectLocalReport(@Param("local") String local);
	
	// 신고 게시글 상세 조회
	public ReportDTO selectDetailReport(@Param("report_cd") int report_cd);
	
	// 신고 글 등록
	public int insertReport(ReportDTO reportDTO);
	
	// 신고 글 수정
	public int updateReport(ReportDTO reportDTO);

	// 신고 글 삭제
	public int deleteReport(@Param("report_cd") int report_cd);
}
