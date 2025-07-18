package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.NoticeMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private NoticeMapper noticeMapper;

	// 공지사항 목록 조회
	@Override
	public List<NoticeDTO> getNoticeList() {
		try {
			return noticeMapper.selectNoticeList();
		} catch (Exception e) {
			throw new ServiceException("공지사항 리스트 조회 실패", e);
		}
	}
	@Override
	public List<NoticeDTO> getNoticeSearchList(String search_word) {
		try {
			return noticeMapper.selectNoticeSearchList(search_word);
		} catch (Exception e) {
			throw new ServiceException("공지사항 리스트 조회 실패", e);
		}
	}

	// 공지사항 상세 조회
	@Override
	public NoticeDTO getNoticeDetail(int notice_cd) {
		try {
			return noticeMapper.selectDetailNotice(notice_cd);
		} catch (Exception e) {
			throw new ServiceException("공지사항 상세 조회 실패", e);
		}	
	}

	// 공지사항 등록
	@Override
	public boolean registerNotice(NoticeDTO notice) {
		try {
			int result = noticeMapper.insertNotice(notice);
			return result>0;
		} catch (Exception e) {
			throw new ServiceException("공지사항 등록 실패", e);
		}
	}

	// 공지사항 수정
	@Override
	public boolean modifyNotice(NoticeDTO notice) {
		try {
			int result = noticeMapper.updateNotice(notice);
			return result>0;
		} catch (Exception e) {
			throw new ServiceException("공지사항 등록 실패", e);
		}
	}

	// 공지사항 삭제
	@Override
	public boolean removeNotice(int notice_cd) {
		try {
			int result = noticeMapper.deleteNotice(notice_cd);
			return result>0;
		} catch (Exception e) {
			throw new ServiceException("공지사항 등록 실패", e);
		}
	}

}
