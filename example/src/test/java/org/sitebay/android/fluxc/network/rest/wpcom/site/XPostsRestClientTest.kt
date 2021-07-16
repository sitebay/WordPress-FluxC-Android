package org.sitebay.android.fluxc.network.rest.wpcom.site

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.sitebay.android.fluxc.generated.endpoint.WPCOMV2
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.XPostSiteModel
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.test
import kotlin.test.assertEquals

internal class XPostsRestClientTest {
    private lateinit var subject: XPostsRestClient
    private val wpComGsonRequestBuilder = mock<WPComGsonRequestBuilder>()

    @Before
    fun setup() {
        subject = XPostsRestClient(wpComGsonRequestBuilder, mock(), mock(), mock(), mock(), mock())
    }

    @Test
    fun `fetch performs request`() = test {
        val site = SiteModel().apply { siteId = 123 }
        val expected = mock<WPComGsonRequestBuilder.Response<Array<XPostSiteModel>>>()

        val url = WPCOMV2.sites.site(site.siteId).xposts.url
        whenever(
                wpComGsonRequestBuilder.syncGetRequest(
                        subject,
                        url,
                        mapOf("decode_html" to "true"),
                        Array<XPostSiteModel>::class.java,
                        true,
                        60000
                )
        )
                .thenReturn(expected)

        val actual = subject.fetch(site)
        assertEquals(expected, actual)
    }
}
