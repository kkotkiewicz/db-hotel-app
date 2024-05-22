package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "EXTRAS_RESERVATION")
open class ExtrasReservation {
    @Id
    @Column(name = "EXTRAS_RESERVATION_ID", nullable = false)
    open var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "EXTRAS_ID")
    open var extras: Extra? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RESERVATION_ID")
    open var reservation: Reservation? = null

    @Column(name = "STATUS")
    open var status: Boolean? = null
}