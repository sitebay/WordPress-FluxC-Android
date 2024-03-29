package org.wordpress.android.fluxc.network.rest.wpcom.mobilepay

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.generated.endpoint.WPCOMV2
import org.wordpress.android.fluxc.network.BaseRequest
import org.wordpress.android.fluxc.network.UserAgent
import org.wordpress.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequest
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response
import org.wordpress.android.fluxc.network.rest.wpcom.auth.AccessToken
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MobilePayRestClient @Inject constructor(
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    dispatcher: Dispatcher,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    @Suppress("LongParameterList")
    suspend fun createOrder(
        productIdentifier: String,
        priceInCents: Int,
        currency: String,
        purchaseToken: String,
        appId: String,
        siteId: Long,
        customBaseUrl: String?,
    ): CreateOrderResponse {
        val url = if (customBaseUrl == null) {
            WPCOMV2.iap.orders.url
        } else {
            "$customBaseUrl/wpcom/v2${WPCOMV2.iap.orders.endpoint}"
        }
        val response = wpComGsonRequestBuilder.syncPostRequest(
            restClient = this,
            url = url,
            params = null,
            body = mapOf(
                "site_id" to siteId,
                "product_id" to productIdentifier,
                "price" to priceInCents,
                "currency" to currency,
                "purchase_token" to purchaseToken,
            ),
            clazz = CreateOrderResponseType::class.java,
            retryPolicy = DefaultRetryPolicy(TIMEOUT, 0, 1f),
            headers = mapOf(APP_ID_HEADER to appId)
        )
        return when (response) {
            is Response.Success -> CreateOrderResponse.Success(response.data.orderId)
            is Response.Error -> CreateOrderResponse.Error(
                response.error.toCreateOrderError(),
                response.error.message
            )
        }
    }

    data class CreateOrderResponseType(
        val orderId: Long
    )

    sealed class CreateOrderResponse {
        data class Success(val orderId: Long) : CreateOrderResponse()
        data class Error(
            val type: CreateOrderErrorType,
            val message: String? = null
        ) : CreateOrderResponse()
    }

    enum class CreateOrderErrorType {
        API_ERROR,
        AUTH_ERROR,
        GENERIC_ERROR,
        INVALID_RESPONSE,
        TIMEOUT,
        NETWORK_ERROR
    }

    private fun WPComGsonRequest.WPComGsonNetworkError.toCreateOrderError() =
        when (type) {
            BaseRequest.GenericErrorType.TIMEOUT -> CreateOrderErrorType.TIMEOUT
            BaseRequest.GenericErrorType.NO_CONNECTION,
            BaseRequest.GenericErrorType.INVALID_SSL_CERTIFICATE,
            BaseRequest.GenericErrorType.NETWORK_ERROR -> CreateOrderErrorType.NETWORK_ERROR
            BaseRequest.GenericErrorType.SERVER_ERROR -> CreateOrderErrorType.API_ERROR
            BaseRequest.GenericErrorType.PARSE_ERROR,
            BaseRequest.GenericErrorType.NOT_FOUND,
            BaseRequest.GenericErrorType.CENSORED,
            BaseRequest.GenericErrorType.INVALID_RESPONSE -> CreateOrderErrorType.INVALID_RESPONSE
            BaseRequest.GenericErrorType.HTTP_AUTH_ERROR,
            BaseRequest.GenericErrorType.AUTHORIZATION_REQUIRED,
            BaseRequest.GenericErrorType.NOT_AUTHENTICATED -> CreateOrderErrorType.AUTH_ERROR
            BaseRequest.GenericErrorType.UNKNOWN -> CreateOrderErrorType.GENERIC_ERROR
            null -> CreateOrderErrorType.GENERIC_ERROR
        }

    companion object {
        private const val APP_ID_HEADER = "X-APP-ID"
        private const val TIMEOUT = 120_000
    }
}
