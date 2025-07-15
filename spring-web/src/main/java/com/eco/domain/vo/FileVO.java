package com.eco.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

// 업로드된 파일 정보를 담는 VO 객체
@Data
public class FileVO {
    private int file_cd;              // 파일 코드 (PK)
    private String original_name;     // 업로드 시 원래 파일 이름
    private String stored_name;       // 서버에 저장된 파일 이름
    private String file_path;         // 파일이 저장된 전체 경로
    private long file_size;           // 파일 크기 (bytes)
    private LocalDateTime upload_dt;  // 업로드 일시
    private LocalDateTime update_dt;  // 수정 일시
    private String use_yn;            // 사용 여부 (Y/N)
}
