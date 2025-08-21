package com.eureka.ip.team1.urjung_admin.backoffice.line.repository;

import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, String> {

    List<Line> getAllLinesByUserId(String userId);
}
