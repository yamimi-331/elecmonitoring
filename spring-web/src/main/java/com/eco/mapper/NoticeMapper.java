package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.NoticeDTO;

public interface NoticeMapper {

	// 공지사항 목록 조회
	public List<NoticeDTO> selectNoticeList();
	public List<NoticeDTO> selectNoticeSearchList(@Param("search_word") String search_word);
	
	// 공지사항 상세 조회
	public NoticeDTO selectDetailNotice(@Param("notice_cd") int notice_cd);
	
	// 공지사항 등록
	public int insertNotice(NoticeDTO notice);
	
	// 공지사항 수정
	public int updateNotice(NoticeDTO notice);
	
	// 공지사항 삭제
	public int deleteNotice(@Param("notice_cd") int notice_cd);  // 소프트 삭제
	
	// 공지사항 목록 조회 (페이징)
	public List<NoticeDTO> selectNoticeListPaged(@Param("searchWord") String searchWord,@Param("offset") int offset, @Param("size") int size);
	// 공지사항 갯수 카운팅 
	public long countNoticeList(@Param("searchWord") String searchWord);
}
