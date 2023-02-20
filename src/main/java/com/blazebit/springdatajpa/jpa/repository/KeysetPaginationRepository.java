package com.blazebit.springdatajpa.jpa.repository;

import com.blazebit.persistence.PagedList;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface KeysetPaginationRepository<T> {

    PagedList<T> findTopN(
            Specification<T> specification,
            Sort sortBy,
            int pageSize,
            Class<T> entityClass
    );

    PagedList<T> findNextN(
            Specification<T> specification,
            Sort orderBy,
            PagedList<T> previousPage,
            Class<T> entityClass
    );
}
