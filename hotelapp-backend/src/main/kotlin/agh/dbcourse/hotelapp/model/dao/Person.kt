package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "PERSON")
open class Person {
    @Id
    @Column(name = "PERSON_ID", nullable = false)
    open var id: Long? = null

    @Column(name = "FIRSTNAME", length = 50)
    open var firstname: String? = null

    @Column(name = "LASTNAME", length = 50)
    open var lastname: String? = null

    @Column(name = "EMAIL")
    open var email: String? = null

    @Column(name = "PASSWORD")
    open var password: String? = null

    @ColumnDefault("0")
    @Column(name = "ADMIN", nullable = false)
    open var admin: Long? = null
}