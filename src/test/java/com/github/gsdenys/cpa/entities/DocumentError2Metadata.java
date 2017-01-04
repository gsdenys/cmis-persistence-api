package com.github.gsdenys.cpa.entities;

import com.github.gsdenys.cpa.annotations.*;

import java.io.InputStream;

/**
 * Created by gsdenys on 30/12/16.
 */
@Entity(name = "cmis:document")
public class DocumentError2Metadata {

    @ID
    private String id;

    @Parent
    private String parent;


    @Metadata(name = "cmis:name", mandatory = true)
    private String name;

    @Metadata(name = "cmis:name", mandatory = true)
    private String name2;

    @Content
    private InputStream content;
}