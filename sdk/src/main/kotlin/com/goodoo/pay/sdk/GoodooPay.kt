package com.goodoo.pay.sdk

import android.app.Activity
import android.content.Intent

object GoodooPay {

    const val EXTRA_PARAMS = "goodoo_pay_params"
    const val EXTRA_RESULT = "goodoo_pay_result"
    const val REQUEST_CODE_PAYMENT = 9001

    /**
     * Запуск экрана оплаты. Результат — в [onActivityResult] через [onActivityResult].
     */
    fun startPayment(activity: Activity, params: PaymentParams) {
        val intent = Intent(activity, GoodooPayActivity::class.java).apply {
            putExtra(EXTRA_PARAMS, params.toBundle())
        }
        activity.startActivityForResult(intent, REQUEST_CODE_PAYMENT)
    }

    /**
     * Вызвать из [Activity.onActivityResult]. Если возвращает true, результат обработан.
     */
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onResult: (PaymentResult) -> Unit,
    ): Boolean {
        if (requestCode != REQUEST_CODE_PAYMENT) return false
        val b = data?.getBundleExtra(EXTRA_RESULT)
        val result = when {
            b != null -> PaymentResult.fromBundle(b)
            resultCode == Activity.RESULT_CANCELED -> PaymentResult(status = PaymentStatus.CANCELLED)
            else -> PaymentResult(status = PaymentStatus.CANCELLED)
        }
        onResult(result)
        return true
    }
}
