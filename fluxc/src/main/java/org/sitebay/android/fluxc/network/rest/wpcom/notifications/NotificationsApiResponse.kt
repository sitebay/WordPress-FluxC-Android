package org.sitebay.android.fluxc.network.rest.wpcom.notifications

import org.sitebay.android.fluxc.network.Response

class NotificationsApiResponse : Response {
    val last_seen_time: Long? = null
    val number: Int? = null
    val notes: List<NotificationApiResponse>? = null
}
