package com.gsdenys.cpa.annotations;

import org.apache.chemistry.opencmis.commons.enums.VersioningState;

/**
 * Created by gsdenys on 30/12/16.
 */
public enum VersioningType {
    MAJOR(VersioningState.MAJOR),
    MINOR(VersioningState.MINOR),
    NONE(VersioningState.NONE);

    private VersioningState version;

    VersioningType(VersioningState version) {
        this.version = version;
    }

    public VersioningState getVersion() {
        return this.version;
    }
}
