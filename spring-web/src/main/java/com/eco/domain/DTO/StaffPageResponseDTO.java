package com.eco.domain.DTO;

import java.util.List;

import com.eco.domain.vo.StaffVO;

import lombok.Data;

@Data
public class StaffPageResponseDTO {
	private List<StaffVO> content; // 현재 페이지의 StaffVO 목록
	private int totalPages; // 전체 페이지 수
	private long totalElements; // 전체 항목 수
	private int currentPage; // 현재 페이지 번호 (0-based)
	private int pageSize; // 페이지당 항목 수
}
