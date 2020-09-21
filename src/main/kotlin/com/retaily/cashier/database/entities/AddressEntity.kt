package com.retaily.cashier.database.entities

import com.retaily.cashier.models.Address
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PersistenceContext
import javax.persistence.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "address")
class AddressEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var addressId: Long? = null

    var firstName: String? = null
    var lastName: String? = null
    var address1: String? = null
    var address2: String? = null
    var city: String? = null
    var state: String? = null
    var zip: String? = null

    constructor(
        firstName: String,
        lastName: String,
        address1: String,
        address2: String,
        city: String,
        state: String,
        zip: String
    ) : this() {
        this.firstName = firstName
        this.lastName = lastName
        this.address1 = address1
        this.address2 = address2
        this.city = city
        this.state = state
        this.zip = zip
    }

    fun mapToModel(): Address = Address(firstName!!, lastName!!, address1!!, address2!!, city!!, state!!, zip!!)
}

interface AddressRepositoryCustom {
    fun flush()
    fun clear()
}

class AddressRepositoryImpl : AddressRepositoryCustom {
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
interface AddressRepository : CrudRepository<AddressEntity, Long>, AddressRepositoryCustom {
    fun save(entity: AddressEntity): AddressEntity
}
