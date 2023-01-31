package org.wordpress.android.fluxc.example

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_domains.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.coroutines.launch
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.example.ui.StoreSelectingFragment
import org.wordpress.android.fluxc.store.SiteStore
import javax.inject.Inject

class DomainsFragment : StoreSelectingFragment() {
    @Inject internal lateinit var dispatcher: Dispatcher
    @Inject internal lateinit var store: SiteStore

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_domains, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetch_domains.setOnClickListener {
            selectedSite?.let { site ->
                lifecycleScope.launch {
                    val result = store.fetchSiteDomains(site)
                    when {
                        result.isError -> {
                            prependToLog("Error fetching plans: ${result.error.type}")
                        }
                        else -> {
                            val plans = result.domains
                                ?.joinToString(separator = "\n") {
                                    "${it.domain} (Renewal: ${it.autoRenewalDate}), primary: ${it.primaryDomain}"
                                }
                            prependToLog("Domains:\n$plans")
                        }
                    }
                }
            } ?: prependToLog("Select a site first")
        }
    }
}
