package org.wordpress.android.fluxc.endpoints;

import org.junit.Test;
import org.wordpress.android.fluxc.generated.endpoint.WPAPI;

import static org.junit.Assert.assertEquals;

public class WPAPIEndpointTest {
    @Test
    public void testAllEndpoints() {
        // Posts
        assertEquals("/posts/", WPAPI.posts.getEndpoint());
        assertEquals("/posts/56/", WPAPI.posts.id(56).getEndpoint());

        // Pages
        assertEquals("/pages/", WPAPI.pages.getEndpoint());
        assertEquals("/pages/56/", WPAPI.pages.id(56).getEndpoint());

        // Media
        assertEquals("/media/", WPAPI.media.getEndpoint());
        assertEquals("/media/56/", WPAPI.media.id(56).getEndpoint());

        // Comments
        assertEquals("/comments/", WPAPI.comments.getEndpoint());
        assertEquals("/comments/56/", WPAPI.comments.id(56).getEndpoint());

        // Settings
        assertEquals("/settings/", WPAPI.settings.getEndpoint());

        // Users
        assertEquals("/users/", WPAPI.users.getEndpoint());
        assertEquals("/users/me/", WPAPI.users.me.getEndpoint());
    }

    @Test
    public void testUrls() {
        assertEquals("api/posts/", WPAPI.posts.getUrlV2());
        assertEquals("api/pages/", WPAPI.pages.getUrlV2());
        assertEquals("api/media/", WPAPI.media.getUrlV2());
        assertEquals("api/comments/", WPAPI.comments.getUrlV2());
        assertEquals("api/users/", WPAPI.users.getUrlV2());
        assertEquals("api/users/me/", WPAPI.users.me.getUrlV2());
    }
}
