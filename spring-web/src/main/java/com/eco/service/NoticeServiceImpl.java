package com.eco.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.domain.NoticeVO;
import com.eco.mapper.NoticeMapper;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeMapper noticeMapper;
	
	@Override
	public List<NoticeVO> getNoticeList(){
		return noticeMapper.getNoticeList();
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
