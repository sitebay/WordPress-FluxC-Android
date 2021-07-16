package org.sitebay.android.fluxc.model

import org.sitebay.android.fluxc.model.SiteHomepageSettings.ShowOnFront.PAGE
import org.sitebay.android.fluxc.model.SiteHomepageSettings.ShowOnFront.POSTS

sealed class SiteHomepageSettings(val showOnFront: ShowOnFront) {
    data class StaticPage(val pageForPostsId: Long, val pageOnFrontId: Long) : SiteHomepageSettings(PAGE)
    object Posts : SiteHomepageSettings(POSTS)

    enum class ShowOnFront(val value: String) {
        PAGE("page"), POSTS("posts")
    }
}
