package com.eco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.domain.ASVO;
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

}
