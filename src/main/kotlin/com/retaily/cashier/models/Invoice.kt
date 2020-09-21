package com.retaily.cashier.models

import java.math.BigDecimal

data class Invoice(
    val id: Long,
    val userId: Long,
    val orderId: Long,
    val lines: List<InvoiceLine>,
    val billingAddress: Address,
    val total: BigDecimal
) {
    val type = "invoice"
}
