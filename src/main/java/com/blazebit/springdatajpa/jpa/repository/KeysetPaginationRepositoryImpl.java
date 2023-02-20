package com.blazebit.springdatajpa.jpa.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.criteria.BlazeCriteria;
import com.blazebit.persistence.criteria.BlazeCriteriaBuilder;
import com.blazebit.persistence.criteria.BlazeCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;

public class KeysetPaginationRepositoryImpl<T> implements KeysetPaginationRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public PagedList<T> findTopN(Specification<T> specification,
                                 Sort sortBy,
                                 int pageSize,
                                 Class<T> entityClass) {
        return sortedCriteriaBuilder(specification, sortBy, entityClass)
                .page(0, pageSize)
                .withKeysetExtraction(true)
                .getResultList();
    }

    @Override
    public PagedList<T> findNextN(Specification<T> specification,
                                  Sort sortBy,
                                  PagedList<T> previousPage,
                                  Class<T> entityClass) {
        return sortedCriteriaBuilder(specification, sortBy, entityClass)
                .page(
                        previousPage.getKeysetPage(),
                        previousPage.getPage() * previousPage.getMaxResults(),
                        previousPage.getMaxResults()
                )
                .getResultList();
    }

    private CriteriaBuilder<T> sortedCriteriaBuilder(Specification<T> specification,
                                                       Sort sortBy,
                                                       Class<T> entityClass) {
        BlazeCriteriaBuilder cb = BlazeCriteria.get(criteriaBuilderFactory);
        BlazeCriteriaQuery<T> query = cb.createQuery(entityClass);

        Root<T> root = query.from(entityClass);

        // todo this is not working as intended for OR in specification
        if(null != specification){
            query.where(specification.toPredicate(root, query, cb));
        }

        CriteriaBuilder<T> builder = query.createCriteriaBuilder(entityManager);

        sortBy.forEach(order -> {
            builder.orderBy(
                    order.getProperty(),
                    order.isAscending()
            );
        });
        return builder;
    }
}