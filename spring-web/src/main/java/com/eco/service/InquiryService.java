package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.InquiryDTO;

public interface InquiryService {

	// 문의 게시판 전체 조회
	public List<InquiryDTO> getAllInquiry();
	
	// 문의 게시글 상세 조회
	public InquiryDTO getDetailInquiry(int inquiry_cd);
}
