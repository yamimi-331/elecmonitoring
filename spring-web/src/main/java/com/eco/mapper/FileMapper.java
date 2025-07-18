package com.eco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.FileUploadDTO;

public interface FileMapper {
	// 파일 등록
	public int insertFile(FileUploadDTO fileDTO);

	// 파일 목록 조회
	public List<FileUploadDTO> selectFilesByNoticeCd(int noticeCd);
	// 파일 다운로드를 위한 조회
	public FileUploadDTO selectFileByFileCd(int fileCd);
	
	// 파일 업데이트(제거)
	public int updateFileUseYn(@Param("fileCd") int fileCd, @Param("useYn") String useYn);
	
	// 특정 공지사항과 연결된 모든 파일을 제거
	public int updateFilesUseYnByNoticeCd(@Param("noticeCd") int noticeCd, @Param("useYn") String useYn);
	
}
