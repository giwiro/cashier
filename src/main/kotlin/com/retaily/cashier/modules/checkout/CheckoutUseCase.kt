package com.retaily.cashier.modules.checkout

import com.google.gson.Gson
import com.retaily.cashier.database.entities.AddressEntity
import com.retaily.cashier.database.entities.AddressRepository
import com.retaily.cashier.database.entities.InvoiceEntity
import com.retaily.cashier.database.entities.InvoiceLineEntity
import com.retaily.cashier.database.entities.InvoiceLineRepository
import com.retaily.cashier.database.entities.InvoiceRepository
import com.retaily.cashier.models.Address
import com.retaily.cashier.models.Invoice
import com.retaily.cashier.models.OrderShort
import com.retaily.cashier.models.Pricing
import com.stripe.model.Charge
import java.util.Optional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CheckoutUseCase(
    @Autowired val addressRepository: AddressRepository,
    @Autowired val invoiceRepository: InvoiceRepository,
    @Autowired val invoiceLineRepository: InvoiceLineRepository
) {
    fun createInvoice(
        pricing: Pricing,
        order: OrderShort,
        address: Address,
        userId: Long,
        apiResponse: String
    ): Invoice {
        val billingAddress = addressRepository.save(
            AddressEntity(
                address.firstName,
                address.lastName,
                address.address1,
                address.address2,
                address.city,
                address.state,
                address.zip
            )
        )

        val invoice = invoiceRepository.save(
            InvoiceEntity(userId, order.id, apiResponse, billingAddress)
        )

        // Items
        order.items.forEach {
            invoiceLineRepository.save(
                InvoiceLineEntity(
                    invoice,
                    "Item",
                    "",
                    it.price,
                    it.amount
                )
            )
        }

        // Taxes
        invoiceLineRepository.save(
            InvoiceLineEntity(
                invoice,
                "Tax",
                "",
                pricing.taxes,
                1
            )
        )

        // Commission
        invoiceLineRepository.save(
            InvoiceLineEntity(
                invoice,
                "Commission",
                "",
                pricing.commission,
                1
            )
        )

        // Payment fee
        invoiceLineRepository.save(
            InvoiceLineEntity(
                invoice,
                "Payment fee",
                "",
                pricing.paymentFee,
                1
            )
        )

        invoiceRepository.flush()
        invoiceRepository.clear()

        return invoice.mapToModel()
    }

    fun createInvoiceFromStripe(request: CreateInvoiceFromStripeRequest): Invoice {
        val event = request.event
        if (event.type != "charge.succeeded") {
            throw WrongChargeStatus("Charge did not succeed")
        }

        val charge: Optional<Charge> = event.dataObjectDeserializer.`object` as Optional<Charge>

        if (!charge.isPresent) {
            throw WrongChargeFormat("Charge object was not found")
        }

        val pricingJSON = charge.get().metadata["pricing"]
        val orderJSON = charge.get().metadata["order"]
        val userId = charge.get().metadata["user_id"]
        val billingAddressJSON = charge.get().metadata["billing_address"]
        val gson = Gson()

        if (pricingJSON == null) {
            throw WrongChargeFormat("Pricing property in metadata is empty")
        }

        if (orderJSON == null) {
            throw WrongChargeFormat("Order property in metadata is empty")
        }

        if (billingAddressJSON == null) {
            throw WrongChargeFormat("Billing address property in metadata is empty")
        }

        if (userId == null) {
            throw WrongChargeFormat("UserId property in metadata is empty")
        }

        val pricing = gson.fromJson(pricingJSON, Pricing::class.java)
        val billingAddress = gson.fromJson(billingAddressJSON, Address::class.java)
        val order = gson.fromJson(orderJSON, OrderShort::class.java)

        return createInvoice(pricing, order, billingAddress, userId.toLong(), request.payload)
    }
}
