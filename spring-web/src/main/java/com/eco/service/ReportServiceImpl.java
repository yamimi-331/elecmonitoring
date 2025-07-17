package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.DTO.ReportDTO;
import com.eco.domain.DTO.ReportListResponseDTO;
import com.eco.domain.DTO.ReportStatsDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.ReportMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final ReportMapper reportMapper;

	// 신고 리스트 전체 조회
	@Override
	public ReportListResponseDTO getAllReportList(int page, int size) {
		try {
			// DB에서 신고 글 목록 조회
			int offset = (page - 1) * size;
			List<ReportDTO> results = reportMapper.selectAllReport(size, offset);
			int totalCount = reportMapper.selectReportCount("");

			ReportListResponseDTO dto = new ReportListResponseDTO();

			dto.setList(results);
			dto.setTotalCount(totalCount);

			return dto;
		} catch (Exception e) {
			throw new ServiceException("신고 글 목록 조회 실패", e);
		}
	}

	@Override
	public ReportListResponseDTO getLocalReportList(String local, int page, int size) {
		try {
			// DB에서 신고 글 목록 조회
			int offset = (page - 1) * size;
			int totalCount = reportMapper.selectReportCount(local);
			List<ReportDTO> results = reportMapper.selectLocalReportWidthPaging(local, size, offset);

			ReportListResponseDTO dto = new ReportListResponseDTO();
			dto.setList(results);
			dto.setTotalCount(totalCount);

			return dto;

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

	// 신고글 지역별로 top 5 뽑기
	@Override
	public List<ReportStatsDTO> getTop3LocalReportStats() {
		try {
			List<ReportStatsDTO> list = reportMapper.selectTop3LocalReportStats();

			// 위험도 계산 예시
			for (ReportStatsDTO dto : list) {
				int count = dto.getCount();
				if (count >= 20) {
					dto.setRiskLevel("높음");
				} else if (count >= 10) {
					dto.setRiskLevel("보통");
				} else {
					dto.setRiskLevel("낮음");
				}
			}

			return list;
		} catch (Exception e) {
			throw new ServiceException("신고글 조회 실패", e);
		}
	}

}
