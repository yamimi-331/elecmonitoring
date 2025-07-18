package com.eco.mapper;

import java.util.List;

import com.eco.domain.DTO.FileUploadDTO;

public interface FileMapper {
	// 파일 등록
	public int insertFile(FileUploadDTO fileDTO);

	// 파일 목록 조회
	public List<FileUploadDTO> selectFilesByNoticeCd(int noticeCd);
	// 파일 다운로드를 위한 조회
	public FileUploadDTO selectFileByFileCd(int fileCd);
}
