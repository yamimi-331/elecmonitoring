create database elecmonitor;
use elecmonitor;

CREATE TABLE T_NOTICE (
    notice_cd INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
	create_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_dt DATETIME,
    user_nm VARCHAR(100) NOT NULL,
    use_yn CHAR(1) NOT NULL DEFAULT 'Y'
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