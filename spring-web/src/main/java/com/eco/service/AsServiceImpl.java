package com.eco.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.domain.DTO.ASListDTO;
import com.eco.domain.DTO.ASPageResponseDTO;
import com.eco.domain.DTO.AvailableStaffDTO;
import com.eco.domain.DTO.GuestDTO;
import com.eco.domain.vo.ASVO;
import com.eco.domain.vo.UserVO;
import com.eco.exception.ServiceException;
import com.eco.mapper.AsMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsServiceImpl implements AsService {

	private final AsMapper asMapper;

	// 사용자의의 AS 리스트 가져오는 함수
	@Override
	public List<ASVO> getUserAsList(int user_cd, int limit, int offset) {
		try {
			return asMapper.selectAsListByUserPaging(user_cd, limit, offset);
		} catch (Exception e) {
			throw new ServiceException("사용자의 AS 리스트 가져오기 실패", e);
		}
	}

	// 일반 회원의 AS 신고
	@Override
	@Transactional
	public boolean registerAsByCommon(ASVO asvo) {
		try {
			int insertCount = asMapper.insertAsListByCommon(asvo);
			if (insertCount == 0)
				return false;

			LocalDateTime dt = asvo.getAs_date();
			String date = dt.toLocalDate().toString();
			String time = String.format("%02d:00:00", dt.getHour());
			String addr = asvo.getAs_addr().split(" ")[0];
			System.out.println("date " + date);
			System.out.println("addr " + addr);
			System.out.println("time " + time);

			Integer staffCd = asMapper.selectAsStaff(date, addr, time);

			if (staffCd == null)
				return false;

			int updated = asMapper.updateMatchStaff((int) staffCd, asvo.getAs_cd());
			return updated > 0;
		} catch (Exception e) {
			throw new ServiceException("일반 회원의 AS 신고 실패", e);
		}
	}

	// AS 신고 수정 화면에 해당 신고 정보 출력
	@Override
	public ASVO readAsDetailByUser(int as_cd) {
		try {
			return asMapper.selectAsDetailByCommon(as_cd);
		} catch (Exception e) {
			throw new ServiceException("AS 신고 수정 화면에 해당 신고 정보 출력 실패", e);
		}
	}

	// AS 신고 수정
	@Override
	@Transactional
	public boolean editAsListByCommon(ASVO asvo) {
		try {
			int updated = asMapper.updateAsListByCommon(asvo);
			if (updated == 0)
				return false;

			LocalDateTime dt = asvo.getAs_date();
			String date = dt.toLocalDate().toString();
			String time = String.format("%02d:00:00", dt.getHour());
			String addr = asvo.getAs_addr().split(" ")[0];

			Integer staffCd = asMapper.selectAsStaff(date, addr, time);
			if (staffCd == null)
				return false;

			int reassigned = asMapper.updateMatchStaff(staffCd, asvo.getAs_cd());
			return reassigned >= 0;
		} catch (Exception e) {
			throw new ServiceException("AS 신고 수정 실패", e);
		}
	}

	// AS 신고 삭제
	@Override
	public boolean cancleAsListByCommon(int as_cd) {
		try {
			int result = asMapper.deleteAsListByCommon(as_cd);
			return result > 0;
		} catch (Exception e) {
			throw new ServiceException("AS 신고 삭제 실패", e);
		}
	}

	// 관리자/기사의 상태 업데이트
	@Override
	public void updateStatus(int as_cd, String as_status) {
		try {
			asMapper.updateStatusByCd(as_cd, as_status);
		} catch (Exception e) {
			throw new ServiceException("상태 업데이트 실패", e);
		}
	}

	// 항목 상세 조회
	@Override
	public ASListDTO getAsTask(int as_cd) {
		try {
			return asMapper.selectAsTask(as_cd);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	// 직원의 모든 스케쥴 조회
	@Override
	public List<ASListDTO> getScheduleByStaffAndDate(LocalDate start, LocalDate end, String staffInfo) {
		try {
			return asMapper.selectScheduleByStaffAndDate(start, end, staffInfo);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	// 관리자의 모든 스케쥴 조회
	@Override
	public List<ASListDTO> getScheduleByPeriodAndStaff(LocalDate start, LocalDate end, String staffInfo) {
		try {
			return asMapper.selectScheduleByDate(start, end, staffInfo);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

	// 지역 일자 기준 전 직원 스케줄 조회
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

	// 회원 탈퇴시 미래 예약건 취소
	@Override
	public boolean cancleAsListBydeleteUser(UserVO userVO) {
		try {
			int result = asMapper.deleteAsListBydeleteUser(userVO);
			return result > 0;
		} catch (Exception e) {
			throw new ServiceException("사용자 회원 탈퇴 실패", e);
		}
	}

	// 날짜순으로 조회
	@Override
	public List<ASVO> getUserAsListOrderByAsDate(int user_cd) {
		try {
			return asMapper.selectUserAsListOrderByAsDate(user_cd);
		} catch (Exception e) {
			throw new ServiceException("사용자의 AS 리스트 가져오기 실패", e);
		}
	}

	// 게스트의 전체 일정 조회
	@Override
	public List<ASVO> getGuestAsList(GuestDTO guest) {
		try {
			return asMapper.selectAsListByGuest(guest);
		} catch (Exception e) {
			throw new ServiceException("게스트의 AS 리스트 가져오기 실패", e);
		}
	}

	// 게스트의 전체 일정 조회(날짜순)
	@Override
	public List<ASVO> getGuestAsListOrderByAsDate(GuestDTO guest) {
		try {
			return asMapper.selectGuestAsListOrderByAsDate(guest);
		} catch (Exception e) {
			throw new ServiceException("게스트의 AS 리스트 가져오기 실패", e);
		}
	}

	// 게스트의 일정 등록
	@Override
	public boolean registerAsByGuest(ASVO vo) {
		try {
			int insertCount = asMapper.insertAsListByGuest(vo);
			if (insertCount == 0)
				return false;

			LocalDateTime dt = vo.getAs_date();
			String date = dt.toLocalDate().toString();
			String time = String.format("%02d:00:00", dt.getHour());
			String addr = vo.getAs_addr().split(" ")[0];

			Integer staffCd = asMapper.selectAsStaff(date, addr, time);

			if (staffCd == null)
				return false;

			int updated = asMapper.updateMatchStaff((int) staffCd, vo.getAs_cd());
			return updated > 0;
		} catch (Exception e) {
			throw new ServiceException("게스트의 AS 신고 실패", e);
		}
	}

	// 게스트의 일정 수정
	@Override
	public boolean editAsListByGuest(ASVO vo) {
		try {
			int updated = asMapper.updateAsListByGuest(vo);
			if (updated == 0)
				return false;

			LocalDateTime dt = vo.getAs_date();
			String date = dt.toLocalDate().toString();
			String time = String.format("%02d:00:00", dt.getHour());
			String addr = vo.getAs_addr().split(" ")[0];

			Integer staffCd = asMapper.selectAsStaff(date, addr, time);
			if (staffCd == null)
				return false;

			int reassigned = asMapper.updateMatchStaff(staffCd, vo.getAs_cd());
			return reassigned >= 0;
		} catch (Exception e) {
			throw new ServiceException("AS 신고 수정 실패", e);
		}
	}

	@Override
	public ASPageResponseDTO getUserAsListWithPaging(int user_cd, int page, int size) {
		try {
			int offset = (page - 1) * size;
			int totalCount = asMapper.selectAsCountByUser(user_cd);
			List<ASVO> list = asMapper.selectAsListByUserPaging(user_cd, size, offset);
			
			int totalPages = (int) Math.ceil((double) totalCount / size);

			int pageGroup = (page - 1) / 10;
			int startPage = pageGroup * 10 + 1;
			int endPage = Math.min(startPage + 9, totalPages);

			ASPageResponseDTO dto = new ASPageResponseDTO();
			dto.setAsList(list);
			dto.setCurrentPage(page);
			dto.setTotalPages(totalPages);
			dto.setStartPage(startPage);
			dto.setEndPage(endPage);
			dto.setHasPrev(startPage > 1);
			dto.setHasNext(endPage < totalPages);
			return dto;
		} catch (Exception e) {
			throw new ServiceException("AS 조회 페이징 처리 실패", e);
		}
	}

}
