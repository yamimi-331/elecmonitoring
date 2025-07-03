package com.eco.service;

import java.util.List;

import com.eco.domain.NoticeVO;

public interface NoticeService {

	List<NoticeVO> getNoticeList();
	NoticeVO getNoticeDetail(int notice_cd);
	int insertNotice(NoticeVO notice);
	int updateNotice(NoticeVO notice);
	int deleteNotice(int notice_cd);
}
