package com.retaily.cashier.modules.checkout

import com.stripe.model.Event

data class CreateInvoiceFromStripeRequest(val event: Event, val payload: String)
