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
import com.gsdenys.cpa.operations.parser.EntityParser;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The executor responsible to perform actions to copy or move document
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class LocationExec {

    private CmisExec cmisExec;

    public LocationExec(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    public <E> void move(E entity) throws CpaRuntimeException, CpaAnnotationException {
        EntityParser parser = this.cmisExec.getEntityParser(entity.getClass());
        Session session = this.cmisExec.getSession();

        String id = parser.getId(entity);

        ObjectId objectId = session.createObjectId(id);
        ObjectId parentId = session.createObjectId(parser.getParentId(entity));

        CmisObject cmisObject = session.getObject(objectId);
        CmisObject parent = session.getObject(parentId);

        if (parser.getBaseType().equals(BaseType.DOCUMENT)) {
            Document document = (Document) cmisObject;
            ObjectId parentActual = document.getParents().get(0);
            if (!parentActual.equals(parent)) {
                document.move(parentActual, parent);
            }
        } else if (parser.getBaseType().equals(BaseType.FOLDER)) {
            Folder folder = (Folder) cmisObject;
            ObjectId parentActual = folder.getParents().get(0);
            if (!parentActual.equals(parent)) {
                folder.move(parentActual, parent);
            }
        } else {
            Item item = (Item) cmisObject;
            ObjectId parentActual = item.getParents().get(0);
            if (!parentActual.equals(parent)) {
                item.move(parentActual, parent);
            }
        }
    }

    /**
     * @param entity
     * @param <E>
     * @return
     * @throws CpaRuntimeException
     * @throws CpaAnnotationException
     */
    public <E> E copy(E entity, E dest) throws CpaRuntimeException, CpaAnnotationException {
        EntityParser parser = this.cmisExec.getEntityParser(entity.getClass());
        EntityParser parserDest = this.cmisExec.getEntityParser(dest.getClass());

        Session session = this.cmisExec.getSession();
        E entityCopy = null;

        ObjectId entityId = session.createObjectId(parser.getId(entity));
        ObjectId destId = session.createObjectId(parserDest.getId(dest));

        try {
            entityCopy = (E) entity.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (parser.getBaseType().equals(BaseType.DOCUMENT)) {
            Document document = (Document) session.getObject(entityId);
            Document doc = document.copy(destId);

            parser.setId(entityCopy, doc.getId());
            this.cmisExec.getPersistExec().refresh(entityCopy);

            return entity;
        }

        if (parser.getBaseType().equals(BaseType.FOLDER)) {
            Folder origin = (Folder) session.getObject(entityId);
            Folder target = (Folder) session.getObject(destId);

            Folder folder = this.copyFolder(origin, target, session);

            parserDest.setId(entityCopy, folder.getId());
            this.cmisExec.getPersistExec().refresh(entity);

            return entity;
        }

        if (parser.getBaseType().equals(BaseType.ITEM)) {
            Item item = (Item) session.getObject(entityId);
            Folder target = (Folder) session.getObject(destId);

            Map<String, Object> prop = new HashMap<>();

            for (Property p : item.getProperties()) {
                prop.put(p.getDisplayName(), p.getValue());
            }

            Item iRet = target.createItem(prop);
            parserDest.setId(entityCopy, iRet.getId());
            this.cmisExec.getPersistExec().refresh(entityCopy);

            return entity;
        }

        return null;
    }


    private Folder copyFolder(Folder origin, Folder dest, Session session) {
        Map<String, ?> properties = new HashMap<>();
        for (Property<?> prop : origin.getProperties()) {
            properties.put(prop.getDisplayName(), prop.getValue());
        }
        properties.remove(PropertyIds.OBJECT_ID);

        //create new folder with all properties of the origin
        Folder newFolder = dest.createFolder(properties);

        //iterate over all origin children
        Iterator<CmisObject> it = origin.getChildren().iterator();
        while (it.hasNext()) {
            CmisObject object = it.next();
            if (object instanceof Document) {
                Document document = (Document) object;
                document.copy(newFolder);
            } else if (object instanceof Folder) {
                Folder folder = (Folder) object;
                this.copyFolder(folder, newFolder, session);
            } else if (object instanceof Item) {
                Item item = (Item) object;
                //TODO copy Item
            }
        }

        return newFolder;
    }
}
