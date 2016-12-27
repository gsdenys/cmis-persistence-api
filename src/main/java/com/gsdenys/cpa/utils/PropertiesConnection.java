package com.gsdenys.cpa.utils;

import com.gsdenys.cpa.exception.CpaPersistenceException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gsdenys on 24/12/16.
 */
public class PropertiesConnection {

    private final static String PROPERTIES_FILE_SUFIX = "-cmis.properties";

    /**
     * Load properties file
     *
     * @param connName
     *          the name of persistence CmisExec
     * @return Properties
     *          the connection properties file name
     */
    public Properties loadPropertiesFile(String connName) throws CpaPersistenceException {
        String fileName = connName + PROPERTIES_FILE_SUFIX;

        Properties properties = new Properties();
        try {
            properties.load(
                    this.getClass().getClassLoader().getResourceAsStream(fileName)
            );
            return properties;
        } catch (IOException ioException) {
            throw new CpaPersistenceException(
                    "The file " + connName + PROPERTIES_FILE_SUFIX + " not found at classpath.",
                    ioException
            );
        }
    }


    /**
     * Transform a string in a map of repositories is
     *
     * @param repoString A repositories string comma separated
     * @return Map the repositories map
     */
    public Map<String, String> stringToMap(String repoString){
        String regex = "(.*)\\[(.*)\\]";
        Pattern pattern = Pattern.compile(regex);

        Map<String, String> map = new HashMap<>();

        String[] strings = repoString.split(",");

        for (String str : strings) {
            Matcher matcher = pattern.matcher(str);;

            if(matcher.matches()){
                String id = matcher.group(1);
                String alias = matcher.group(2);

                map.put(alias.trim(), id.trim());
                continue;
            }

            map.put(str.trim(), str.trim());
        }

        return map;
    }
}
