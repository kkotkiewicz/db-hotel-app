package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "EXTRAS")
open class Extra {
    @Id
    @Column(name = "EXTRAS_ID", nullable = false)
    open var id: Long? = null

    @Column(name = "PRICE")
    open var price: Long? = null

    @Column(name = "NAME")
    open var name: String? = null

    @Column(name = "NO_AVAILABLE")
    open var noAvailable: Long? = null
}