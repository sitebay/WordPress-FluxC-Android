package org.sitebay.android.fluxc.model.stats

import org.sitebay.android.fluxc.store.StatsStore.InsightType

data class InsightTypeDataModel(val type: InsightType, val status: Status, val position: Int?) {
    enum class Status {
        ADDED, REMOVED, NEW
    }
}
