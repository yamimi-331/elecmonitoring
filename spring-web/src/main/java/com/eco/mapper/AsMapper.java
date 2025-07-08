package com.eco.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;
import com.eco.domain.DTO.AvailableStaffDTO;

public interface AsMapper {
	// AS 신고내역 전체 조회
	public List<ASVO> selectAllAsList();

	// 기사에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByStaff(int staffCd);

	// 기사에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByUser(int user_cd);

	// 일반 회원의 AS 신고
	public int insertAsListByCommon(ASVO asvo);
	// 기사 조회 배정
	public Integer selectAsStaff(@Param("date") String date, @Param("addr") String addr, @Param("time") String time);
	public int updateMatchStaff(@Param("staff_cd") int staffCd, @Param("as_cd") int asCd);
	// AS 신고 수정 화면
	public ASVO selectAsDetailByCommon(int as_cd);

	// AS 신고 수정
	public int updateAsListByCommon(ASVO asvo);

	// AS 신고 삭제
	public int deleteAsListByCommon(int as_cd);
	
	// AS 신고내역 전체 조회
	public List<ASListDTO> selectAllAsDtoList();

	// 기사에 해당하는 AS 신고내역 조회
	public List<ASListDTO> selectAsDtoListByStaff(@Param("staffCd") int staffCd);

	// 관리자/기사의 상황 업데이트
	public void updateStatusByCd(@Param("as_cd") int as_cd, @Param("as_status") String as_status);

	// ASCD값으로 항목 상세정보 조회
	public ASListDTO selectAsTask(int as_cd);


	public List<ASListDTO> selectScheduleByUserAndDate(@Param("user_cd") int user_cd, @Param("date") LocalDate date);
	// 직원의 모든 스케쥴 조회
	public List<ASListDTO> selectScheduleByStaffAndDate(@Param("staff_cd") int staff_cd, @Param("date") LocalDate date);
	// 관리자의 모든 스케쥴 조회
	public List<ASListDTO> selectScheduleByDate(@Param("date") LocalDate localDate);
	
	public List<AvailableStaffDTO> selectAllAsListByRegion(@Param("date") LocalDate localDate, @Param("region") String region);

}
