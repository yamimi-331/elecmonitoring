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

CREATE TABLE T_AS(
	as_cd int PRIMARY KEY AUTO_INCREMENT,
	staff_cd int NOT NULL,
	user_cd	int NOT NULL,
	user_mail VARCHAR(50),
	as_title VARCHAR(200) NOT NULL,
	as_content TEXT NOT NULL,
	as_date	DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	as_addr	TEXT NOT NULL,
	as_facility	varchar(100) NOT NULL,
	as_status	varchar(100) NOT NULL  DEFAULT '신고 접수',
	use_yn	char(1) NOT NULL  DEFAULT 'Y',
     -- FK 제약조건
    CONSTRAINT fk_as_staff FOREIGN KEY (staff_cd) REFERENCES T_STAFF(staff_cd),
    CONSTRAINT fk_as_user FOREIGN KEY (user_cd) REFERENCES T_USER(user_cd)
);

create table t_staff(			
	staff_cd int not null primary key auto_increment,		
	staff_id varchar(50) not null unique,		
	staff_pw varchar(50) not null,		
	staff_nm varchar(50) not null,		
	staff_addr varchar(100),		
	staff_role varchar(50) not null default 'STAFF',		
	use_yn char(1) not null default 'Y'
);	
	
CREATE TABLE T_USER (
  user_cd INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id VARCHAR(50) NOT NULL UNIQUE,
  user_pw VARCHAR(50) NOT NULL,
  user_nm VARCHAR(50) NOT NULL,
  user_addr VARCHAR(100),
  user_mail VARCHAR(50),
  user_social VARCHAR(20) NOT NULL DEFAULT 'Basic',
  use_yn CHAR(1) NOT NULL DEFAULT 'Y'
);
