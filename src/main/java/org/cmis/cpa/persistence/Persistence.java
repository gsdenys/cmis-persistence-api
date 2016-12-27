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
package org.cmis.cpa.persistence;

import org.cmis.cpa.exception.CpaConnectionException;
import org.cmis.cpa.exception.CpaPersistenceException;
import org.cmis.cpa.exception.CpaRuntimeException;
import org.cmis.cpa.utils.PropertiesConnection;

import java.io.IOException;
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

    private final static String URL = "cpa.url";
    private final static String USER = "cpa.user";
    private final static String PASSWRD = "cpa.password";
    private final static String REPOSITORY_ID = "cpa.repository";
    private final static String REPOSITORIES = "cpa.repositories";


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

        Properties properties = pCon.loadPropertiesFile(connName);
        EntityManagerFactory factory = new EntityManagerFactory(
                properties.getProperty(URL),
                properties.getProperty(USER),
                properties.getProperty(PASSWRD),
                properties.getProperty(REPOSITORY_ID)
        );

        //set to the factory all mapped repositories with their ids and alias
        String repos = properties.getProperty(REPOSITORIES);
        if (repos != null) {
            Map<String, String> map = pCon.stringToMap(properties.getProperty(REPOSITORIES));
            factory.setMappedRepository(map);
        }

        factoryStore.put(connName, factory);
        return factory;
    }

}
