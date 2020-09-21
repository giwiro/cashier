package com.retaily.cashier.models

data class Address(
    val firstName: String,
    val lastName: String,
    val address1: String,
    val address2: String,
    val city: String,
    val state: String,
    val zip: String
) {
    val type = "address"
}
