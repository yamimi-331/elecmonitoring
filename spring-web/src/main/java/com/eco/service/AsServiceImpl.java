package com.eco.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.domain.DTO.ASListDTO;
import com.eco.domain.DTO.ASPageResponseDTO;
import com.eco.domain.DTO.AsScheduleResponseDTO;
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


	@Override
	public ASListDTO readAsListDetailByUser(int as_cd) {
		try {
			return asMapper.selectAsListDetailByCommon(as_cd);
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
			 System.out.println("조회 요청 날짜: " + date); // 요청 날짜 출력
		        System.out.println("조회 요청 지역: " + region); // 요청 지역 출력

		        // 1) 쿼리 결과: 직원별 시간대별 일정 정보
		        List<AvailableStaffDTO> allData = asMapper.selectAllAsListByRegion(date, region);

		        // --- 디버깅 포인트 1: allData 내용 확인 ---
		        if (allData == null || allData.isEmpty()) {
		            System.out.println("DEBUG: asMapper.selectAllAsListByRegion 결과가 비어있습니다. DB 또는 매퍼 설정을 확인하세요.");
		            return new ArrayList<>(); // 데이터가 없으면 빈 리스트 반환
		        } else {
		            System.out.println("DEBUG: asMapper.selectAllAsListByRegion 결과 (총 " + allData.size() + "건):");
		            allData.forEach(dto -> System.out.println("  " + dto)); // 모든 DTO 출력
		        }

		        // 2) slot_time 으로 그룹화
		        // String으로 된 slot_time을 LocalTime으로 파싱하여 그룹화하는 것이 안정적입니다.
		        Map<LocalTime, List<AvailableStaffDTO>> groupedBySlot = allData.stream()
		                .collect(Collectors.groupingBy(dto -> LocalTime.parse(dto.getSlot_time()))); // String -> LocalTime 변환

		        List<String> fullyBookedSlots = new ArrayList<>();

		        // --- 디버깅 포인트 2: 그룹화된 슬롯 내용 확인 ---
		        System.out.println("DEBUG: 시간대별 그룹화 결과:");
		        if (groupedBySlot.isEmpty()) {
		            System.out.println("  그룹화된 시간대가 없습니다.");
		        } else {
		            groupedBySlot.forEach((slotTime, list) -> {
		                System.out.println("  시간대 " + slotTime + ": 직원 " + list.size() + "명");
		                list.forEach(dto -> System.out.println("    - " + dto.getStaff_nm() + " (as_cd: " + dto.getAs_cd() + ")"));
		            });
		        }

		        // 3) 각 시간대별 직원 예약 상태 검사
		        for (Map.Entry<LocalTime, List<AvailableStaffDTO>> entry : groupedBySlot.entrySet()) {
		            LocalTime slotTime = entry.getKey();
		            List<AvailableStaffDTO> slotList = entry.getValue();

		            // 해당 시간대의 모든 직원이 예약되었는지 확인
		            boolean allBooked = slotList.stream()
		                                        .allMatch(vo -> vo.getAs_cd() != null && vo.getAs_cd() != 0);

		            // --- 디버깅 포인트 3: 각 시간대별 allBooked 상태 확인 ---
		            System.out.println("DEBUG: 시간대 " + slotTime + "의 모든 직원 예약 여부: " + allBooked);

		            if (allBooked) {
		                // LocalTime을 "HH:mm" 형식으로 포맷하여 추가
		                fullyBookedSlots.add(slotTime.format(DateTimeFormatter.ofPattern("HH:mm")));
		            }
		        }

		        // --- 디버깅 포인트 4: 최종 결과 확인 ---
		        System.out.println("DEBUG: 최종적으로 '완전히 예약됨'으로 판단된 시간대: " + fullyBookedSlots);

		        return fullyBookedSlots;
		    } catch (Exception e) {
		        System.err.println("ERROR: 스케줄 조회 중 오류 발생: " + e.getMessage());
		        e.printStackTrace(); // 스택 트레이스 출력하여 자세한 오류 원인 확인
		        throw new ServiceException("항목 상세 조회 실패", e);
		    }
	}
	// 새롭게 추가될 메서드들 (페이징 적용)

	/**
	 * 관리자 권한으로 기간 및 담당자명 기준으로 AS 일정을 페이징 처리하여 조회합니다.
	 *
	 * @param startDate 조회 시작일
	 * @param endDate   조회 종료일
	 * @param staffInfo 담당자명 (검색 조건)
	 * @param offset    시작 위치 (건너뛸 개수)
	 * @param limit     가져올 개수
	 * @return Map<String, Object> (content: 현재 페이지 목록, totalElements: 전체 항목 수)
	 */
	@Override
	public AsScheduleResponseDTO getScheduleByPeriodAndStaffPaged(LocalDate startDate, LocalDate endDate,
			String staffInfo, int offset, int limit) {
		try {
			long totalCount = asMapper.countAsScheduleByPeriodAndStaff(startDate, endDate, staffInfo);
			List<ASListDTO> currentAsList = asMapper.selectAsScheduleByPeriodAndStaffPaged(startDate, endDate,
					staffInfo, offset, limit);

			AsScheduleResponseDTO response = new AsScheduleResponseDTO();
			response.setContent(currentAsList);
			response.setTotalElements(totalCount);
			// 페이지 관련 정보도 서비스에서 계산하여 DTO에 설정합니다.
			response.setTotalPages((int) Math.ceil((double) totalCount / limit));
			response.setCurrentPage((offset / limit) + 1); // 1-based 페이지 번호 계산
			response.setPageSize(limit);

			return response;
		} catch (Exception e) {
			throw new ServiceException("관리자 AS 일정 페이징 조회 실패", e);
		}
	}

	/**
	 * 일반 직원 권한으로 본인의 AS 일정을 페이징 처리하여 조회합니다.
	 *
	 * @param startDate 조회 시작일
	 * @param endDate   조회 종료일
	 * @param staffId   직원 ID (본인 스케줄 조회)
	 * @param offset    시작 위치
	 * @param limit     가져올 개수
	 * @return Map<String, Object> (content: 현재 페이지 목록, totalElements: 전체 항목 수)
	 */
	@Override
	public AsScheduleResponseDTO  getScheduleByStaffAndDatePaged(LocalDate startDate, LocalDate endDate, String staffId,
			int offset, int limit) {
		try {
			long totalCount = asMapper.countAsScheduleByStaffAndDate(startDate, endDate, staffId);
            List<ASListDTO> currentAsList = asMapper.selectAsScheduleByStaffAndDatePaged(startDate, endDate, staffId, offset, limit);

            AsScheduleResponseDTO response = new AsScheduleResponseDTO();
            response.setContent(currentAsList);
            response.setTotalElements(totalCount);
            response.setTotalPages((int) Math.ceil((double) totalCount / limit));
            response.setCurrentPage((offset / limit) + 1);
            response.setPageSize(limit);

            return response;
		} catch (Exception e) {
			throw new ServiceException("직원 AS 일정 페이징 조회 실패", e);
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

	// 사용자의의 AS 리스트 가져오는 함수(페이징)
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

	// 사용자의의 AS 리스트 가져오는 함수(페이징) 일자순으로 정렬
	@Override
	public ASPageResponseDTO getUserAsListOrderByAsDateWithPaging(int user_cd, int page, int size) {
		try {
			int offset = (page - 1) * size;
			int totalCount = asMapper.selectAsCountByUser(user_cd);
			List<ASVO> list = asMapper.selectAsListByUserOrderByDatePaging(user_cd, size, offset);

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

	@Override
	public ASPageResponseDTO getGuestAsListWithPaging(GuestDTO guest, int page, int size) {
		try {
			int offset = (page - 1) * size;
			int totalCount = asMapper.countGuestAsList(guest.getGuest_mail(), guest.getGuest_nm());
			List<ASVO> list = asMapper.selectGuestAsListWithPaging(
					guest.getGuest_mail(), guest.getGuest_nm(), size, offset);

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

	@Override
	public ASPageResponseDTO getGuestAsListOrderByAsDateWithPaging(GuestDTO guest, int page, int size) {
		try {
			int offset = (page - 1) * size;
			int totalCount = asMapper.countGuestAsList(guest.getGuest_mail(), guest.getGuest_nm());
			List<ASVO> list = asMapper.selectGuestAsListOrderByAsDateWithPaging(guest.getGuest_mail(), guest.getGuest_nm(),
					size, offset);

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
