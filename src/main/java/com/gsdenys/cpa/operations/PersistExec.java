package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.annotations.ID;
import com.gsdenys.cpa.annotations.Metadata;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.persistence.EntityManager;
import com.sun.istack.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gsdenys on 29/12/16.
 */
public class PersistExec {

    private CmisExec cmisExec;

    /**
     * Default Builder
     *
     * @param exec
     */
    public PersistExec(CmisExec exec) {
        this.cmisExec = exec;
    }


    /**
     * Persist entity at the repository
     *
     * @param entity
     * @param <E>
     * @return
     */
    public <E> E persist(E entity) {
        String id = null;
        String parentId = null;


        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields){



        }

        //TODO implementar

        return entity;
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
    public <E> Map<String, Object> getProperties(@NotNull E entity, Boolean cmisName) throws CpaAnnotationException, CpaRuntimeException {
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
