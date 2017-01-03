/*
 * Copyright 2016 CMIS Persistence API
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gsdenys.cpa.annotations;

import org.apache.chemistry.opencmis.commons.enums.VersioningState;

/**
 * Enumeration that represents a Versioning.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
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
