package org.sitebay.android.fluxc.network.rest.wpcom.vertical

import android.content.Context
import com.android.volley.RequestQueue
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WPCOMV2
import org.sitebay.android.fluxc.model.vertical.VerticalSegmentModel
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Error
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Success
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.sitebay.android.fluxc.store.VerticalStore.FetchSegmentsError
import org.sitebay.android.fluxc.store.VerticalStore.FetchedSegmentsPayload
import org.sitebay.android.fluxc.store.VerticalStore.VerticalErrorType.GENERIC_ERROR
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private class FetchSegmentsResponse : ArrayList<VerticalSegmentModel>()

@Singleton
class VerticalRestClient @Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchSegments(): FetchedSegmentsPayload {
        val url = WPCOMV2.segments.url
        val response = wpComGsonRequestBuilder.syncGetRequest(this, url, emptyMap(), FetchSegmentsResponse::class.java)
        return when (response) {
            is Success -> FetchedSegmentsPayload(response.data)
            is Error -> FetchedSegmentsPayload(FetchSegmentsError(GENERIC_ERROR))
        }
    }
}
