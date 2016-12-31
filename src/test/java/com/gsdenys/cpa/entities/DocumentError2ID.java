package com.gsdenys.cpa.entities;

import com.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Type(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentError2ID {

    @ID
    String id;

    @ID
    String id2;

    @Parent
    String parent;

    @Metadata(name = "cmis:name", mandatory = true)
    String name;

    @Content
    InputStream content;

    @Encode
    String encode;

    @Versioning
    VersioningType versioningType;
}