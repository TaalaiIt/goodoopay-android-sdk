package com.goodoo.pay.sdk

import android.os.Bundle

/**
 * Параметры платежа (аналог Flutter PaymentParams).
 * Используется при вызове [GoodooPay.startPayment].
 */
data class PaymentParams(
    val apiKey: String = "",
    val ls: String = "",
    val bank: String? = null,
    val transactionId: String? = null,
    val amount: Double,
    val description: String? = null,
    val availableMethods: List<String> = listOf("bank", "qr"),
) {

    fun toBundle(): Bundle = Bundle().apply {
        putString(KEY_API_KEY, apiKey)
        putString(KEY_LS, ls)
        putString(KEY_BANK, bank)
        putString(KEY_TRANSACTION_ID, transactionId)
        putDouble(KEY_AMOUNT, amount)
        putString(KEY_DESCRIPTION, description)
        putStringArrayList(KEY_AVAILABLE_METHODS, ArrayList(availableMethods))
    }

    fun toMap(): Map<String, Any?> = mapOf(
        "apiKey" to apiKey,
        "ls" to ls,
        "bank" to bank,
        "transactionId" to transactionId,
        "amount" to amount,
        "description" to description,
        "availableMethods" to availableMethods,
    )

    companion object {
        private const val KEY_API_KEY = "apiKey"
        private const val KEY_LS = "ls"
        private const val KEY_BANK = "bank"
        private const val KEY_TRANSACTION_ID = "transactionId"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_AVAILABLE_METHODS = "availableMethods"

        fun fromBundle(b: Bundle): PaymentParams = PaymentParams(
            apiKey = b.getString(KEY_API_KEY) ?: "",
            ls = b.getString(KEY_LS) ?: "",
            bank = b.getString(KEY_BANK),
            transactionId = b.getString(KEY_TRANSACTION_ID),
            amount = b.getDouble(KEY_AMOUNT),
            description = b.getString(KEY_DESCRIPTION),
            availableMethods = b.getStringArrayList(KEY_AVAILABLE_METHODS) ?: listOf("bank", "qr"),
        )
    }
}
