package com.disney.studio.cucumber.slices.plugin.assemble

import org.junit.Test


class TestFeatureFileAssembler {

    /*
    Passing a malformed tag to the constructor should throw an AssertionError that reads as follows:
    "Missing '@' character!!! One or more of the supplied Cucumber tags, '[test-tag]', is not properly formatted in the POM file"
    */
    @Test(expected = AssertionError)
    void shouldFailDueToMalformedTag() {
        new FeatureFileAssembler(['test-tag'])
    }

    @Test
    void shouldPassWithProperlyFormattedTag() {
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(['@test-tag'])
        assert featureFileAssembler instanceof FeatureFileAssembler
    }
}
