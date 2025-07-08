package com.eco.domain.DTO;

import java.util.List;


import lombok.Data;

@Data
public class AvailableStaffDTO {
	private int staff_cd;
	private String staff_nm;
	private List<String> availableSlots;
	private int workCnt;
}
