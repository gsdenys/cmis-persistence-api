package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.Content;
import com.github.gsdenys.cpa.annotations.ID;
import com.github.gsdenys.cpa.annotations.Metadata;
import com.github.gsdenys.cpa.annotations.Parent;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
public class DocumentErrorNoAnnotationEntity {

    @ID
    private String id;

    @Parent
    private String parent;

    @Metadata(name = "cmis:name", mandatory = true)
    private String name;

    @Content
    private InputStream content;


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

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentErrorNoAnnotationEntity document = (DocumentErrorNoAnnotationEntity) o;

        if (id != null ? !id.equals(document.id) : document.id != null) return false;
        if (parent != null ? !parent.equals(document.parent) : document.parent != null) return false;
        return name != null ? name.equals(document.name) : document.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}