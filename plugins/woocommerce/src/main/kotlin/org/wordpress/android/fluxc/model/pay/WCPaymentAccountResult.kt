package org.wordpress.android.fluxc.model.pay

import com.google.gson.annotations.SerializedName
import java.util.Date

data class WCPaymentAccountResult(
    val status: WCPayAccountStatusEnum,
    @SerializedName("has_pending_requirements")
    val hasPendingRequirements: Boolean,
    @SerializedName("has_overdue_requirements")
    val hasOverdueRequirements: Boolean,
    @SerializedName("current_deadline")
    val currentDeadline: Date?,
    /**
     * An alphanumeric string set by the merchant, e.g. `MYSTORE.COM`
     * See https://stripe.com/docs/statement-descriptors
     */
    @SerializedName("statement_descriptor")
    val statementDescriptor: String,
    @SerializedName("store_currencies")
    val storeCurrencies: StoreCurrencies,

    /**
     * A two character country code, e.g. `US`
     * See https://stripe.com/docs/api/accounts/object#account_object-country
     */
    @SerializedName("country")
    val country: String,
    /**
     * A boolean flag indicating if this Account is eligible for card present payments
     */
    @SerializedName("card_present_eligible")
    val isCardPresentEligible: Boolean
) {
    /**
     * Represents all of the possible Site Plugin Statuses in enum form
     */
    enum class WCPayAccountStatusEnum {
        /**
         * This is the normal state for a fully functioning WCPay account. The merchant should be able to collect
         * card present payments.
         */
        @SerializedName("complete")
        COMPLETE,

        /**
         * This state occurs when there is required business information missing from the account.
         * If `hasOverdueRequirements` is also true, then the deadline for providing that information HAS PASSED and
         * the merchant will probably NOT be able to collect card present payments.
         * Otherwise, if `hasPendingRequirements` is true, then the deadline for providing that information has not yet passed.
         * The deadline is available in `currentDeadline` and the merchant will probably be able to collect card present payments
         * until the deadline.
         * Otherwise, if neither `hasOverdueRequirements` nor `hasPendingRequirements` is true, then the account is under
         * review by Stripe and the merchant will probably not be able to collect card present payments.
         */
        @SerializedName("restricted")
        RESTRICTED,

        /**
         * This state occurs when our payment processor rejects the merchant account due to suspected fraudulent
         * activity. The merchant will NOT be able to collect card present payments.
         */
        @SerializedName("rejectedFraud")
        REJECTED_FRAUD,

        /** This state occurs when our payment processor rejects the merchant account due to terms of
         * service violation(s). The merchant will NOT be able to collect card present payments.
         */
        @SerializedName("rejectedTermsOfService")
        REJECTED_TERMS_OF_SERVICE,

        /**
         * This state occurs when our payment processor rejects the merchant account due to sanctions/being
        on a watch list. The merchant will NOT be able to collect card present payments.
         */
        @SerializedName("rejectedListed")
        REJECTED_LISTED,

        /**
         * This state occurs when our payment processor rejects the merchant account due to any other reason.
         * The merchant will NOT be able to collect card present payments.
         */
        @SerializedName("rejectedOther")
        REJECTED_OTHER,

        /**
         * This state occurs when the merchant hasn't  on-boarded yet. The merchant will NOT be able to
         * collect card present payments.
         */
        @SerializedName("rejectedOther")
        NO_ACCOUNT,

        /**
         * This state occurs when the self-hosted site responded in a way we don't recognize.
         */
        @SerializedName("unknown")
        UNKNOWN
    }

    data class StoreCurrencies(
        /**
         * A three character lowercase currency code, e.g. `usd`
         * See https://stripe.com/docs/api/accounts/object#account_object-default_currency
         */

        @SerializedName("default")
        val default: String,
        @SerializedName("supported")
        val supportedCurrencies: List<String>
    )
}
