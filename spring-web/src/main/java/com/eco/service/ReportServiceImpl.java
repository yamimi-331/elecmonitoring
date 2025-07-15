package com.eco.service;

import org.springframework.stereotype.Service;

import com.eco.mapper.ReportMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
	
	private final ReportMapper reportMapper;
	
	
	
}
