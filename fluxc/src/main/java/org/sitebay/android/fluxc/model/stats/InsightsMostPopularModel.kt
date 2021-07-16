package org.sitebay.android.fluxc.model.stats

data class InsightsMostPopularModel(
    val siteId: Long,
    val highestDayOfWeek: Int,
    val highestHour: Int,
    val highestDayPercent: Double,
    val highestHourPercent: Double
)
