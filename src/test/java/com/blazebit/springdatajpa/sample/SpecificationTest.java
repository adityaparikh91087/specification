package com.blazebit.springdatajpa.sample;

import com.blazebit.persistence.PagedList;
import com.blazebit.springdatajpa.model.Cat;
import com.blazebit.springdatajpa.jpa.repository.CatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SpecificationTest {

    @Autowired
    CatRepository catRepository;

    Random random = new Random();

    @BeforeEach
    void insert(){
        catRepository.save(getCat("AAPL"));
        catRepository.save(getCat("SNAP"));
        catRepository.save(getCat("TWTR"));

        catRepository.save(getCat("QQQ"));
        catRepository.save(getCat("TSLA"));
        catRepository.save(getCat("AMZN"));

        catRepository.save(getCat("GOOGL"));
        catRepository.save(getCat("ADBE"));
        catRepository.save(getCat("META"));

        catRepository.save(getCat("TMUS"));
        catRepository.save(getCat("SPY"));
        catRepository.save(getCat("AMD"));

        catRepository.save(getCat("AMEX"));
    }

    @Test
    void testSpecificationOrWithBlaze(){
      //  Sort sort = Sort.by("name").ascending()
        //        .and(Sort.by("id").ascending());

        Sort sort = Sort.by("id").ascending();


        Specification<Cat> nameBeginsWithAOrT = nameBeginsWith("A").or(nameBeginsWith("T"));
        PagedList<Cat> firstPage = catRepository.findTopN(nameBeginsWithAOrT, sort, 3, Cat.class);
        Assertions.assertEquals(3, firstPage.size());

        PagedList<Cat> secondPage = catRepository.findNextN(nameBeginsWithAOrT, sort, firstPage, Cat.class);
        Assertions.assertEquals(3, secondPage.size());
        Assertions.assertEquals(3, secondPage.getTotalPages());
        Assertions.assertEquals(8, secondPage.getTotalSize());

        //todo blaze does not iterate over OR correctly
        PagedList<Cat> third = catRepository.findNextN(nameBeginsWithAOrT, sort, secondPage, Cat.class);
        Assertions.assertEquals(2, third.size());


    }

    private Cat getCat(String catName) {
        return new Cat(catName, random.nextInt(100), null);
    }

    private Specification<Cat> nameBeginsWith(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),  name+"%");
    }

}
