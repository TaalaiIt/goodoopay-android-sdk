package com.goodoo.pay.sdk

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** GoodooPayPlugin */
class GoodooPayPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "goodoo_pay")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getInstalledBankApps" -> {
                result.success(getInstalledBankApps())
            }
            "openBankApp" -> {
                val packageName = call.argument<String>("packageName")
                val urlScheme = call.argument<String>("urlScheme")
                val paymentUrl = call.argument<String>("paymentUrl")
                
                if (packageName != null && paymentUrl != null) {
                    val success = openBankApp(packageName, urlScheme, paymentUrl)
                    result.success(success)
                } else {
                    result.error("INVALID_ARGUMENTS", "Missing required arguments", null)
                }
            }
            "shareQR" -> {
                val qrData = call.argument<String>("qrData")
                if (qrData != null) {
                    shareQR(qrData)
                    result.success(null)
                } else {
                    result.error("INVALID_ARGUMENTS", "Missing qrData", null)
                }
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun getInstalledBankApps(): List<String> {
        val installedApps = mutableListOf<String>()
        val pm = context.packageManager
        
        // List of bank package names
        val bankPackages = listOf(
            "ru.sberbankmobile",
            "com.idamobile.tinkoff",
            "ru.vtb24.mobilebanking.android",
            "ru.alfabank.mobile.android",
            "ru.gazprombank.mobile",
            "ru.raiffeisen",
        )
        
        bankPackages.forEach { packageName ->
            try {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                installedApps.add(packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                // App not installed
            }
        }
        
        return installedApps
    }

    private fun openBankApp(packageName: String, urlScheme: String?, paymentUrl: String): Boolean {
        return try {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            
            if (intent != null) {
                // Try to open with payment URL if URL scheme is provided
                if (urlScheme != null && paymentUrl.isNotEmpty()) {
                    try {
                        val uri = Uri.parse("$urlScheme://payment?url=$paymentUrl")
                        intent.data = uri
                    } catch (e: Exception) {
                        // Fallback to regular app launch
                    }
                }
                
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun shareQR(qrData: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, qrData)
        }
        
        val shareIntent = Intent.createChooser(intent, "Share QR Code")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
