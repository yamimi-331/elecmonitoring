package com.eco.service;

import java.util.List;

import com.eco.domain.NoticeVO;

public interface NoticeService {

	public List<NoticeVO> getNoticeList();
	public NoticeVO getNoticeDetail(int notice_cd);
	public int insertNotice(NoticeVO notice);
	public int updateNotice(NoticeVO notice);
	public int deleteNotice(int notice_cd);
}
