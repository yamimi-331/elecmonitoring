package com.eco.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eco.domain.ASListDTO;
import com.eco.domain.ASVO;
import com.eco.domain.DTO.AvailableStaffDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.AsMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsServiceImpl implements AsService {

	private final AsMapper asMapper;
	
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

	//상태 업데이트
	@Override
	public void updateStatus(int as_cd, String as_status) {
		try {
			asMapper.updateStatusByCd(as_cd, as_status);
		} catch (Exception e) {
			throw new ServiceException("상태 업데이트 실패", e);
		}
	}

	//항목 상세 조회
	@Override
	public ASListDTO getAsTask(int as_cd) {
		try {
			return asMapper.selectAsTask(as_cd);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	@Override
	public List<ASListDTO> getScheduleByUserAndDate(int user_cd, LocalDate date) {
		return asMapper.selectScheduleByUserAndDate(user_cd, date);
	}
	// 직원의 모든 스케쥴 조회
	@Override
	public List<ASListDTO> getScheduleByStaffAndDate(int staffCd, LocalDate localDate) {
		try {
			return asMapper.selectScheduleByStaffAndDate(staffCd, localDate);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	// 관리자의 모든 스케쥴 조회
	@Override
	public List<ASListDTO> getScheduleByDate(LocalDate localDate) {
		try {
			return asMapper.selectScheduleByDate(localDate);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	// AS 신고내역 중 해당 날짜의 예약 시간 조회
	@Override
	public List<String> getFullyBookedSlots(LocalDate date, String region) {
		try {
			// 1) 쿼리 결과: 직원별 시간대별 일정 정보
			List<AvailableStaffDTO> allData = asMapper.selectAllAsListByRegion(date, region);

			// 2) slot_time 으로 그룹화
			Map<String, List<AvailableStaffDTO>> groupedBySlot = allData.stream()
					.collect(Collectors.groupingBy(AvailableStaffDTO::getSlot_time));

			List<String> fullyBookedSlots = new ArrayList<>();

			// 3) 각 시간대별 직원 예약 상태 검사
			for (Map.Entry<String, List<AvailableStaffDTO>> entry : groupedBySlot.entrySet()) {
				String slot = entry.getKey();
				List<AvailableStaffDTO> slotList = entry.getValue();

				// 직원 중 하나라도 일정이 없으면(false), 모두 있으면(true)
				boolean allBooked = slotList.stream().allMatch(vo -> vo.getAs_cd() != null && vo.getAs_cd() != 0);

				if (allBooked) {
					fullyBookedSlots.add(slot.substring(0, 5));
				}
			}

			return fullyBookedSlots;
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

}
