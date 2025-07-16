package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.InquiryDTO;

public interface InquiryService {

	// 문의 게시판 전체 조회
	public List<InquiryDTO> getAllInquiry();
	
	// 문의 게시글 상세 조회
	public InquiryDTO getDetailInquiry(int inquiry_cd);
	
	// 문의 글 등록
	public boolean registerInquiry(InquiryDTO inquiryDTO);
	
	// 문의 글 수정
	public boolean modifyInquiry(InquiryDTO inquiryDTO);
	
	// 문의 글 삭제
	public boolean removeInquiry(int inquiry_cd);
}
