/*
 * Copyright 2016 CMIS Persistence API
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gsdenys.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.github.gsdenys.cpa.entities.Document;
import com.github.gsdenys.cpa.exception.CpaRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by gsdenys on 28/12/16.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure
public class EntityManagerImplTest {

    EntityManagerFactory factory;
    EntityManager entity;

    @Before
    public void setUp() throws Exception {
        this.factory = Persistence.createEntityManagerFactory("sample");
        this.entity = this.factory.getEntityManager();
    }

    @Test
    public void persist() throws Exception {

    }

    @Test
    public void persist1() throws Exception {

    }

    @Test
    public void refresh() throws Exception {

    }

    @Test
    public void refresh1() throws Exception {

    }

    @Test
    public void lock() throws Exception {

    }

    @Test
    public void isLocked() throws Exception {

    }

    @Test
    public void isLockedBy() throws Exception {

    }

    @Test
    public void lockedBy() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void move() throws Exception {

    }

    @Test
    public void copy() throws Exception {

    }

    @Test
    public void getProperties() throws Exception {

    }

    @Test
    public void getProperties1() throws Exception {

    }

    @Test
    public void setProperties() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "TESTE-CASE-SET-PROPERTIES");

        Document document = new Document();

        entity.setProperties(document, properties);

        Assert.assertNotNull("the property name should be null", document.getName());
        Assert.assertEquals(
                "The property name should be 'TESTE-CASE-SET-PROPERTIES'",
                document.getName(),
                "TESTE-CASE-SET-PROPERTIES"
        );

        //a fail case
        properties.put("teste", "TEST-CASE-SER-PROPERTIES-ERROR");
        try {
            entity.setProperties(document, properties);
            fail("The set action at test properties shoul throw CpaRuntimeException");
        } catch (CpaRuntimeException e) {
            //nothing to do
        }
    }
}
