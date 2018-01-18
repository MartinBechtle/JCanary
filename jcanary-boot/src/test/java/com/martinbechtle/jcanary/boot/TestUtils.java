package com.martinbechtle.jcanary.boot;

import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Martin Bechtle
 */
public class TestUtils {

    public static ResultMatcher responseBodyEqualsJson(String samePackageResourceName,
                                                       Class<?> klazz,
                                                       Customization...customizations)
            throws IOException {

        return result -> {

            String actual = result.getResponse().getContentAsString();

            try (InputStream resource = klazz.getResourceAsStream(samePackageResourceName)) {

                if (resource == null) {
                    throw new IOException("Couldn't find " + samePackageResourceName +
                            " in " + klazz.getPackage().getName());
                }
                String expected = IOUtils.toString(resource, Charset.forName("UTF-8"));

                JSONAssert.assertEquals(
                        expected,
                        actual,
                        new CustomComparator(JSONCompareMode.STRICT, customizations));
            }
        };
    }
}
