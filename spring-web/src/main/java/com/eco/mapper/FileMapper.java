package com.eco.mapper;

import com.eco.domain.DTO.FileUploadDTO;

public interface FileMapper {
	// 파일 등록
	public int insertFile(FileUploadDTO fileDTO);

}
