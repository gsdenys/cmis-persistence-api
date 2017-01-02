package com.gsdenys.cpa.entities;

import com.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentError2Encode {

    @ID
    String id;

    @Parent
    String parent;

    @Metadata(name = "cmis:name", mandatory = true)
    String name;

    @Content
    InputStream content;

    @Encode
    String encode;

    @Encode
    String encode2;

    @Versioning
    VersioningType versioningType;
}