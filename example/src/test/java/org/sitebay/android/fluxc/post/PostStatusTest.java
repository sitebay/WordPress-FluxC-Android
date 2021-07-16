package org.sitebay.android.fluxc.post;

import org.junit.Test;
import org.sitebay.android.fluxc.model.PostModel;
import org.sitebay.android.fluxc.model.post.PostStatus;
import org.sitebay.android.util.DateTimeUtils;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PostStatusTest {
    @Test
    public void testPostStatusFromPost() {
        PostModel post = new PostModel();
        post.setStatus("publish");

        // Test published post with past date
        post.setDateCreated(DateTimeUtils.iso8601UTCFromDate(new Date()));
        assertEquals(PostStatus.PUBLISHED, PostStatus.fromPost(post));

        // Test "published" post with future date
        post.setDateCreated(DateTimeUtils.iso8601UTCFromDate(new Date(System.currentTimeMillis() + 500000)));
        assertEquals(PostStatus.SCHEDULED, PostStatus.fromPost(post));
    }

    @Test
    public void testPostStatusFromPostWithNoDateCreated() {
        PostModel post = new PostModel();
        post.setStatus("publish");

        assertEquals(PostStatus.PUBLISHED, PostStatus.fromPost(post));
    }
}
