package com.eco.domain.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AsScheduleResponseDTO {
	private List<ASListDTO> content; // 현재 페이지의 AS 목록 데이터
	private int totalPages; // 전체 페이지 수
	private long totalElements; // 전체 AS 항목 수
	private int currentPage; // 현재 페이지 번호 (1-based)
	private int pageSize; // 페이지당 항목 수

}
