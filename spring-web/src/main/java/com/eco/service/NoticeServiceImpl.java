package com.eco.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eco.domain.DTO.FileUploadDTO;
import com.eco.domain.DTO.NoticeDTO;
import com.eco.domain.DTO.NoticePageResponseDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.FileMapper;
import com.eco.mapper.NoticeMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private NoticeMapper noticeMapper;
	private FileMapper fileMapper;   // T_FILE 테이블 관련 DAO/Mapper
	
	@Value("${file.upload-dir}")
    private final String UPLOAD_DIR; // 실제 서버 경로로 변경 필요

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
    
    @Override
    @Transactional // 이 메서드 내의 모든 DB 작업이 하나의 트랜잭션으로 처리됩니다.
    public void registerNoticeWithFiles(NoticeDTO notice, MultipartFile[] files) {
        try {
            // 1. T_NOTICE 테이블에 공지사항 정보 삽입
            // Mybatis Mapper에서 useGeneratedKeys="true" keyProperty="notice_cd" 설정 시,
            // 이 호출 후 notice 객체의 notice_cd 필드에 자동 생성된 PK 값이 채워집니다.
            int noticeResult = noticeMapper.insertNotice(notice);
            if (noticeResult == 0) {
                throw new ServiceException("공지사항 등록에 실패했습니다.");
            }

            // 삽입된 공지사항의 notice_cd 값 가져오기
            int noticeCd = notice.getNotice_cd(); 
            if (noticeCd == 0) { // PK가 제대로 반환되지 않은 경우
                 throw new ServiceException("공지사항 코드 획득에 실패했습니다.");
            }

            // 2. 첨부 파일이 존재하면 T_FILE 테이블에 파일 정보 삽입 및 실제 파일 저장
            if (files != null && files.length > 0) {
                // 파일 업로드 디렉토리가 없으면 생성
            	String actualUploadPath = UPLOAD_DIR.replace("file:///", "");
                File uploadDirFile = new File(actualUploadPath);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs(); // 디렉토리 생성
                }

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalName = file.getOriginalFilename();
                        // 파일명 중복 방지를 위해 UUID를 사용하여 고유한 저장명 생성
                        String storedName = UUID.randomUUID().toString() + "_" + originalName;
                        String filePath = UPLOAD_DIR + storedName; // 실제 저장될 파일의 전체 경로
                        long fileSize = file.getSize();

                        // 실제 파일 저장 (java.nio.file 사용)
                        Path targetPath = Paths.get(filePath);
                        Files.copy(file.getInputStream(), targetPath);

                        // FileDTO 생성 및 값 설정
                        FileUploadDTO fileDTO = new FileUploadDTO();
                        fileDTO.setNotice_cd(noticeCd);         // 획득한 공지사항 코드 설정
                        fileDTO.setOriginal_name(originalName);
                        fileDTO.setStored_name(storedName);
                        fileDTO.setFile_path(filePath);          // 파일 저장 경로 설정
                        fileDTO.setFile_size(fileSize);
                        
                        // T_FILE 테이블에 파일 정보 삽입
                        int fileResult = fileMapper.insertFile(fileDTO);
                        if (fileResult == 0) {
                            // 파일 정보 DB 저장 실패 시, 이미 저장된 물리 파일도 삭제 (선택 사항)
                            Files.deleteIfExists(targetPath); // 롤백 시 물리 파일도 정리
                            throw new ServiceException("파일 정보 DB 저장에 실패했습니다: " + originalName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 예외 발생 시 트랜잭션 롤백
            // @Transactional 어노테이션이 RuntimeException에 대해 기본적으로 롤백을 수행합니다.
            // 따라서 이곳에서는 ServiceException을 throw하여 롤백을 유도합니다.
            throw new ServiceException("공지사항 및 파일 등록 중 오류 발생", e);
        }
    }
}
