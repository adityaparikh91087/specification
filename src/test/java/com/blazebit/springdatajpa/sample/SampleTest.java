/*
 * Copyright 2014 - 2022 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.springdatajpa.sample;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import java.util.*;
import com.blazebit.springdatajpa.model.Cat;
import com.blazebit.springdatajpa.model.Person;
import com.blazebit.springdatajpa.view.CatSimpleView;
import com.blazebit.springdatajpa.repository.CatSimpleViewRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SampleTest.TestConfig.class)
public class SampleTest extends AbstractSampleTest {

    @Autowired
    private CatSimpleViewRepository catSimpleViewRepository;

    @Test
    public void sampleTest() {
        final Iterable<CatSimpleView> listIterable = catSimpleViewRepository.findAll();
        final List<CatSimpleView> list = new ArrayList<>();
        listIterable.forEach(view -> list.add(view));
        Assert.assertEquals(6, list.size());
    }

    @Configuration
    @ComponentScan("com.blazebit.springdatajpa")
    @EnableEntityViews(basePackages = { "com.blazebit.springdatajpa.view"})
    @EnableBlazeRepositories(
            basePackages = "com.blazebit.springdatajpa.repository")
    static class TestConfig {
    }
}