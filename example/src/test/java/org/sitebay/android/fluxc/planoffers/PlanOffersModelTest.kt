package org.sitebay.android.fluxc.planoffers

import org.junit.Test
import org.sitebay.android.fluxc.network.rest.wpcom.planoffers.PLAN_OFFER_MODELS
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PlanOffersModelTest {
    @Test
    fun testPlanOffersEquals() {
        val samplePlanOffersModel1 = PLAN_OFFER_MODELS[0]
        val samplePlanOffersModel2 = PLAN_OFFER_MODELS[0].copy()

        assertEquals(samplePlanOffersModel1, samplePlanOffersModel2)
        assertEquals(samplePlanOffersModel1.hashCode(), samplePlanOffersModel2.hashCode())

        samplePlanOffersModel2.description = "mismatched description"
        assertNotEquals(samplePlanOffersModel1, samplePlanOffersModel2)
        assertNotEquals(samplePlanOffersModel1.hashCode(), samplePlanOffersModel2.hashCode())
    }
}
