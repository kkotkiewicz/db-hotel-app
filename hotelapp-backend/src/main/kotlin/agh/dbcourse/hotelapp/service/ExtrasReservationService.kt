package agh.dbcourse.hotelapp.service

import agh.dbcourse.hotelapp.model.dto.req.ExtrasReservationDto
import agh.dbcourse.hotelapp.model.dto.res.ExtrasDetailsDto
import agh.dbcourse.hotelapp.model.dto.res.ExtrasReservationInfoDto
import jakarta.persistence.EntityManager
import jakarta.persistence.ParameterMode
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ExtrasReservationService {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun addExtrasReservation(extResDto: ExtrasReservationDto) {
        entityManager.createStoredProcedureQuery("p_add_extras_reservation")
            .registerStoredProcedureParameter("ex_id", Long::class.java, ParameterMode.IN)
            .registerStoredProcedureParameter("r_id", Long::class.java, ParameterMode.IN)
            .setParameter("ex_id", extResDto.extrasId)
            .setParameter("r_id", extResDto.reservationId)
            .executeUpdate()
    }

    @Transactional
    fun cancelExtrasReservation(extrasReservationId: Long) {
        entityManager.createStoredProcedureQuery("p_cancel_extras_reservation")
            .registerStoredProcedureParameter("exr_id", Long::class.java, ParameterMode.IN)
            .setParameter("exr_id", extrasReservationId)
            .executeUpdate()
    }

    fun getReservationExtras(reservationId: Long): List<ExtrasReservationInfoDto> {
        val query = entityManager.createNativeQuery(
            "SELECT * FROM f_get_reservation_extras(:r_id)")
        query.setParameter("r_id", reservationId)

        return query.resultList.map { result ->
            if (result is Array<*>) {
                ExtrasReservationInfoDto(
                    extrasReservationId = (result[0] as BigDecimal).toLong(),
                    extrasId = (result[1] as BigDecimal).toLong(),
                    extrasName = result[2] as String,
                    st = result[3] as Char,
                    price = (result[4] as BigDecimal).toLong()
                )
            } else {
                throw IllegalArgumentException("Invalid result type")
            }
        }
    }

    fun getAvailableExtrasForReservation(reservationId: Long): List<ExtrasDetailsDto> {
        val query = entityManager.createNativeQuery(
            "SELECT * FROM f_reservation_extras_available(:r_id)"
        )
        query.setParameter("r_id", reservationId)

        return query.resultList.map { result ->
            if (result is Array<*>) {
                ExtrasDetailsDto(
                    extrasId = (result[0] as BigDecimal).toLong(),
                    extrasName = result[1] as String,
                    extrasNumber = (result[2] as BigDecimal).toLong(),
                    price = (result[3] as BigDecimal).toLong()
                )
            } else {
                throw IllegalArgumentException("Invalid result type")
            }
        }
    }
}