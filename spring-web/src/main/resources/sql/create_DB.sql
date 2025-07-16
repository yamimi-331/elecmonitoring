create database elecmonitor;
use elecmonitor;
DROP TABLE T_NOTICE;

CREATE TABLE T_NOTICE (
    notice_cd INT NOT NULL AUTO_INCREMENT COMMENT '공지사항 코드',
    staff_cd INT DEFAULT NULL COMMENT '작성자(직원) 코드',
    title VARCHAR(200) NOT NULL COMMENT '제목',
    content TEXT NOT NULL COMMENT '내용',
    create_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    update_dt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    allow_range VARCHAR(100) NOT NULL DEFAULT 'all' COMMENT '열람 권한 범위',
    use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부',

    PRIMARY KEY (notice_cd),

    -- FK 제약조건은 staff 및 user 테이블이 있을 경우 활성화
    FOREIGN KEY (staff_cd) REFERENCES t_staff(staff_cd)
);
select * from t_notice;

-- 새로 생성
CREATE TABLE T_AS (
  as_cd INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'AS 일정 구분 CD값',
  staff_cd INT COMMENT '직원 구분 CD값',
  user_cd INT COMMENT '사용자 구분 CD값',
  user_mail VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '사용자 메일 - 대소문자 구분',
  as_title VARCHAR(200) NOT NULL COMMENT '문제 종류',
  as_content TEXT NOT NULL COMMENT '상세 정보',
  as_date DATETIME NOT NULL COMMENT '예약 일시',
  as_addr TEXT NOT NULL COMMENT 'AS 대상 시설 위치',
  as_facility VARCHAR(100) NOT NULL COMMENT '시설물 종류',
  as_status VARCHAR(100) NOT NULL DEFAULT '신고 접수' COMMENT '진행 상태',
  guest_mail VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '비회원 로그인 메일 - 대소문자 구분',
  guest_nm VARCHAR(50) COMMENT '비회원 이름',
  use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
  -- FK 제약조건
  CONSTRAINT fk_as_staff FOREIGN KEY (staff_cd) REFERENCES T_STAFF(staff_cd),
  CONSTRAINT fk_as_user FOREIGN KEY (user_cd) REFERENCES T_USER(user_cd)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 새로 생성 (아이디/비번 대소문자 구분)
CREATE TABLE T_STAFF (
  staff_cd INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  staff_id VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL UNIQUE COMMENT '직원 계정 ID - 대소문자 구분',
  staff_pw VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '직원 계정 PW - 대소문자 구분',
  staff_nm VARCHAR(50) NOT NULL COMMENT '직원 이름',
  staff_addr VARCHAR(100) COMMENT '직원 주소',
  staff_role VARCHAR(50) NOT NULL DEFAULT 'STAFF' COMMENT '직원 역할',
  use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '계정 사용 여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
desc T_STAFF;

-- 새로 생성 (대소문자 구분 설정)
CREATE TABLE T_USER (
  user_cd INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 구분 CD값',
  user_id VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL UNIQUE COMMENT '계정 ID - 대소문자 구분',
  user_pw VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '계정 비밀번호 - 대소문자 구분',
  user_nm VARCHAR(50) NOT NULL COMMENT '사용자 이름',
  user_addr VARCHAR(100) COMMENT '사용자 주소',
  user_mail VARCHAR(50) COMMENT '사용자 메일',
  user_social VARCHAR(20) NOT NULL DEFAULT 'Basic' COMMENT '일반, 구글, 네이버, 카카오',
  use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '계정 사용여부(삭제시 N)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
desc t_user;

ALTER TABLE t_user
MODIFY COLUMN user_pw VARCHAR(255);

ALTER TABLE t_staff
MODIFY COLUMN staff_pw VARCHAR(255);
select * from t_user;


CREATE TABLE T_REPORT (
    report_cd INT NOT NULL AUTO_INCREMENT COMMENT '신고 코드',
    staff_cd INT NOT NULL COMMENT '직원 코드값',
    title VARCHAR(100) DEFAULT NULL COMMENT '신고 제목',
    type VARCHAR(50) DEFAULT NULL COMMENT '재해 유형',
    content TEXT DEFAULT NULL COMMENT '신고 내용',
    local VARCHAR(50) DEFAULT NULL COMMENT '발생 지역',
    phone VARCHAR(13) NOT NULL DEFAULT '000-1234-1234' COMMENT '신고자 폰번호',
    report_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '신고 일자',
    update_dt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
    PRIMARY KEY (report_cd),
    FOREIGN KEY (staff_cd) REFERENCES t_staff(staff_cd)  -- ← 실제 참조 테이블명으로 변경 필요
) COMMENT='전기 재해 신고 테이블';

select * from t_report;


-- 전기 파일 업로드 정보 테이블
CREATE TABLE T_FILE (
    file_cd        INT AUTO_INCREMENT PRIMARY KEY COMMENT '파일 코드 (PK)',
    original_name  VARCHAR(255) NOT NULL COMMENT '업로드 시 원래 파일 이름',
    stored_name    VARCHAR(255) NOT NULL COMMENT '서버에 저장된 파일 이름',
    file_path      VARCHAR(500) NOT NULL COMMENT '파일이 저장된 경로 (전체 경로 또는 상대 경로)',
    file_size      BIGINT NOT NULL COMMENT '파일 크기 (bytes)',
    upload_dt      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 일시',
    update_dt      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    use_yn         CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부 (Y/N)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='파일 업로드 테이블';


