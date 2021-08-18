package org.wordpress.android.fluxc.persistence.dao

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel.AddOnDisplay.Select
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel.AddOnPriceType.FlatFee
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel.AddOnRestrictionsType.Email
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel.AddOnTitleFormat.Heading
import org.wordpress.android.fluxc.model.addons.WCProductAddonModel.AddOnType.Checkbox
import org.wordpress.android.fluxc.network.rest.wpcom.wc.addons.dto.AddOnGroupDto
import org.wordpress.android.fluxc.persistence.WCAndroidDatabase
import org.wordpress.android.fluxc.persistence.entity.AddonEntity
import org.wordpress.android.fluxc.persistence.entity.AddonEntity.Display
import org.wordpress.android.fluxc.persistence.entity.AddonEntity.PriceType
import org.wordpress.android.fluxc.persistence.entity.AddonEntity.Restrictions
import org.wordpress.android.fluxc.persistence.entity.AddonEntity.TitleFormat
import org.wordpress.android.fluxc.persistence.entity.AddonEntity.Type
import org.wordpress.android.fluxc.persistence.entity.AddonOptionEntity
import org.wordpress.android.fluxc.persistence.entity.AddonWithOptions
import org.wordpress.android.fluxc.persistence.entity.GlobalAddonGroupEntity
import org.wordpress.android.fluxc.persistence.entity.GlobalAddonGroupWithAddons

@RunWith(RobolectricTestRunner::class)
internal class AddonsDaoTest {
    private lateinit var database: WCAndroidDatabase
    private lateinit var sut: AddonsDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(context, WCAndroidDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        sut = database.addonsDao()
    }

    @Test
    fun `save and retrieve global add-on`(): Unit = runBlocking {
        val expectedGlobalAddonGroupEntity = getSampleGlobalAddonGroupWithAddons(DB_GENERATED_ID_IN_FIRST_ITERATION)

        sut.cacheGroups(
                globalAddonGroups = listOf(TEST_GLOBAL_ADDON_GROUP_DTO),
                remoteSiteId = TEST_REMOTE_SITE_ID
        )

        val resultFromDatabase = sut.getGlobalAddonsForSite(TEST_REMOTE_SITE_ID).first()
        assertThat(resultFromDatabase).containsOnly(expectedGlobalAddonGroupEntity)
    }

    @Test
    fun `caching global addon groups doesn't duplicate entities`(): Unit = runBlocking {
        val expectedGlobalAddonGroupEntity = getSampleGlobalAddonGroupWithAddons(DB_GENERATED_ID_IN_SECOND_ITERATION)

        sut.cacheGroups(
                globalAddonGroups = listOf(TEST_GLOBAL_ADDON_GROUP_DTO),
                remoteSiteId = TEST_REMOTE_SITE_ID
        )
        sut.cacheGroups(
                globalAddonGroups = listOf(TEST_GLOBAL_ADDON_GROUP_DTO),
                remoteSiteId = TEST_REMOTE_SITE_ID
        )

        val resultFromDatabase = sut.getGlobalAddonsForSite(TEST_REMOTE_SITE_ID).first()
        assertThat(resultFromDatabase).containsOnly(expectedGlobalAddonGroupEntity)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private companion object {
        const val DB_GENERATED_ID_IN_FIRST_ITERATION = 1L
        const val DB_GENERATED_ID_IN_SECOND_ITERATION = 2L
        const val TEST_REMOTE_SITE_ID = 5L

        fun getSampleGlobalAddonGroupWithAddons(autoGeneratedId: Long): GlobalAddonGroupWithAddons {
            return GlobalAddonGroupWithAddons(
                    group = GlobalAddonGroupEntity(
                            globalGroupLocalId = autoGeneratedId,
                            remoteId = 5,
                            name = "Test Group",
                            restrictedCategoriesIds = emptyList(),
                            remoteSiteId = TEST_REMOTE_SITE_ID
                    ),
                    addons = listOf(
                            AddonWithOptions(
                                    addon = AddonEntity(
                                            addonLocalId = autoGeneratedId,
                                            globalGroupLocalId = autoGeneratedId,
                                            type = Type.Checkbox,
                                            display = Display.Select,
                                            name = "Test Addon name",
                                            titleFormat = TitleFormat.Heading,
                                            description = "Test",
                                            descriptionEnabled = true,
                                            required = false,
                                            position = 4,
                                            restrictions = Restrictions.Email,
                                            priceAdjusted = true,
                                            priceType = PriceType.FlatFee,
                                            price = "123",
                                            min = 10,
                                            max = 100
                                    ),
                                    options = listOf(
                                            AddonOptionEntity(
                                                    addonLocalId = autoGeneratedId,
                                                    addonOptionLocalId = autoGeneratedId,
                                                    priceType = PriceType.FlatFee,
                                                    label = "Test label",
                                                    price = "Test price",
                                                    image = null
                                            )
                                    )
                            )
                    )

            )
        }

        val TEST_GLOBAL_ADDON_GROUP_DTO = AddOnGroupDto(
                id = 5,
                name = "Test Group",
                categoryIds = null,
                addons = listOf(
                        WCProductAddonModel(
                                titleFormat = Heading,
                                description = "Test",
                                descriptionEnabled = 1,
                                restrictionsType = Email,
                                adjustPrice = 1,
                                priceType = FlatFee,
                                type = Checkbox,
                                display = Select,
                                name = "Test Addon name",
                                required = 0,
                                position = 4,
                                price = "123",
                                min = 10,
                                max = 100,
                                options = listOf(
                                        WCProductAddonModel.ProductAddonOption(
                                                priceType = FlatFee,
                                                label = "Test label",
                                                price = "Test price",
                                                image = null
                                        )
                                )
                        )
                )
        )
    }
}
