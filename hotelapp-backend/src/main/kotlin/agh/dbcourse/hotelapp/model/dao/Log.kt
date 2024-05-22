package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
@Table(name = "LOG")
open class Log {
    @Id
    @Column(name = "LOG_ID", nullable = false)
    open var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RESERVATION_ID", nullable = false)
    open var reservation: Reservation? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "EXTRAS_ID")
    open var extras: Extra? = null

    @Column(name = "LOG_DATE", nullable = false)
    open var logDate: LocalDate? = null

    @Column(name = "STATUS")
    open var status: Boolean? = null
}