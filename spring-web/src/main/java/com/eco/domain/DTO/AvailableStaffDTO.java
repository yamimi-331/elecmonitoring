package com.eco.domain.DTO;

import lombok.Data;

@Data
public class AvailableStaffDTO {
	private int staff_cd;
	private String staff_nm;
	private String staff_addr;
	private String slot_time;  
    private Integer as_cd;   
	private String as_date;
	private String as_addr;
}

