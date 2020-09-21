package com.retaily.cashier.models

data class Order(
    val id: Long,
    val status: String,
    val shippingAddress: Address,
    val billingAddress: Address,
    val paymentToken: String?,
    val items: List<OrderItem>
) {
    val type = "order"
}

data class OrderShort(
    val id: Long,
    val status: String,
    val billingAddress: Address,
    val paymentToken: String?,
    val items: List<OrderItemShort>
) {
    val type = "orderShort"
}
