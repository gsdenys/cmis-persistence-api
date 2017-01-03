package com.github.gsdenys.cpa.operations.parser;

import com.github.gsdenys.cpa.exception.CpaRuntimeException;

import java.lang.reflect.Field;

public class FieldChecker {
    private boolean id;
    private boolean parent;
    private boolean content;
    private boolean versioning;
    private boolean metadata;
    private boolean encode;

    public boolean isId() {
        return id;
    }

    public boolean isParent() {
        return parent;
    }

    public boolean isContent() {
        return content;
    }

    public boolean isVersioning() {
        return versioning;
    }

    public boolean isMetadata() {
        return metadata;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public void setContent(boolean content) {
        this.content = content;
    }

    public void setVersioning(boolean versioning) {
        this.versioning = versioning;
    }

    public void setMetadata(boolean metadata) {
        this.metadata = metadata;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    /**
     * check by the existence of more than one field set with <code>true</code>
     *
     * @return <code>true</code> if have more than one true
     * @throws CpaRuntimeException any exception that occur during field access
     */
    public boolean multipleChecker() throws CpaRuntimeException {
        boolean flag = false;

        Field[] fields = FieldChecker.class.getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);

            try {
                Class t = field.getType();
                Object v = field.get(this);

                boolean value = (t == boolean.class && Boolean.TRUE.equals(v));
                if (flag && value) {
                    return true;
                }

                if(value) {
                    flag = value;
                }
            } catch (IllegalAccessException e) {
                throw new CpaRuntimeException(
                        "Error when try to access some field",
                        e.getCause()
                );
            }
        }

        return false;
    }
}
