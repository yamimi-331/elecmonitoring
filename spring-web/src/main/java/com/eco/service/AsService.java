package com.eco.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;

public interface AsService {

	// 기사, 관리자의 AS 리스트 가져오는 함수
	public List<ASListDTO> getAsList(String userType, int staffCd);
	// 기사 관리자의 상태 업데이트
	public void updateStatus(int as_cd, String as_status);
	// as_cd로 정보 가져오기
	public ASListDTO getAsDetail(int as_cd);

	public List<ASVO> getScheduleByUserAndDate(@Param("user_cd") int user_cd, @Param("date") LocalDate  date);

	public List<ASVO> getScheduleByStaffAndDate(@Param("staff_cd") int staff_cd, @Param("date") LocalDate  date);

}
