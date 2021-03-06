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
package com.github.gsdenys.cpa.utils;

import com.github.gsdenys.cpa.exception.CpaPersistenceException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Unit Test for {@link PropertiesConnection} class
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class PropertiesConnectionTest {

    @Test
    public void loadPropertiesFile() throws Exception {
        PropertiesConnection pc = new PropertiesConnection();
        Properties prop = pc.loadPropertiesFile("sample");

        Assert.assertNotNull("The Object Properties should not be null", prop);

        Assert.assertEquals("The user should be test", prop.getProperty("cpa.user"), "test");
        Assert.assertEquals("The user should be test", prop.getProperty("cpa.password"), "test");
        Assert.assertEquals("The user should be test", prop.getProperty("cpa.repository"), "A1");
        Assert.assertEquals("The user should be test", prop.getProperty("cpa.repositories"), "A2[trash],A3[RM]");


        try {
            Properties propError = pc.loadPropertiesFile("error");
            Assert.fail("The method should be not be able do load a properties from a non existent file");
        } catch (CpaPersistenceException e) {
            //nothing to do
        }

    }

    @Test
    public void stringToMap() throws Exception {
        String repos = "1234,12345[repo1], 12356, 123[repo3]";
        List<String> repoKey = new ArrayList<>(Arrays.asList("1234", "repo1", "12356", "repo3"));

        PropertiesConnection pc = new PropertiesConnection();

        Map<String, String> map = pc.stringToMap(repos);

        map.keySet().forEach(key -> {
            Assert.assertTrue("Any key element is not present in repokey", repoKey.contains(key));
        });
    }
}