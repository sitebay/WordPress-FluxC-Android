package org.wordpress.android.fluxc.release

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wordpress.android.fluxc.store.ReactNativeFetchResponse.Success
import org.wordpress.android.fluxc.store.ReactNativeFetchResponse.Error
import org.wordpress.android.fluxc.store.ReactNativeStore
import javax.inject.Inject

class ReleaseStack_ReactNativeWPComRequestTest : ReleaseStack_WPComBase() {
    @Inject lateinit var reactNativeStore: ReactNativeStore

    override fun setUp() {
        super.setUp()
        mReleaseStackAppComponent.inject(this)
        init()
    }

    @Test
    fun testWpComCall() {
        val url = "https://public-api.wordpress.com/wp/v2/sites/${sSite.siteId}/media"
        val params = mapOf("context" to "edit")
        val response = runBlocking { reactNativeStore.performWPComRequest(url, params) }

        val failureMessage = "Call failed with error: ${(response as? Error)?.error}"
        assertTrue(failureMessage, response is Success)
    }

    @Test
    fun testWpComCall_fails() {
        val url = "https://public-api.wordpress.com/wp/v2/sites/${sSite.siteId}/an-invalid-extension"
        val response = runBlocking { reactNativeStore.performWPComRequest(url, emptyMap()) }

        val assertionMessage = "Call should have failed with a 404, instead response was $response"
        val actualStatusCode = (response as? Error)?.error?.volleyError?.networkResponse?.statusCode
        Assert.assertEquals(assertionMessage, actualStatusCode, 404)
    }
}