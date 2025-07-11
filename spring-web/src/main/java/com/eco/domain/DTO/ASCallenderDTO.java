package com.eco.domain.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ASCallenderDTO {
	// error
	private String error;
	// role
	private String role;
	
	// events
	private List<ASListDTO> events; 
}
