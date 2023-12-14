package com.example.motoBE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.motoBE.model.Error;

public interface ErrorRepository extends JpaRepository<Error, Integer> {
	
	@Query("SELECT e FROM Error e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Error> searchErrorByStringName(@Param("keyword") String keyword);
}
