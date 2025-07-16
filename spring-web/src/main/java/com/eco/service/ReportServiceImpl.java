package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.DTO.ReportDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.ReportMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final ReportMapper reportMapper;

	// 신고 리스트 전체 조회
	@Override
	public List<ReportDTO> getAllReportList() {
		try {
			// DB에서 신고 글 목록 조회
			List<ReportDTO> results = reportMapper.selectAllReport();
			return results;
		} catch (Exception e) {
			throw new ServiceException("신고 글 목록 조회 실패", e);
		}
	}
	@Override
	public List<ReportDTO> getLocalReportList(String local) {
		try {
			// DB에서 신고 글 목록 조회
			List<ReportDTO> results = reportMapper.selectLocalReport(local);
			return results;
		} catch (Exception e) {
			throw new ServiceException("신고 글 목록 지역별 조회 실패", e);
		}
	}

	// 신고 게시글 상세 조회
	@Override
	public ReportDTO getDetailReport(int report_cd) {
		try {
			// DB에서 신고 글 목록 조회
			ReportDTO results = reportMapper.selectDetailReport(report_cd);
			return results;
		} catch (Exception e) {
			throw new ServiceException("신고 글 조회 실패", e);
		}
	}

	// 신고 글 등록
	@Override
	public boolean registerReport(ReportDTO reportDTO) {
		try {
			// DB에서 신고 글 목록 조회
			int results = reportMapper.insertReport(reportDTO);
			return results > 0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 등록 실패", e);
		}
	}

	// 신고 글 수정
	@Override
	public boolean modifyReport(ReportDTO reportDTO) {
		try {
			// DB에서 신고 글 목록 조회
			int results = reportMapper.updateReport(reportDTO);
			return results > 0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 수정 실패", e);
		}
	}

	// 신고 글 삭제
	@Override
	public boolean removeReport(int report_cd) {
		try {
			// DB에서 신고 글 목록 조회
			int results = reportMapper.deleteReport(report_cd);
			return results > 0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 삭제 실패", e);
		}
	}

}
