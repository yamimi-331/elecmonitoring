package com.eco.domain.DTO;

import lombok.Data;

@Data
public class FileUploadDTO {
	private int notice_cd;
	private String original_name;	// 업로드 시 원래 파일 이름
	private String stored_name; 	// 서버에 저장된 파일 이름
	private String file_path;		// 파일이 저장된 전체 경로
	private long file_size;			// 파일 크기 (bytes)
}
