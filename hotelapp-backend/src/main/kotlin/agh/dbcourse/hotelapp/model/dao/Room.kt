package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "ROOM")
open class Room {
    @Id
    @Column(name = "ROOM_ID", nullable = false)
    open var id: Long? = null

    @Column(name = "ROOM_NUMBER")
    open var roomNumber: Long? = null

    @Column(name = "COMMON", nullable = false)
    open var common: Long? = null

    @Column(name = "MAX_NO_PLACES", nullable = false)
    open var maxNoPlaces: Long? = null

    @Column(name = "PRICE")
    open var price: Long? = null
}