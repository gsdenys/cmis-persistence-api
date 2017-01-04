package com.github.gsdenys.cpa.operations.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by gsdenys on 31/12/16.
 */
public class FieldCheckerTest {
    @Test
    public void multipleChecker() throws Exception {
        FieldChecker fieldChecker = new FieldChecker();
        fieldChecker.multipleChecker();
    }

    @Test
    public void isMetadata() throws Exception {
        FieldChecker checker = new FieldChecker();

        Assert.assertFalse("The isMetadata should return false", checker.isMetadata());

        checker.setMetadata(true);

        Assert.assertTrue("The Metadata should return true", checker.isMetadata());
    }
}