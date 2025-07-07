package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.ASListDTO;
import com.eco.exception.ServiceException;
import com.eco.mapper.AsMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsServiceImpl implements AsService {

	private final AsMapper asMapper;

	// 기사, 관리자의 AS 리스트 가져오는 함수
	@Override
	public List<ASListDTO> getAsList(String userType, int staffCd) {
		try {
			if ("admin".equals(userType)) {
				return asMapper.selectAllAsList();
			} else if ("staff".equals(userType)) {
				return asMapper.selectAsListByStaff(staffCd);
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
	public ASListDTO getAsDetail(int as_cd) {
		try {
			return asMapper.selectAsDetail(as_cd);
		} catch (Exception e) {
			throw new ServiceException("항목 상세 조회 실패", e);
		}
	}

}
