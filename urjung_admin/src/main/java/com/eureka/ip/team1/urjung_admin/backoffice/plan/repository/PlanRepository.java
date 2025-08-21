package com.eureka.ip.team1.urjung_admin.backoffice.plan.repository;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 요금제 목록 및 검색 Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    // 이름, 설명 통합 검색 (대소문자 무시)
    @Query("SELECT p FROM Plan p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Plan> searchByNameOrDescription(String keyword);

    // 이름 Desc/Asc
    List<Plan> findAllByOrderByNameAsc();

    List<Plan> findAllByOrderByNameDesc();

    // 가격 Asc/Desc
    List<Plan> findAllByOrderByPriceAsc();

    List<Plan> findAllByOrderByPriceDesc();

    // 데이터 Asc/Desc
    List<Plan> findAllByOrderByDataAmountAsc();

    List<Plan> findAllByOrderByDataAmountDesc();

    // 통화량 Asc/Desc
    List<Plan> findAllByOrderByCallAmountAsc();
    List<Plan> findAllByOrderByCallAmountDesc();

    // 문자량 Asc/Desc
    List<Plan> findAllByOrderBySmsAmountAsc();
    List<Plan> findAllByOrderBySmsAmountDesc();

    // 가입자 수 count Desc/Asc
    @Query("SELECT p FROM Plan p LEFT JOIN p.lines l WITH l.status = 'active' " +
            "GROUP BY p ORDER BY COUNT(l.lineId) DESC")
    List<Plan> findAllByOrderByUserAmountDesc();

    @Query("SELECT p FROM Plan p LEFT JOIN p.lines l WITH l.status = 'active' " +
            "GROUP BY p ORDER BY COUNT(l.lineId) ASC")
    List<Plan> findAllByOrderByUserAmountAsc();
}
