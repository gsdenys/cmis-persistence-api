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
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.TypeParser;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The persistence Executor
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class PersistExec extends AbstPersistExec {

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
     * @throws CpaAnnotationException any annotation inconsistence
     * @throws CpaRuntimeException    any error at runtime
     */
    public <E> void persist(E entity) throws CpaAnnotationException, CpaRuntimeException {
        TypeParser parser = this.cmisExec.getDocParser(entity.getClass());

        String id = parser.getId(entity);
        Session session = this.cmisExec.getSession();

        if (id == null) {
            this.create(entity, parser, session);
        } else {
            this.update(entity, parser, session);
        }
    }

    /**
     * refresh entity from the repository
     *
     * @param entity the entity
     * @param <E>    any element
     * @throws CpaAnnotationException any annotation inconsistence
     * @throws CpaRuntimeException    any error at runtime
     */
    public <E> void refresh(E entity) throws CpaAnnotationException, CpaRuntimeException {
        TypeParser parser = this.cmisExec.getDocParser(entity.getClass());

        if (parser.getId(entity) == null) {
            throw new CpaRuntimeException("Unable to refresh no persisted entity");
        }

        Map<String, ?> properties = new HashMap<>();

        //load CMIS object from repository
        Session session = this.cmisExec.getSession();
        ObjectId id = session.createObjectId(parser.getId(entity));
        CmisObject cmisObject = session.getObject(id);

        //convert property in a map
        List<Property<?>> propertyList = cmisObject.getProperties();
        for (Property<?> property : propertyList) {
            properties.put(property.getDisplayName(), property.getValue());
        }

        parser.refreshProperty(entity, properties);

        //refresh document - in case of existence of
        if (parser.getBaseType().equals(BaseType.DOCUMENT)) {
            Document document = (Document) cmisObject;
            ContentStream stream = document.getContentStream();

            if (stream != null) {
                InputStream is = stream.getStream();
                parser.setContent(entity, is);
            }
        }
    }

    /**
     * refresh entity from the repository
     *
     * @param entity      the entity
     * @param allVersions remove allVersion
     * @param <E>         any element
     * @throws CpaAnnotationException any annotation inconsistence
     * @throws CpaRuntimeException    any error at runtime
     */
    public <E> void remove(E entity, final boolean allVersions) throws CpaAnnotationException, CpaRuntimeException {
        TypeParser parser = this.cmisExec.getDocParser(entity.getClass());

        //load CMIS object from repository
        Session session = this.cmisExec.getSession();
        ObjectId id = session.createObjectId(parser.getId(entity));
        CmisObject cmisObject = session.getObject(id);

        cmisObject.delete(allVersions);
    }
}
