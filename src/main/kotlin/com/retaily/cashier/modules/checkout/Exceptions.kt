package com.retaily.cashier.modules.checkout

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadFormat(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class WrongChargeStatus(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class WrongChargeFormat(message: String) : RuntimeException(message)
