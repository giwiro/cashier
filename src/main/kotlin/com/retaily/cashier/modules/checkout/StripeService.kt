package com.retaily.cashier.modules.checkout

import com.google.gson.JsonSyntaxException
import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.net.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StripeService {
    @Value("\${stripe.endpoint.secret}")
    val endpointSecret: String? = null

    fun getPaymentIntentSuccessEvent(payload: String, sigHeader: String): Event {
        return try {
            Webhook.constructEvent(
                payload, sigHeader, endpointSecret
            )
        } catch (e: JsonSyntaxException) {
            // Invalid payload
            throw BadFormat("Body is not a valid JSON object")
        } catch (e: SignatureVerificationException) {
            // Invalid signature
            throw BadFormat("Invalid signature")
        }
    }
}
