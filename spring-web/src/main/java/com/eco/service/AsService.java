package com.eco.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.ASListDTO;
import com.eco.domain.vo.ASVO;
import com.eco.domain.vo.UserVO;

public interface AsService {

	// 지역 일자 기준 전 직원 스케줄 조회
	public List<String> getFullyBookedSlots(LocalDate date, String region);
	
	// 사용자의의 AS 리스트 가져오는 함수
	public List<ASVO> getUserAsList(int user_cd);

	// 일반 회원의 AS 신고
	public boolean registerAsByCommon(ASVO asvo);

	// AS 신고 수정 화면에 해당 신고 정보 출력
	public ASVO readAsDetailByUser(int as_cd);

	// AS 신고 수정
	public boolean editAsListByCommon(ASVO asvo);

	// AS 신고 삭제
	public boolean cancleAsListByCommon(int as_cd);

	// 기사 관리자의 상태 업데이트
	public void updateStatus(@Param("as_cd") int as_cd,@Param("as_status") String as_status);

	// 항목 상세 조회
	public ASListDTO getAsTask(int as_cd);

	// 직원의 모든 스케쥴 조회
	public  List<ASListDTO> getScheduleByStaffAndDate(LocalDate start, LocalDate end, String staffInfo);
	
	// 관리자의 모든 스케쥴 조회
	public List<ASListDTO> getScheduleByPeriodAndStaff(LocalDate start, LocalDate end, String staffInfo);
	
	// 회원 탈퇴시 미래 예약건 취소
	public boolean cancleAsListBydeleteUser(UserVO userVO);
	
	// 일자순으로 정렬
    public List<ASVO> getUserAsListOrderByAsDate(int user_cd);
}
