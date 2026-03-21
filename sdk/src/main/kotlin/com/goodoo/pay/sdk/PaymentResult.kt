package com.goodoo.pay.sdk

import android.os.Bundle

/**
 * Результат платежа (аналог Flutter PaymentResult).
 * Передаётся в [GoodooPay.startPayment] onResult.
 */
data class PaymentResult(
    val status: PaymentStatus,
    val transactionId: String? = null,
    val amount: Double? = null,
    val timestamp: Long? = null,
    val errorMessage: String? = null,
) {
    val isSuccess: Boolean get() = status == PaymentStatus.SUCCESS
    val isFailed: Boolean get() = status == PaymentStatus.FAILED
    val isCancelled: Boolean get() = status == PaymentStatus.CANCELLED
    val isPending: Boolean get() = status == PaymentStatus.PENDING

    fun toBundle(): Bundle = Bundle().apply {
        putString(KEY_STATUS, status.name)
        putString(KEY_TRANSACTION_ID, transactionId)
        putDouble(KEY_AMOUNT, amount ?: 0.0)
        putLong(KEY_TIMESTAMP, timestamp ?: 0L)
        putString(KEY_ERROR_MESSAGE, errorMessage)
    }

    companion object {
        private const val KEY_STATUS = "status"
        private const val KEY_TRANSACTION_ID = "transactionId"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_ERROR_MESSAGE = "errorMessage"

        fun fromBundle(b: Bundle): PaymentResult {
            val statusStr = b.getString(KEY_STATUS) ?: "failed"
            val status = try {
                PaymentStatus.valueOf(statusStr)
            } catch (_: Exception) {
                PaymentStatus.FAILED
            }
            return PaymentResult(
                status = status,
                transactionId = b.getString(KEY_TRANSACTION_ID),
                amount = b.getDouble(KEY_AMOUNT).takeIf { it != 0.0 },
                timestamp = b.getLong(KEY_TIMESTAMP).takeIf { it != 0L },
                errorMessage = b.getString(KEY_ERROR_MESSAGE),
            )
        }

        fun fromMap(map: Map<String, Any?>): PaymentResult {
            val statusStr = (map["status"] as? String)?.lowercase() ?: "failed"
            val status = when (statusStr) {
                "success" -> PaymentStatus.SUCCESS
                "failed" -> PaymentStatus.FAILED
                "cancelled" -> PaymentStatus.CANCELLED
                "pending" -> PaymentStatus.PENDING
                else -> PaymentStatus.FAILED
            }
            val amount = (map["amount"] as? Number)?.toDouble()
            val timestamp = (map["timestamp"] as? Number)?.toLong()
            return PaymentResult(
                status = status,
                transactionId = map["transactionId"] as? String,
                amount = amount,
                timestamp = timestamp,
                errorMessage = map["errorMessage"] as? String,
            )
        }
    }
}

enum class PaymentStatus {
    SUCCESS,
    FAILED,
    CANCELLED,
    PENDING,
}
