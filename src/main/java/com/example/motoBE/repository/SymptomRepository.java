package com.example.motoBE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.motoBE.model.Symptom;

public interface SymptomRepository extends JpaRepository<Symptom, Integer>{
	@Query("SELECT s FROM Symptom s WHERE LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Symptom> searchSymptomsByStringDescription(@Param("keyword") String keyword);
}
