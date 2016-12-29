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

import com.gsdenys.cpa.exception.CpaConnectionException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.utils.PropertiesConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * The persistence class to abstract all complexity on create a new {@link EntityManagerFactory} object
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class Persistence {

    public final static String PROP_URL = "cpa.url";
    public final static String PROP_USER = "cpa.user";
    public final static String PROP_PASSWRD = "cpa.password";
    public final static String PROP_REPOSITORY_ID = "cpa.repository";
    public final static String PROP_REPOSITORIES = "cpa.repositories";


    private static Map<String, EntityManagerFactory> factoryStore;

    static {
        factoryStore = new HashMap<>();
    }

    private Persistence() {}

    /**
     * Create a new {@link EntityManagerFactory} instance
     *
     * @param connName
     *          the name of persistence CmisExec
     * @return EntityManagerFactory
     *          the {@link EntityManagerFactory} instance
     */
    public static EntityManagerFactory createEntityManagerFactory(final String connName)
            throws CpaPersistenceException, CpaConnectionException, CpaRuntimeException {

        PropertiesConnection pCon = new PropertiesConnection();

        //try to get an entity manager already created.
        EntityManagerFactory emf = factoryStore.get(connName);
        if (emf != null) {
            return emf;
        }

        //set properties at properties that be used to create a connection
        Properties properties = pCon.loadPropertiesFile(connName);
        EntityManagerFactory factory = new EntityManagerFactory(
                properties.getProperty(PROP_URL),
                properties.getProperty(PROP_USER),
                properties.getProperty(PROP_PASSWRD),
                properties.getProperty(PROP_REPOSITORY_ID)
        );

        //set to the factory all mapped repositories with their ids and alias
        String repos = properties.getProperty(PROP_REPOSITORIES);
        if (repos != null) {
            Map<String, String> map = pCon.stringToMap(properties.getProperty(PROP_REPOSITORIES));
            factory.setMappedRepository(map);
        }

        factoryStore.put(connName, factory);
        return factory;
    }

}
