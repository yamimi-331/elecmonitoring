package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.NoticePageResponseDTO;

public interface NoticeService {

	// 공지사항 목록 조회
	public List<NoticeDTO> getNoticeList();
	public List<NoticeDTO> getNoticeSearchList(String search_word);
	
	// 공지사항 상세 조회
	public NoticeDTO getNoticeDetail(int notice_cd);
	
	// 공지사항 등록
	public boolean registerNotice(NoticeDTO notice);
	
	// 공지사항 수정
	public boolean modifyNotice(NoticeDTO notice);
	
	// 공지사항 삭제
	public boolean removeNotice(int notice_cd);
	

    // 페이징을 위한 새로운 메서드
    public NoticePageResponseDTO getNoticeListPaged(String searchWord, int page, int size);

}
