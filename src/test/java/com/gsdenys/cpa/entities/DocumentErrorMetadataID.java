package com.gsdenys.cpa.entities;

import com.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Type(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentErrorMetadataID {

    @ID
    String id;

    @Parent
    String parent;

    @ID
    @Metadata(name = "cmis:name", mandatory = true)
    String name;

    @Content
    InputStream content;

    @Encode
    String encode;

    @Versioning
    VersioningType versioningType;
}