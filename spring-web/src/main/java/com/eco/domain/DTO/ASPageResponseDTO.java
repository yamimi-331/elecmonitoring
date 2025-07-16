package com.eco.domain.DTO;

import java.util.List;

import com.eco.domain.vo.ASVO;

import lombok.Data;

@Data
public class ASPageResponseDTO {
	private List<ASVO> asList; // 실제 결과 리스트
	private int currentPage; // 현재 페이지
	private int totalPages; // 전체 페이지 수
	private int startPage; // 페이지 네비게이션 시작 번호
	private int endPage; // 페이지 네비게이션 끝 번호
	private boolean hasPrev; // 이전 페이지 그룹 존재 여부
	private boolean hasNext; // 다음 페이지 그룹 존재 여부
}
