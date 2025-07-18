package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.NoticePageResponseDTO;
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
	 // 페이징을 위한 새로운 서비스 메서드 구현
    @Override
    public NoticePageResponseDTO getNoticeListPaged(String searchWord, int page, int size) {
        try {
            int offset = page * size; // 0-based 페이지 번호이므로 page * size

            // 1. 검색 조건에 맞는 전체 공지사항의 총 개수를 조회합니다.
            long totalElements = noticeMapper.countNoticeList(searchWord);

            // 2. 현재 페이지에 해당하는 공지사항 목록을 조회합니다.
            // Note: Mapper의 selectNoticeListPaged는 NoticeVO를 반환하도록 되어 있습니다.
            List<NoticeDTO> content = noticeMapper.selectNoticeListPaged(searchWord, offset, size);

            // NoticePageResponseDTO 객체를 생성하고 모든 페이징 정보를 설정합니다.
            NoticePageResponseDTO response = new NoticePageResponseDTO();
            response.setContent(content);
            response.setTotalElements(totalElements);
            // 전체 페이지 수 계산: (총 항목 수 + 페이지 사이즈 - 1) / 페이지 사이즈 (올림)
            response.setTotalPages((int) Math.ceil((double) totalElements / size));
            response.setCurrentPage(page); // 현재 페이지 번호 (0-based)
            response.setPageSize(size);

            return response;
        } catch (Exception e) {
            throw new ServiceException("페이징된 공지사항 목록 조회 실패", e);
        }
    }
}
