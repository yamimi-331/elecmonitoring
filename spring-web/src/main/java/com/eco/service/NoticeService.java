package com.eco.service;

import java.util.List;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.vo.NoticeVO;

public interface NoticeService {

	// 공지사항 목록 조회
	public List<NoticeDTO> getNoticeList();
	public List<NoticeDTO> getNoticeSearchList(String search_word);
	
	public NoticeVO getNoticeDetail(int notice_cd);
	public int insertNotice(NoticeVO notice);
	public int updateNotice(NoticeVO notice);
	public int deleteNotice(int notice_cd);
}
