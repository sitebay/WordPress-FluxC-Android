package org.wordpress.android.fluxc.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StarterDesignModel(
    @SerializedName("blog_id") val blogId: Int?,
    val slug: String?,
    val title: String?,
    @SerializedName("site_url") val siteUrl: String?,
    @SerializedName("demo_url") val demoUrl: String?,
    val theme: String?,
    val screenshot: String?
) : Parcelable