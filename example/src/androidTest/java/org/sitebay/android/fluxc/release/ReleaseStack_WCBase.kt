package org.sitebay.android.fluxc.release

import com.wellsql.generated.SiteModelTable
import org.sitebay.android.fluxc.example.BuildConfig
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.persistence.SiteSqlUtils
import org.sitebay.android.fluxc.store.AccountStore.AuthenticatePayload

open class ReleaseStack_WCBase : ReleaseStack_WPComBase() {
    private val siteSqlUtils = SiteSqlUtils()
    private val authenticatePayload by lazy {
        AuthenticatePayload(BuildConfig.TEST_WPCOM_USERNAME_WOO_JETPACK,
                BuildConfig.TEST_WPCOM_PASSWORD_WOO_JETPACK)
    }

    override fun getSiteFromDb(): SiteModel {
        val wcSites = siteSqlUtils.getSitesWith(SiteModelTable.HAS_WOO_COMMERCE, true).asModel
        if (wcSites.isEmpty()) {
            throw AssertionError("This test account doesn't seem to have any WooCommerce sites!")
        } else {
            return wcSites[0]
        }
    }

    override fun buildAuthenticatePayload() = authenticatePayload
}
