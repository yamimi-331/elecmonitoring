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

}
