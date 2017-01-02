package com.gsdenys.cpa;

import com.gsdenys.cpa.annotations.*;

@Entity(base = BaseType.FOLDER, name = "cmis:folder")
public class RootFolder {
    @ID
    private String id;

    @Parent
    private String parent;

    @Metadata(name = "cmis:name")
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
