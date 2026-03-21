package com.example.goodoo_pay_native

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodoo.pay.sdk.GoodooPay
import com.goodoo.pay.sdk.PaymentParams
import com.goodoo.pay.sdk.PaymentResult

class MainActivity : AppCompatActivity() {

    private lateinit var editAmount: EditText
    private lateinit var editLs: EditText
    private lateinit var editBank: EditText

    private lateinit var textStatus: TextView
    private lateinit var textLastStatus: TextView
    private lateinit var textLastTransactionId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editAmount = findViewById(R.id.editAmount)
        editLs = findViewById(R.id.editLs)
        editBank = findViewById(R.id.editBank)

        textStatus = findViewById(R.id.textStatus)
        textLastStatus = findViewById(R.id.textLastStatus)
        textLastTransactionId = findViewById(R.id.textLastTransactionId)

        val buttonStart: Button = findViewById(R.id.buttonStartPayment)
        buttonStart.setOnClickListener { startPayment() }
    }

    private fun startPayment() {
        val amountText = editAmount.text.toString().trim()
        val lsText = editLs.text.toString().trim()
        val bankText = editBank.text.toString().trim()

        val amount = amountText.toDoubleOrNull() ?: 10.0
        val ls = if (lsText.isEmpty()) "112197" else lsText
        val bank = if (bankText.isEmpty()) "О! банк" else bankText

        val params = PaymentParams(
            apiKey = "",
            ls = ls,
            bank = bank,
            transactionId = null,
            amount = amount,
            description = "",
            availableMethods = listOf("bank", "qr")
        )

        try {
            GoodooPay.startPayment(this, params)
        } catch (e: Exception) {
            textStatus.text = "Error: ${e.message}"
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        GoodooPay.onActivityResult(requestCode, resultCode, data) { result: PaymentResult ->
            handlePaymentResult(result)
        }
    }

    private fun handlePaymentResult(result: PaymentResult) {
        val statusText = "Payment ${result.status.name}"
        textStatus.text = statusText

        textLastStatus.text = "Status: ${result.status.name}"
        textLastTransactionId.text =
            result.transactionId?.let { "Transaction ID: $it" } ?: ""
    }
}

