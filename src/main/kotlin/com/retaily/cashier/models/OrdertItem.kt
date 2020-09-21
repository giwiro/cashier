package com.retaily.cashier.models

import java.math.BigDecimal

data class OrderItem(
    val id: Long,
    val name: String,
    val category: ProductCategory,
    val price: BigDecimal,
    val imageUrl: String,
    val amount: Int
) {
    val type = "orderItem"
}

data class OrderItemShort(
    val id: Long,
    val price: BigDecimal,
    val amount: Int
) {
    val type = "orderItemShort"
}
