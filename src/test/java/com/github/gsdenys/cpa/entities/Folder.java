package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:folder", base = BaseType.FOLDER)
public class Folder {

    @ID
    private String id;

    @Metadata(name = "cmis:name", mandatory = true)
    private String name;

    @Parent
    private String parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}