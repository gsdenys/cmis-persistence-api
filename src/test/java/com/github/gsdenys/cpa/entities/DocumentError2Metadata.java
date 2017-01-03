package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document")
public class DocumentError2Metadata {

    @ID
    String id;

    @Parent
    String parent;


    @Metadata(name = "cmis:name", mandatory = true)
    String name;

    @Metadata(name = "cmis:name", mandatory = true)
    String name2;

    @Content
    InputStream content;
}