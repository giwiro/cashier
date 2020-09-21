package com.retaily.cashier.models

import java.math.BigDecimal

data class InvoiceLine(
    val id: Long,
    val name: String,
    val description: String,
    val unitPrice: BigDecimal,
    var amount: Int
) {
    val type = "invoiceLine"
}
