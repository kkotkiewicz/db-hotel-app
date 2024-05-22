package agh.dbcourse.hotelapp.service

import agh.dbcourse.hotelapp.model.dao.Reservation
import agh.dbcourse.hotelapp.model.dto.req.ReservationDto
import agh.dbcourse.hotelapp.model.dto.res.ExtrasReservationInfoDto
import agh.dbcourse.hotelapp.model.dto.res.ReservationInfoDto
import agh.dbcourse.hotelapp.model.dto.res.RoomDetailsDto
import jakarta.persistence.EntityManager
import jakarta.persistence.ParameterMode
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ReservationService {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun addReservation(res: ReservationDto) {
        entityManager.createStoredProcedureQuery("p_add_reservation")
            .registerStoredProcedureParameter("rm_id", Long::class.java, ParameterMode.IN)
            .registerStoredProcedureParameter("pn_id", Long::class.java, ParameterMode.IN)
            .registerStoredProcedureParameter("date_from", LocalDate::class.java, ParameterMode.IN)
            .registerStoredProcedureParameter("date_to", LocalDate::class.java, ParameterMode.IN)
            .registerStoredProcedureParameter("no_people", Integer::class.java, ParameterMode.IN)
            .setParameter("rm_id", res.roomId)
            .setParameter("pn_id", res.personId)
            .setParameter("date_from", res.startDate)
            .setParameter("date_to", res.endDate)
            .setParameter("no_people", res.numberOfPeople)
            .executeUpdate()
    }

    @Transactional
    fun cancelReservation(resID: Long) {
        entityManager.createStoredProcedureQuery("p_cancel_reservation")
            .registerStoredProcedureParameter("r_id", Long::class.java, ParameterMode.IN)
            .setParameter("r_id", resID)
            .executeUpdate()
    }

    @Transactional
    fun payForReservation(resID: Long) {
        entityManager.createStoredProcedureQuery("p_pay_for_reservation")
            .registerStoredProcedureParameter("r_id", Long::class.java, ParameterMode.IN)
            .setParameter("r_id", resID)
            .executeUpdate()
    }

    @Transactional
    fun restoreReservationStatus(resID: Long) {
        entityManager.createStoredProcedureQuery("p_restore_reservation")
            .registerStoredProcedureParameter("r_id", Long::class.java, ParameterMode.IN)
            .setParameter("r_id", resID)
            .executeUpdate()
    }

    fun getReservationPrice(reservationId: Long): Double {
        val query = entityManager.createNativeQuery(
            "SELECT f_get_reservation_price(:reservation_id_check) FROM DUAL"
        )
        query.setParameter("reservation_id_check", reservationId)

        val result = query.singleResult
        return (result as Number).toDouble()
    }

    fun getAvailableRooms(dateFrom: LocalDate, dateTo: LocalDate): List<RoomDetailsDto> {
        val query = entityManager.createNativeQuery(
            "SELECT * FROM f_available_rooms(:date_from, :date_to)"
        )
        query.setParameter("date_from", dateFrom)
        query.setParameter("date_to", dateTo)

        return query.resultList.map { result ->
            if (result is Array<*>) {
                if (result[5] == null) {
                    RoomDetailsDto(
                        roomId = (result[0] as BigDecimal).toLong(),
                        roomNumber = (result[1] as BigDecimal).toLong(),
                        common = (result[2] as BigDecimal).toLong(),
                        price = (result[3] as BigDecimal).toLong(),
                        maxNoPlaces = (result[4] as BigDecimal).toLong(),
                        availablePlaces = null
                    )
                } else {
                RoomDetailsDto(
                    roomId = (result[0] as BigDecimal).toLong(),
                    roomNumber = (result[1] as BigDecimal).toLong(),
                    common = (result[2] as BigDecimal).toLong(),
                    price = (result[3] as BigDecimal).toLong(),
                    maxNoPlaces = (result[4] as BigDecimal).toLong(),
                    availablePlaces = (result[5] as BigDecimal).toLong()
                )}
            } else {
                throw IllegalArgumentException("Invalid result type")
            }
        }

    }

    fun createReservationResponse(reservation: Reservation, price: Double, extrasInfo: List<ExtrasReservationInfoDto>): ReservationInfoDto {
        return ReservationInfoDto(
            reservation.id, reservation.room?.id, reservation.person?.id, reservation.startDate,
            reservation.endDate, reservation.status, reservation.noOfPeople, price, extrasInfo
        )
    }
}