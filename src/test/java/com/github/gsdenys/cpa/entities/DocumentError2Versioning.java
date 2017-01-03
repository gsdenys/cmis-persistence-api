package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document", base = BaseType.DOCUMENT)
public class DocumentError2Versioning {

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

    @Versioning
    VersioningType versioningType;

    @Versioning
    VersioningType versioningType2;
}