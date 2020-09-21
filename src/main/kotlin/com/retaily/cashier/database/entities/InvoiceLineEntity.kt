package com.retaily.cashier.database.entities

import com.retaily.cashier.models.InvoiceLine
import java.math.BigDecimal
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Formula
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "invoice_line")
class InvoiceLineEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var invoiceLineId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    var invoice: InvoiceEntity? = null

    var name: String? = null
    var description: String? = null
    var unitPrice: BigDecimal? = null
    var amount: Int? = null

    @Formula("BigDecimal(unitPrice).multiply(BigDecimal(amount))")
    val total: BigDecimal? = null

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date? = null

    constructor(
        invoice: InvoiceEntity,
        name: String,
        description: String,
        unitPrice: BigDecimal,
        amount: Int
    ) : this() {
        this.invoice = invoice
        this.name = name
        this.description = description
        this.unitPrice = unitPrice
        this.amount = amount
    }

    fun mapToModel(): InvoiceLine =
        InvoiceLine(
            invoiceLineId!!,
            name!!,
            description!!,
            unitPrice!!,
            amount!!
        )
}

@Repository
interface InvoiceLineRepository : CrudRepository<InvoiceLineEntity, Long> {
    fun save(entity: InvoiceLineEntity): InvoiceLineEntity
}
