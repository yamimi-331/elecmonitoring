package com.eco.domain.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ReportListResponseDTO {
	private List<ReportDTO> list;
	private int totalCount;
}
