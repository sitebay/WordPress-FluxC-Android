package org.sitebay.android.fluxc.release

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sitebay.android.fluxc.example.BuildConfig
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.store.AccountStore.AuthenticatePayload
import org.sitebay.android.fluxc.store.XPostsResult
import org.sitebay.android.fluxc.store.XPostsSource
import org.sitebay.android.fluxc.store.XPostsStore
import javax.inject.Inject

class ReleaseStack_XPostsTest : ReleaseStack_WPComBase() {
    @Inject lateinit var xPostsStore: XPostsStore

    override fun buildAuthenticatePayload(): AuthenticatePayload =
            AuthenticatePayload(BuildConfig.TEST_WPCOM_USERNAME_XPOSTS, BuildConfig.TEST_WPCOM_PASSWORD_XPOSTS)

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        mReleaseStackAppComponent.inject(this)
        init()
    }

    @Test
    fun makesSuccessfulCallReturningXpost() {
        val response = runBlocking {
            val site = SiteModel().apply { siteId = siteFromDb.siteId }
            xPostsStore.fetchXPosts(site)
        }

        assertTrue(response is XPostsResult.Result)
        assertEquals(XPostsSource.REST_API, (response as XPostsResult.Result).source)
        assertTrue(response.xPosts.isNotEmpty())
    }
}
