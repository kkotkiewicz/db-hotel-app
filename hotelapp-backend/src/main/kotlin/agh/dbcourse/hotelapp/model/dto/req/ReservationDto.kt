package agh.dbcourse.hotelapp.model.dto.req

import java.time.LocalDate

data class ReservationDto(
    val roomId: Long,
    val personId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val numberOfPeople: Int
)