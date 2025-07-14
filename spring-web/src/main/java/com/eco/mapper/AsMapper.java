package com.eco.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eco.domain.DTO.ASListDTO;
import com.eco.domain.DTO.AvailableStaffDTO;
import com.eco.domain.DTO.GuestDTO;
import com.eco.domain.vo.ASVO;
import com.eco.domain.vo.UserVO;

public interface AsMapper {
	// AS 신고내역 전체 조회
	public List<ASVO> selectAllAsList();

	// 기사에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByStaff(int staffCd);

	// 사용자에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByUser(int user_cd);

	// 일반 회원의 AS 신고
	public int insertAsListByCommon(ASVO asvo);
	
	// 기사 조회 및 배정
	public Integer selectAsStaff(@Param("date") String date, @Param("addr") String addr, @Param("time") String time);
	public int updateMatchStaff(@Param("staff_cd") int staffCd, @Param("as_cd") int asCd);
	
	// AS 신고 수정 화면에 해당 신고 정보 출력
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

	// 직원의 모든 스케쥴 조회
	public List<ASListDTO> selectScheduleByStaffAndDate(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("staffInfo") String staffInfo);
	
	// 관리자의 모든 스케쥴 조회
	public List<ASListDTO> selectScheduleByDate(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("staffInfo") String staffInfo);
	
	// 지역 일자 기준 전 직원 스케줄 조회
	public List<AvailableStaffDTO> selectAllAsListByRegion(@Param("date") LocalDate localDate, @Param("region") String region);
	
	// 회원 탈퇴시 미래 예약건 취소
	public int deleteAsListBydeleteUser(UserVO userVO);
	
	// 일반 사용자의 AS 일정 날짜순으로 정렬
    public List<ASVO> selectUserAsListOrderByAsDate(int user_cd);
    
	// 게스트에 해당하는 AS 신고내역 조회
	public List<ASVO> selectAsListByGuest(GuestDTO guest);
	
    // 게스트의 AS 일정 날짜순으로 정렬
	public List<ASVO> selectGuestAsListOrderByAsDate(GuestDTO guest);
	
	// 게스트의 AS 일정 등록
	public int insertAsListByGuest(ASVO vo);

	// 게스트의 일정 수정
	public int updateAsListByGuest(ASVO vo);

}
