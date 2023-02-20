package com.blazebit.springdatajpa.jpa.repository;

import com.blazebit.springdatajpa.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long>,
        KeysetPaginationRepository<Cat> {
}
