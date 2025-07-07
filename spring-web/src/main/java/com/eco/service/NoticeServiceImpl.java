package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.NoticeVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.NoticeMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private NoticeMapper noticeMapper;

	@Override
	public List<NoticeVO> getNoticeList(String userType) {
		try {
			return noticeMapper.getNoticeList();
		} catch (Exception e) {
			throw new ServiceException("공지사항 리스트 조회 실패", e);
		}
	}

	@Override
	public NoticeVO getNoticeDetail(int notice_cd) {
		return noticeMapper.getNoticeDetail(notice_cd);
	}

	@Override
	public int insertNotice(NoticeVO notice) {
		return noticeMapper.insertNotice(notice);
	}

	@Override
	public int updateNotice(NoticeVO notice) {
		return noticeMapper.updateNotice(notice);
	}

	@Override
	public int deleteNotice(int notice_cd) {
		return noticeMapper.deleteNotice(notice_cd);
	}

}
