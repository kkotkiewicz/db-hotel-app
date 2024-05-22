package agh.dbcourse.hotelapp.model.dao

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
@Table(name = "RESERVATION")
open class Reservation {
    @Id
    @Column(name = "RESERVATION_ID", nullable = false)
    open var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    open var room: Room? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PERSON_ID", nullable = false)
    open var person: Person? = null

    @Column(name = "STATUS", nullable = false)
    open var status: Char? = null

    @Column(name = "START_DATE", nullable = false)
    open var startDate: LocalDate? = null

    @Column(name = "END_DATE", nullable = false)
    open var endDate: LocalDate? = null

    @Column(name = "NO_OF_PEOPLE", nullable = false)
    open var noOfPeople: Long? = null
}