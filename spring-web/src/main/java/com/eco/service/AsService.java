package com.eco.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;

public interface AsService {

	// 지역 일자 기준 전 직원 스케줄 조회
	public List<String> getFullyBookedSlots(LocalDate date, String region);

	// 기사, 관리자의 AS 리스트 가져오는 함수
	public List<ASVO> getAsList(String userType, int staffCd);

	// 사용자의의 AS 리스트 가져오는 함수
	public List<ASVO> getUserAsList(int user_cd);

	// 일반 회원의 AS 신고
	public boolean registerAsByCommon(ASVO asvo);

	// AS 신고 수정 화면
	public ASVO readAsDetailByUser(int as_cd);

	// AS 신고 수정
	public boolean editAsListByCommon(ASVO asvo);

	// AS 신고 삭제
	public boolean cancleAsListByCommon(int as_cd);

	// 기사, 관리자의 AS 리스트 가져오는 함수
	public List<ASListDTO> getAsDtoList(String userType, int staffCd);

	// 기사 관리자의 상태 업데이트
	public void updateStatus(int as_cd, String as_status);

	// as_cd로 정보 가져오기
	public ASListDTO getAsTask(int as_cd);

	public List<ASListDTO> getScheduleByUserAndDate(@Param("user_cd") int user_cd, @Param("date") LocalDate date);
	// 직원의 모든 스케쥴 조회
	public  List<ASListDTO> getScheduleByStaffAndDate(@Param("staff_cd") int staff_cd, @Param("date") LocalDate date);
	// 관리자의 모든 스케쥴 조회
	public List<ASListDTO> getScheduleByDate(LocalDate localDate);
	
}
