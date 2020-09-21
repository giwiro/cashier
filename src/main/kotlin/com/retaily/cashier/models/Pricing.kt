package com.retaily.cashier.models

import java.math.BigDecimal

data class Pricing(
    val subtotal: BigDecimal,
    val taxes: BigDecimal,
    val commission: BigDecimal,
    val paymentFee: BigDecimal,
    val total: BigDecimal
) {
    val type = "pricing"
}
