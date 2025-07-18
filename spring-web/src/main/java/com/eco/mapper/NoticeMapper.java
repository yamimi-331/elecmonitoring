package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.vo.NoticeVO;

public interface NoticeMapper {

	// 공지사항 목록 조회
	public List<NoticeDTO> selectNoticeList();
	public List<NoticeDTO> selectNoticeSearchList(@Param("search_word") String search_word);
	
	public NoticeVO getNoticeDetail(int notice_cd);
	
	public int insertNotice(NoticeVO notice);
	
	public int updateNotice(NoticeVO notice);
	
	public int deleteNotice(int notice_cd);  // 소프트 삭제

}
