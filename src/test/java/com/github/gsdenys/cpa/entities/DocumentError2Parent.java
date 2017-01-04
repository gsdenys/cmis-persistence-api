package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentError2Parent {

    @ID
    private String id;

    @Parent
    private String parent;

    @Parent
    private String parent2;

    @Metadata(name = "cmis:name", mandatory = true)
    private String name;

    @Content
    private InputStream content;

    @Encode
    private String encode;

    @Versioning
    private VersioningType versioningType;
}