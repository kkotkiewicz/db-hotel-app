package agh.dbcourse.hotelapp.model.dto.res

import java.time.LocalDate

data class ReservationInfoDto(
    val reservationId: Long?,
    val roomId: Long?,
    val personId: Long?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val status: Char?,
    val numberOfPeople: Long?,
    val totalPrice: Double,
    val extrasInfo: List<ExtrasReservationInfoDto>
)
