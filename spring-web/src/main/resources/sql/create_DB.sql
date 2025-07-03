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
