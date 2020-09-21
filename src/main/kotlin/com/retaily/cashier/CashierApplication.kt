package com.retaily.cashier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.retaily.cashier", "com.retaily.common"])
class CashierApplication

fun main(args: Array<String>) {
    runApplication<CashierApplication>(*args)
}
