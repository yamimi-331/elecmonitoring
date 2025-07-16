package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.DTO.InquiryDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.InquiryMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InquiryServiceImpl implements InquiryService{
	private final InquiryMapper inquiryMapper;

	// 문의 게시판 전체 조회
	@Override
	public List<InquiryDTO> getAllInquiry() {
		try {
			List<InquiryDTO> results = inquiryMapper.selectAllInquiry();
			return results;
		} catch (Exception e) {
			throw new ServiceException("신고 글 목록 조회 실패", e);
		}
	}

	// 문의 게시글 상세 조회
	@Override
	public InquiryDTO getDetailInquiry(int inquiry_cd) {
		try {
			InquiryDTO results = inquiryMapper.selectDetailInquiry(inquiry_cd);
			return results;
		} catch (Exception e) {
			throw new ServiceException("신고 글 조회 실패", e);
		}
	}

	// 문의 글 등록
	@Override
	public boolean registerInquiry(InquiryDTO inquiryDTO) {
		try {
			int results = inquiryMapper.insertInquiry(inquiryDTO);
			return results>0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 조회 실패", e);
		}
	}

	// 문의 글 수정
	@Override
	public boolean modifyInquiry(InquiryDTO inquiryDTO) {
		try {
			int results = inquiryMapper.updateInquiry(inquiryDTO);
			return results>0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 수정 실패", e);
		}
	}

	// 문의 글 삭제
	@Override
	public boolean removeInquiry(int inquiry_cd) {
		try {
			int results = inquiryMapper.deleteInquiry(inquiry_cd);
			return results>0;
		} catch (Exception e) {
			throw new ServiceException("신고 글 수정 실패", e);
		}
	}

}
