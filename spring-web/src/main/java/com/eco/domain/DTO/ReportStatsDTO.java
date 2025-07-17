package com.eco.domain.DTO;

import lombok.Data;

@Data
public class ReportStatsDTO {
	private String local;
	private int count;
	private String riskLevel; // 예: 낮음, 보통, 높음
}
