package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentFullConfigured {

    @ID
    private String id;

    @Parent
    private String parent;

    @Metadata(name = "cmis:name", mandatory = true)
    private String name;

    @Content
    private InputStream content;

    @Encode
    private String encode;

    @Versioning
    private VersioningType versioningType;

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public VersioningType getVersioningType() {
        return versioningType;
    }

    public void setVersioningType(VersioningType versioningType) {
        this.versioningType = versioningType;
    }

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
}