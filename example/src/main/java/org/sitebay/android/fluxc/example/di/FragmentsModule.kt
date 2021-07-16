package org.sitebay.android.fluxc.example.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.sitebay.android.fluxc.example.AccountFragment
import org.sitebay.android.fluxc.example.CommentsFragment
import org.sitebay.android.fluxc.example.EditorThemeFragment
import org.sitebay.android.fluxc.example.ExperimentsFragment
import org.sitebay.android.fluxc.example.MainFragment
import org.sitebay.android.fluxc.example.MediaFragment
import org.sitebay.android.fluxc.example.NotificationsFragment
import org.sitebay.android.fluxc.example.PostsFragment
import org.sitebay.android.fluxc.example.ReactNativeFragment
import org.sitebay.android.fluxc.example.SignedOutActionsFragment
import org.sitebay.android.fluxc.example.SiteSelectorDialog
import org.sitebay.android.fluxc.example.SitesFragment
import org.sitebay.android.fluxc.example.TaxonomiesFragment
import org.sitebay.android.fluxc.example.ThemeFragment
import org.sitebay.android.fluxc.example.UploadsFragment
import org.sitebay.android.fluxc.example.ui.StoreSelectorDialog
import org.sitebay.android.fluxc.example.ui.WooCommerceFragment
import org.sitebay.android.fluxc.example.ui.customer.WooCustomersFragment
import org.sitebay.android.fluxc.example.ui.customer.creation.WooCustomerCreationFragment
import org.sitebay.android.fluxc.example.ui.customer.search.WooCustomersSearchFragment
import org.sitebay.android.fluxc.example.ui.gateways.WooGatewaysFragment
import org.sitebay.android.fluxc.example.ui.leaderboards.WooLeaderboardsFragment
import org.sitebay.android.fluxc.example.ui.orders.WooOrdersFragment
import org.sitebay.android.fluxc.example.ui.products.WooProductAttributeFragment
import org.sitebay.android.fluxc.example.ui.products.WooProductCategoriesFragment
import org.sitebay.android.fluxc.example.ui.products.WooProductFiltersFragment
import org.sitebay.android.fluxc.example.ui.products.WooProductTagsFragment
import org.sitebay.android.fluxc.example.ui.products.WooProductsFragment
import org.sitebay.android.fluxc.example.ui.products.WooUpdateProductFragment
import org.sitebay.android.fluxc.example.ui.products.WooUpdateVariationFragment
import org.sitebay.android.fluxc.example.ui.refunds.WooRefundsFragment
import org.sitebay.android.fluxc.example.ui.shippinglabels.WooShippingLabelFragment
import org.sitebay.android.fluxc.example.ui.shippinglabels.WooVerifyAddressFragment
import org.sitebay.android.fluxc.example.ui.stats.WooRevenueStatsFragment
import org.sitebay.android.fluxc.example.ui.stats.WooStatsFragment
import org.sitebay.android.fluxc.example.ui.taxes.WooTaxFragment

@Module
internal abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun provideMainFragmentInjector(): MainFragment

    @ContributesAndroidInjector
    abstract fun provideAccountFragmentInjector(): AccountFragment

    @ContributesAndroidInjector
    abstract fun provideCommentsFragmentInjector(): CommentsFragment

    @ContributesAndroidInjector
    abstract fun provideMediaFragmentInjector(): MediaFragment

    @ContributesAndroidInjector
    abstract fun providePostsFragmentInjector(): PostsFragment

    @ContributesAndroidInjector
    abstract fun provideSignedOutActionsFragmentInjector(): SignedOutActionsFragment

    @ContributesAndroidInjector
    abstract fun provideSitesFragmentInjector(): SitesFragment

    @ContributesAndroidInjector
    abstract fun provideTaxonomiesFragmentInjector(): TaxonomiesFragment

    @ContributesAndroidInjector
    abstract fun provideThemeFragmentInjector(): ThemeFragment

    @ContributesAndroidInjector
    abstract fun provideUploadsFragmentInjector(): UploadsFragment

    @ContributesAndroidInjector
    abstract fun provideWooCommerceFragmentInjector(): WooCommerceFragment

    @ContributesAndroidInjector
    abstract fun provideNotificationsFragmentInjector(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun provideWooStatsFragmentInjector(): WooStatsFragment

    @ContributesAndroidInjector
    abstract fun provideWooRevenueStatsFragmentInjector(): WooRevenueStatsFragment

    @ContributesAndroidInjector
    abstract fun provideWooProductsFragmentInjector(): WooProductsFragment

    @ContributesAndroidInjector
    abstract fun provideWooUpdateProductFragmentInjector(): WooUpdateProductFragment

    @ContributesAndroidInjector
    abstract fun provideWooUpdateVariationFragmentInjector(): WooUpdateVariationFragment

    @ContributesAndroidInjector
    abstract fun provideWooProductFiltersFragmentInjector(): WooProductFiltersFragment

    @ContributesAndroidInjector
    abstract fun provideWooProductCategoriesFragmentInjector(): WooProductCategoriesFragment

    @ContributesAndroidInjector
    abstract fun provideWooProductTagsFragmentInjector(): WooProductTagsFragment

    @ContributesAndroidInjector
    abstract fun provideWooOrdersFragmentInjector(): WooOrdersFragment

    @ContributesAndroidInjector
    abstract fun provideWooRefundsFragmentInjector(): WooRefundsFragment

    @ContributesAndroidInjector
    abstract fun provideWooGatewaysFragmentInjector(): WooGatewaysFragment

    @ContributesAndroidInjector
    abstract fun provideWooTaxFragmentInjector(): WooTaxFragment

    @ContributesAndroidInjector
    abstract fun provideWooShippingLabelFragmentInjector(): WooShippingLabelFragment

    @ContributesAndroidInjector
    abstract fun provideWooVerifyAddressFragment(): WooVerifyAddressFragment

    @ContributesAndroidInjector
    abstract fun provideWooLeaderboardsFragmentInjector(): WooLeaderboardsFragment

    @ContributesAndroidInjector
    abstract fun provideSiteSelectorDialogInjector(): SiteSelectorDialog

    @ContributesAndroidInjector
    abstract fun provideStoreSelectorDialogInjector(): StoreSelectorDialog

    @ContributesAndroidInjector
    abstract fun provideReactNativeFragmentInjector(): ReactNativeFragment

    @ContributesAndroidInjector
    abstract fun provideEditorThemeFragmentInjector(): EditorThemeFragment

    @ContributesAndroidInjector
    abstract fun provideExperimentsFragmentInjector(): ExperimentsFragment

    @ContributesAndroidInjector
    abstract fun provideWooProductAttributeFragmentInjector(): WooProductAttributeFragment

    @ContributesAndroidInjector
    abstract fun provideWooCustomersFragmentInjector(): WooCustomersFragment

    @ContributesAndroidInjector
    abstract fun provideWooCustomersSearchInjector(): WooCustomersSearchFragment

    @ContributesAndroidInjector
    abstract fun provideWooCustomerCreationFragment(): WooCustomerCreationFragment
}
