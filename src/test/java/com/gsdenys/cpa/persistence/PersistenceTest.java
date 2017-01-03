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
package com.gsdenys.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit Test for {@link Persistence} class
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
@RunWith(CmisInMemoryRunner.class)
@Configure()
public class PersistenceTest {

    /**
     * Test case for {@link Persistence#createEntityManagerFactory(String)}
     *
     * @throws Exception
     */
    @Test
    public void createEntityManagerFactory() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("docs");
        Assert.assertNotNull("The factory should be not null!", factory);

        EntityManagerFactory factory2 = Persistence.createEntityManagerFactory("docs");
        Assert.assertNotNull("The factory2 should be not null!", factory2);

        Assert.assertSame("The factory and factory2 should be the same", factory, factory2);
    }

    @Test
    public void Persistence() throws Exception {
        try {
            Persistence.class.newInstance();
            Assert.fail("The Persistence class should not be instantiated.");
        } catch (IllegalAccessException e) {
            //nothing to do here
        }
    }
}