package com.eureka.ip.team1.urjung_admin.backoffice.line.service;

import com.eureka.ip.team1.urjung_admin.backoffice.line.dto.LineDto;
import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import com.eureka.ip.team1.urjung_admin.backoffice.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;
    //사용자 전체 회선 조회
    @Override
    public List<LineDto> getAllLinesByUserId(String userId) {
        List<Line> lines = lineRepository.getAllLinesByUserId(userId);

        return lines.stream()
                .map(line -> LineDto.builder()
                        .id(line.getUserId())
                        .userId(line.getUserId())
                        .phoneNumber(line.getPhoneNumber())
                        .planId(line.getPlanId())
                        .status(line.getStatus().name())
                        .startDate(line.getStartDate())
                        .endDate(line.getEndDate())
                        .discountedPrice(line.getDiscountedPrice())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
