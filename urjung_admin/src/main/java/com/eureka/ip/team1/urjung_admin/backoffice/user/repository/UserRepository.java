package com.eureka.ip.team1.urjung_admin.backoffice.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    // email, 이름 통합 검색 (대소문자 무시)
    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByNameOrEmail(String keyword);

    // 가입 서비스 count Asc
    @Query("SELECT u FROM User u LEFT JOIN u.lines l GROUP BY u ORDER BY COUNT(l.lineId) ASC")
    List<User> findAllByOrderServiceCountAsc();

    // 가입 서비스 count Desc
    @Query("SELECT u FROM User u LEFT JOIN u.lines l GROUP BY u ORDER BY COUNT(l.lineId) DESC")
    List<User> findAllByOrderServiceCountDesc();

    // Id Asc
    List<User> findAllByOrderByEmailAsc();

    // Id Desc
    List<User> findAllByOrderByEmailDesc();

    // 이름 Asc
    List<User> findAllByOrderByNameAsc();

    // 이름 Desc
    List<User> findAllByOrderByNameDesc();
}
