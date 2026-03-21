package com.goodoo.pay.sdk

import android.content.Intent
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * Activity с Flutter-экраном оплаты. Запускается через [GoodooPay.startPayment].
 * Принимает параметры из Intent, передаёт в Dart по MethodChannel,
 * по результату из Dart вызывает setResult и finish().
 */
class GoodooPayActivity : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_RESULT).apply {
            setMethodCallHandler { call, result ->
                if (call.method == "onPaymentResult") {
                    @Suppress("UNCHECKED_CAST")
                    val map = call.arguments as? Map<String, Any?> ?: emptyMap()
                    val paymentResult = PaymentResult.fromMap(map)
                    val intent = Intent().apply {
                        putExtra(GoodooPay.EXTRA_RESULT, paymentResult.toBundle())
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                    result.success(null)
                } else {
                    result.notImplemented()
                }
            }
        }
        val paramsBundle = intent.getBundleExtra(GoodooPay.EXTRA_PARAMS)
        if (paramsBundle != null) {
            val params = PaymentParams.fromBundle(paramsBundle)
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_START).apply {
                invokeMethod("startPayment", params.toMap(), object : MethodChannel.Result {
                    override fun success(o: Any?) {}
                    override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                    override fun notImplemented() {}
                })
            }
        }
    }

    companion object {
        const val CHANNEL_START = "goodoo_pay_native_start"
        const val CHANNEL_RESULT = "goodoo_pay_native_result"
    }
}
