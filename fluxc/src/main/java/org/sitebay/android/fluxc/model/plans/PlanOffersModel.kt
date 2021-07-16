package org.sitebay.android.fluxc.model.plans

import android.annotation.SuppressLint
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
data class PlanOffersModel(
    var planIds: List<Int>?,
    var features: List<Feature>?,
    var name: String?,
    var shortName: String?,
    var tagline: String?,
    var description: String?,
    var iconUrl: String?,
    var costMonth: Int?,
    var costYear: Int?,
    var couponId: String?,
    var couponDiscount: Int?,
    var taxCode: String?,
    var taxRate: Double?,
    var currency: String?

) : Parcelable {
    @Parcelize
    @SuppressLint("ParcelCreator")
    data class Feature(
        var id: String?,
        var name: String?,
        var description: String?
    ) : Parcelable {
        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }

            if (other == null || other !is Feature) {
                return false
            }

            return id == other.id && name == other.name &&
                    description == other.description
        }

        override fun hashCode(): Int {
            var result = id?.hashCode() ?: 0
            result = 31 * result + (name?.hashCode() ?: 0)
            result = 31 * result + (description?.hashCode() ?: 0)
            return result
        }
    }

    fun calculateTax(interval: String): Int {
        var price = costMonth
        if (interval == "year") {
            price = costYear
        }
        val tax = price?.times((taxRate?.div(100)!!))?.toInt()
        Log.i("MYLOG TAX", tax.toString())
        return tax!!
    }

    fun calculateTaxFormatted(interval: String): String {
        val taxCost = calculateTax(interval)
        return formatPrice(taxCost, true)
    }

    fun getFormattedCost(interval: String, showCents: Boolean): String {
        var price = costMonth
        if (interval == "year") {
            price = costYear
        }
        return formatPrice(price!!, showCents)
    }

    fun getFormattedCostWithTax(interval: String): String {
        var price = costMonth
        if (interval == "year") {
            price = costYear
        }
        if (price != null) {
            price += calculateTax(interval)
        }
        return formatPrice(price!!, true)
    }

    fun formatPrice(price: Int, showCents: Boolean): String {
        var priceStr = price.toString()
        var first = priceStr.dropLast(2)
        var last = priceStr.takeLast(2)
        var formattedPrice = "$first.$last"
        var currSign = "$"
        if (last == "00") {
            formattedPrice = "$first"
        } else if (first == "") {
            formattedPrice = "0"
        }
        if (showCents) {
            formattedPrice = "$formattedPrice.00"
        }
        if (currency == "eur") {
            currSign = "€"
        }
        return currSign + formattedPrice
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is PlanOffersModel) {
            return false
        }

        return name == other.name && shortName == other.shortName &&
                tagline == other.tagline && description == other.description &&
                iconUrl == other.iconUrl && planIds == other.planIds && features == other.features
    }

    override fun hashCode(): Int {
        var result = planIds?.hashCode() ?: 0
        result = 31 * result + (features?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (shortName?.hashCode() ?: 0)
        result = 31 * result + (tagline?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (iconUrl?.hashCode() ?: 0)
        return result
    }
}
