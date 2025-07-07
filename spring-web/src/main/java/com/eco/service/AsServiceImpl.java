package com.eco.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.AsMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsServiceImpl implements AsService {

	private final AsMapper asMapper;

	// AS 신고내역 중 해당 날짜의 예약 시간 조회
	@Override
	public List<String> getTotalAs(LocalDate date) {
		List<ASVO> allReservations = asMapper.selectAllAsList();

		return allReservations.stream().filter(vo -> vo.getAs_date().toLocalDate().equals(date)) // 같은 날짜만
				.map(vo -> {
					// 시간은 HH:00으로 고정 (분은 무시)
					int hour = vo.getAs_date().getHour();
					return String.format("%02d:00", hour);
				}).distinct() // 중복 제거
				.collect(Collectors.toList());
	}

	// 기사, 관리자의 AS 리스트 가져오는 함수
	@Override
	public List<ASVO> getAsList(String userType, int staffCd) {
		if ("admin".equals(userType)) {
			return asMapper.selectAllAsList();
		} else if ("staff".equals(userType)) {
			return asMapper.selectAsListByStaff(staffCd);
		} else {
			return null; // 접근 불가
		}
	}

	// 사용자의의 AS 리스트 가져오는 함수
	@Override
	public List<ASVO> getUserAsList(int user_cd) {
		return asMapper.selectAsListByUser(user_cd);
	}

	// 일반 회원의 AS 신고
	@Override
	public boolean registerAsByCommon(ASVO asvo) {
		int result = asMapper.insertAsListByCommon(asvo);
		return result > 0;
	}

	// AS 신고 수정 화면
	@Override
	public ASVO readAsDetailByUser(int as_cd) {
		return asMapper.selectAsDetailByCommon(as_cd);
	}

	// AS 신고 수정
	@Override
	public boolean editAsListByCommon(ASVO asvo) {
		int result = asMapper.updateAsListByCommon(asvo);
		return result > 0;
	}

	// AS 신고 삭제
	@Override
	public boolean cancleAsListByCommon(int as_cd) {
		int result = asMapper.deleteAsListByCommon(as_cd);
		return result > 0;
	}

	// 기사, 관리자의 AS 리스트 가져오는 함수
	@Override
	public List<ASListDTO> getAsDtoList(String userType, int staffCd) {
		try {
			if ("admin".equals(userType)) {
				return asMapper.selectAllAsDtoList();
			} else if ("staff".equals(userType)) {
				return asMapper.selectAsDtoListByStaff(staffCd);
			} else {
				return null; // 접근 불가
			}
		} catch (Exception e) {
			throw new ServiceException("AS 리스트 조회 실패", e);
		}
	}

	@Override
	public void updateStatus(int as_cd, String as_status) {
		try {
			asMapper.updateStatusByCd(as_cd, as_status);
		} catch (Exception e) {
			throw new ServiceException("상태 업데이트 실패", e);
		}
	}

	@Override
	public ASListDTO getAsTask(int as_cd) {
		try {
			return asMapper.selectAsTask(as_cd);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	@Override
	public List<ASVO> getScheduleByUserAndDate(int user_cd, LocalDate date) {
		return asMapper.selectScheduleByUserAndDate(user_cd, date);
	}

	@Override
	public List<ASVO> getScheduleByStaffAndDate(int staff_cd, LocalDate date) {
		return asMapper.selectScheduleByStaffAndDate(staff_cd, date);
	}

}
