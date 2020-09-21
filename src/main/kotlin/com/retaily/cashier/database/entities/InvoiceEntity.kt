package com.retaily.cashier.database.entities

import com.retaily.cashier.models.Invoice
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import java.math.BigDecimal
import java.util.Date
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.PersistenceContext
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Entity
@Table(
    name = "invoice",
    indexes = [
        Index(name = "invoice_user_id_invoice_id_idx", columnList = "userId,invoiceId", unique = true),
        Index(name = "invoice_order_id_invoice_id_idx", columnList = "orderId,invoiceId", unique = true)
    ]
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class InvoiceEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var invoiceId: Long? = null

    var userId: Long? = null
    var orderId: Long? = null

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var providerApiResponse: String? = null

    @OneToMany(
        mappedBy = "invoice",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    var invoiceLines: List<InvoiceLineEntity> = emptyList()

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_address_id", nullable = false)
    var billingAddress: AddressEntity? = null

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date? = null

    constructor(
        userId: Long,
        orderId: Long,
        providerApiResponse: String,
        billingAddress: AddressEntity
    ) : this() {
        this.userId = userId
        this.orderId = orderId
        this.providerApiResponse = providerApiResponse
        this.billingAddress = billingAddress
    }

    fun mapToModel(): Invoice =
        Invoice(
            invoiceId!!,
            userId!!,
            orderId!!,
            invoiceLines.map { il -> il.mapToModel() },
            billingAddress!!.mapToModel(),
            BigDecimal("0.00")
        )
}

interface InvoiceRepositoryCustom {
    fun flush()
    fun clear()
}

class InvoiceRepositoryImpl : InvoiceRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Override
    override fun flush() {
        entityManager.flush()
    }

    @Override
    override fun clear() {
        entityManager.clear()
    }
}

@Repository
interface InvoiceRepository : CrudRepository<InvoiceEntity, Long>, InvoiceRepositoryCustom {
    @Query(value = "SELECT i FROM #{#entityName} i WHERE i.userId = ?1")
    fun findByUserId(@Param("userId") userId: Long): InvoiceEntity?

    @Query(value = "SELECT i FROM #{#entityName} i WHERE i.orderId = ?1")
    fun findByOrderId(@Param("orderId") orderId: Long): InvoiceEntity?

    fun save(entity: InvoiceEntity): InvoiceEntity
}
