package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.InquiryDTO;

public interface InquiryMapper {
	
	// 문의 게시판 전체 조회
	public List<InquiryDTO> selectAllInquiry();
	// 내가 작성한 게시글 목록 조회
	public List<InquiryDTO> selectPersonalInquiry(@Param("user_cd") int user_cd, @Param("search_word") String search_word);
	// 검색 통한 게시글 목록 조회
	public List<InquiryDTO> selectInquiryBySearch(@Param("search_word") String search_word);
	
	// 문의 게시글 상세 조회
	public InquiryDTO selectDetailInquiry(@Param("inquiry_cd") int inquiry_cd);
	
	// 문의 글 등록
	public int insertInquiry(InquiryDTO inquiryDTO);
	
	// 문의 글 수정
	public int updateInquiry(InquiryDTO inquiryDTO);
	
	// 문의 글 삭제
	public int deleteInquiry(@Param("inquiry_cd") int inquiry_cd);

}
