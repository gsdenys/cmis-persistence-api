package com.gsdenys.cpa.entities;

import com.gsdenys.cpa.annotations.*;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:folder", base = BaseType.FOLDER)
public class Folder {

    @ID
    String id;

    @Metadata(name = "cmis:folder", mandatory = true)
    String name;

    @Parent
    String parent;

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