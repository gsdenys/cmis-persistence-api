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

import org.junit.*;
import org.junit.runner.Runner;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import javax.servlet.ServletException;
import java.io.File;


/**
 * Unit Test for {@link Persistence} class
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class PersistenceTest {

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception {

        server = new Server();
        SocketConnector connector = new SocketConnector();
        connector.setPort(8090);
        server.setConnectors(new Connector[] { connector });
        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/cmis");

        File file = new File("webapps/cmis.war");
        context.setWar(file.getAbsolutePath());

        server.addHandler(context);
        /*Thread monitor = new MonitorThread();
        monitor.start();*/
        server.start();

        //server.join();

        int a=1;
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }


    /**
     * Test case for {@link Persistence#createEntityManagerFactory(String)}
     * @throws Exception
     */
    @Test
    public void createEntityManagerFactory() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("sample");
        Assert.assertNotNull("The factory should be not null!", factory);

        EntityManagerFactory factory2 = Persistence.createEntityManagerFactory("sample");
        Assert.assertNotNull("The factory2 should be not null!", factory2);

        Assert.assertSame("The factory and factory2 should be the same", factory, factory2);
    }

}