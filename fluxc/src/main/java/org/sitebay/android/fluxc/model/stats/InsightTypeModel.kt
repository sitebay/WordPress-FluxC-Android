package org.sitebay.android.fluxc.model.stats

import org.sitebay.android.fluxc.store.StatsStore.InsightType

data class InsightTypeModel(val addedTypes: List<InsightType>, val removedTypes: List<InsightType>)
