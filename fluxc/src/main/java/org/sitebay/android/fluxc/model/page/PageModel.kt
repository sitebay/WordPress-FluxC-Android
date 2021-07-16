package org.sitebay.android.fluxc.model.page

import org.sitebay.android.fluxc.model.PostModel
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.util.DateTimeUtils
import java.util.Date

data class PageModel(
    val post: PostModel,
    val site: SiteModel,
    val pageId: Int,
    val title: String,
    var status: PageStatus,
    var date: Date,
    var hasLocalChanges: Boolean,
    val remoteId: Long,
    var parent: PageModel?,
    val featuredImageId: Long
) {
    constructor(post: PostModel, site: SiteModel, parent: PageModel? = null) : this(post, site, post.id, post.title,
            PageStatus.fromPost(post), Date(DateTimeUtils.timestampFromIso8601Millis(post.dateCreated)),
            post.isLocalDraft || post.isLocallyChanged, post.remotePostId, parent, post.featuredImageId)
}
