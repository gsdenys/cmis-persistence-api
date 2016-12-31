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
package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.annotations.BaseType;
import com.gsdenys.cpa.annotations.Metadata;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.TypeParser;
import com.sun.istack.NotNull;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * The persistence Executor
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class PersistExec {

    private CmisExec cmisExec;

    /**
     * Default Builder
     *
     * @param exec the {@link CmisExec} object
     */
    public PersistExec(CmisExec exec) {
        this.cmisExec = exec;
    }


    /**
     * Persist entity at the repository
     *
     * @param entity the entity
     * @param <E>    any element
     * @return E the persistent object
     * @throws CpaAnnotationException any annotation inconsistence
     * @throws CpaRuntimeException    any error at runtime
     */
    public <E> E persist(E entity) throws CpaAnnotationException, CpaRuntimeException {
        Class clazz = entity.getClass();
        TypeParser type = this.cmisExec.getDocParser(entity.getClass());

        String id = type.getId(entity);
        String parentId = type.getParentId(entity);
        Map<String, ?> map = type.getProperties(entity);

        if (type.getBaseType().equals(BaseType.DOCUMENT)) {
            InputStream inputStream = type.getContent(entity);
            String encode = type.getEncode(entity);


            // this.persistDocument(map, id, encode, inputStream, )

        } else if (type.getBaseType().equals(BaseType.FOLDER)) {

        } else {

        }

        return entity;
    }

    /**
     * create a new document node
     *
     * @param prop   the properties
     * @param id     the id of parent
     * @param encode the encode of content
     * @param is     the content input stream
     * @param vs     the versioning state
     * @return String id of generated document
     * @throws Exception any erros that can occur during the new document creation
     */
    private String persistDocument(Map<String, ?> prop, String id, String encode, InputStream is, VersioningState vs)
            throws Exception {

        Session session = this.cmisExec.getSession();

        ObjectId objId = session.createObjectId(id);

        BigInteger size = new BigInteger(String.valueOf(is.available()));


        ContentStream cs = new ContentStreamImpl(
                (String) prop.get(PropertyIds.NAME),
                size,
                encode,
                is
        );

        return session.createDocument(prop, objId, cs, vs).getId();
    }


    /**
     * Convert the entity in a {@link Map}
     * <p>
     * Case the <b>cmisName</b> was {@link Boolean#TRUE} then the name that will be used at the map is the cmis name
     * like <i>cmis:name</i>
     *
     * @param entity   the entity to be converted
     * @param cmisName if is used cmis name or not
     * @param <E>      some element
     * @return Map the converted map
     * @throws CpaAnnotationException some errors that can be occur case the entity is not correctly mapped
     * @throws CpaRuntimeException    some errors that can occur during access field action
     */
    public <E> Map<String, Object> getProperties(E entity, boolean cmisName) throws CpaAnnotationException, CpaRuntimeException {
        Map<String, Object> map = new HashMap<>();
        Field fields[] = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(Metadata.class)) {
                    field.setAccessible(true);
                    Object obj = field.get(entity);

                    String name;
                    if (cmisName) {
                        Metadata metadata = field.getAnnotation(Metadata.class);
                        name = metadata.name();
                    } else {
                        name = field.getName();
                    }

                    map.put(name, obj);
                }
            } catch (IllegalAccessException e) {
                throw new CpaRuntimeException("Unable to perform get operation from " + field.getName(), e.getCause());
            }
        }

        return map;
    }
}
