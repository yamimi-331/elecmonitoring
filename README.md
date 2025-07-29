
# 전기재해 모니터링 및 노후시설 A/S 신고 관리 시스템 ElecMonitoring

## 💡 프로젝트 개요

'전기재해 모니터링 및 노후시설 A/S 신고 관리 시스템(ElecMonitoring)'은 전기 재해를 예측하고 시각화하여 사용자 경각심을 높이고, 노후 전기시설의 A/S 신청부터 처리까지의 전 과정을 효율적으로 관리하기 위해 개발된 웹 기반 시스템입니다. 일반 사용자, 현장 직원, 관리자가 각자의 권한에 따라 시설 상태 조회, A/S 신고 및 처리, 직원 관리 등을 수행할 수 있도록 설계되었습니다.

## 🛠️ 개발 환경 및 기술 스택

- 개발 기간: 2025년 7월 02일 \~ 2025년 7월 28일
- 개발자: 신혁주(팀장), 박정현
- 프레임워크: Spring Framework (Spring MVC), FastAPI
- IDE: STS3 (Spring Tool Suite 3)
- 서버: Apache Tomcat 9 (Spring Backend), Uvicorn (FastAPI)
- 데이터베이스: MySQL
- 아키텍처: MVC (Spring) + RESTful API (Spring ↔ FastAPI) + Machine Learning (FastAPI)
- 예측 모델: Python (Prophet)

## 📁 프로젝트 구조

프로젝트는 효율적인 관리와 역할 분담을 위해 다음과 같이 모듈화하여 구성되었습니다.

```
📁elecMonitorAS/
├── 📁fastapi_ml/                # 예측 모델 및 API 서버 (FastAPI)
│   ├── 📁data/                  # 예측용 데이터 저장 폴더
│   ├── 📁models/                # 학습된 모델 파일 저장소
│   ├── 🐍main.py                # FastAPI 진입점
│   ├── 🐍train.py               # 예측 모델 학습 스크립트
│   ├── 🐍prediction.py          # 예측 로직 처리
│   ├── 🐍firereason.py          # 전기 화재 원인 JSON 반환
│   ├── 🐍shockreason.py         # 감전 사고 원인 JSON 반환
│   ├── 🐍elecrate.py            # 전체 화재 중 전기 화재 비율 JSON 반환
│   ├── 🐍list.py                # 전기 재해 현황 목록 JSON 반환
│   └── 📑requirements.txt       # FastAPI 의존성 목록
├── 📁spring-web/                # Spring MVC 기반 웹 백엔드
│   └── 📁src/
│       └── 📁main/
│           ├── 📁java/com/eco/
│           │   ├── 📁config/mail/           # 이메일 설정 클래스
│           │   │   └── 📄MailConfig.java
│           │   ├── 📁controller/            # 웹 컨트롤러
│           │   ├── 📁domain/                # VO/DTO 클래스
│           │   ├── 📁exception/             # 예외 처리
│           │   ├── 📁mapper/                # MyBatis 매퍼 인터페이스
│           │   └── 📁service/               # 서비스 로직
│           ├── 📁resources/
│           │   ├── 📁com/eco/mapper/        # MyBatis XML 매퍼
│           │   ├── 📁sql/
│           │   │   ├── 📄create_DB.sql      # DB 생성 스크립트
│           │   │   └── 📄insert_staff.sql   # 더미 직원 데이터 삽입
│           │   ├── 📄log4j.xml
│           │   └── 📄log4jdbc.log4j2.properties
│           └── 📁webapp/                    # JSP/정적 자원
├── 📄.gitignore                 # Git 무시 파일 설정
└── 📄README.md                 # 프로젝트 설명 파일
```

## ✨ 주요 기능

**전기재해 예측 (FastAPI)**

* **예측 기능**:

  * 전기 재해 피해 건수, 피해액 등을 ML 모델로 예측
  * 예측 결과를 JSON으로 반환 → Spring에서 시각화
    
* **시각화 구성**:

  * 예측 결과를 Chart.js 기반 그래프로 렌더링하여 사용자에게 직관적으로 제공

**노후시설 A/S 접수 및 처리 (Spring MVC)**

* **A/S 신고**:

  * 사용자가 Kakao 주소 API 기반 위치 입력 → A/S 예약 및 접수

* **처리 프로세스**:

  * 접수 → 담당자 배정 → 일정 관리 → 처리 상태 업데이트 → 사용자 메일 발송

* **캘린더 통합**:

  * FullCalendar.js를 이용한 직원별, 전체 일정 조회
  
**권한 및 계정 관리**

* 관리자, 직원, 사용자 각자의 권한에 따라 기능 차별 제공
* 관리자 전용 기능: 직원 계정 관리, 전체 신고 내역 조회 및 처리 상태 변경

## 🔁 시스템 플로우

### 🛠 A/S 처리 프로세스

```
[1] 사용자 A/S 신청
    ↓
[2] DB 저장 (Spring → MyBatis → MySQL)
    ↓
[3] 직원 캘린더에서 할당 및 일정 확인
    ↓
[4] 처리 상태 업데이트 (진행 중/완료)
    ↓
[5] 사용자에게 상태 메일 발송
```

### 🛠 A/S 처리 프로세스

```
[1] 사용자 요청 (지역/모델 선택)
    ↓
[2] JavaScript → FastAPI 호출 (AJAX)
    ↓
[3] Python 예측 → JSON 반환
    ↓
[4] Spring Controller 처리
    ↓
[5] Chart.js로 시각화
```

---

## 🚀 프로젝트 실행 방법

**1.프로젝트 클론:**

```bash
git clone https://github.com/yamimi-331/elecmonitoring.git
```

**2. MySQL 초기화:**

 - MySQL 데이터베이스를 생성합니다.
 - 제공된 SQL 파일을 사용하여 필요한 테이블을 생성하고 직원 계정을 생성합니다.

```bash
mysql -u [USER] -p [DB_NAME] < mysql-query/create_DB.sql
mysql -u [USER] -p [DB_NAME] < mysql-query/insert_staff.sql

```

**2. FastAPI 서버 실행**

 - fastapi-ml 디렉토리로 이동합니다.
 - 필요한 Python 의존성을 설치합니다.
 - Uvicorn을 사용하여 FastAPI 애플리케이션을 실행합니다.

```bash
cd fastapi-ml
pip install -r requirements.txt
uvicorn main:app --reload
```

**3. Spring 서버 실행**

 - spring-backend/GasDashboard 프로젝트를 IDE (STS/IntelliJ)로 임포트합니다.
 - src/main/webapp/WEB-INF/spring/root-context.xml 파일에 데이터베이스 연결 정보를 설정합니다.
 - Maven 의존성 설치: 프로젝트 우클릭 -> Maven -> Update Project... 를 통해 필요한 의존성을 다운로드합니다.
 - Apache Tomcat 9 서버를 IDE에 연동하고, 프로젝트를 서버에 추가합니다.
 - Tomcat 서버를 시작하여 웹 애플리케이션을 실행합니다.

## **🚀 프로젝트 시연 영상**

https://github.com/user-attachments/assets/fa8a0f17-5f94-4561-a112-a69b4a77b022

[데모 영상 다운로드](https://raw.githubusercontent.com/yamimi-331/elecmonitoring/main/spring-web/src/main/resources/showFile/demo.mp4)
