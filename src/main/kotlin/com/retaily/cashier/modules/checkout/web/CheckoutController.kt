package com.retaily.cashier.modules.checkout.web

import com.retaily.cashier.modules.checkout.CheckoutUseCase
import com.retaily.cashier.modules.checkout.CreateInvoiceFromStripeRequest
import com.retaily.cashier.modules.checkout.StripeService
import com.retaily.common.web.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/cashier/checkout"])
class CheckoutController(
    @Autowired val sessionService: SessionService,
    @Autowired val checkoutUseCase: CheckoutUseCase,
    @Autowired val stripeService: StripeService
) {
    @PostMapping(path = ["/stripe"])
    fun acceptStripePayment(
        @RequestBody body: String,
        @RequestHeader("Stripe-Signature") sigHeader: String
    ) {
        val event = stripeService.getPaymentIntentSuccessEvent(body, sigHeader)
        val invoice = checkoutUseCase.createInvoiceFromStripe(CreateInvoiceFromStripeRequest(event, body))
        println("!!!!!!!!!!!!!!!")
        println(invoice)
    }
}
