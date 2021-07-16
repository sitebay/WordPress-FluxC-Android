package org.sitebay.android.fluxc.endpoints;

import org.junit.Test;
import org.sitebay.android.fluxc.generated.endpoint.WPCOMV2;

import static org.junit.Assert.assertEquals;

public class WPComV2EndpointTest {
    @Test
    public void testAllEndpoints() {
        // Users
        assertEquals("/users/username/suggestions/", WPCOMV2.users.username.suggestions.getEndpoint());
        assertEquals("/plugins/featured/", WPCOMV2.plugins.featured.getEndpoint());
    }

    @Test
    public void testUrls() {
        assertEquals("https://mytest.sitebay.org/api/users/username/suggestions/",
                WPCOMV2.users.username.suggestions.getUrl());
    }
}
