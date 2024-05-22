package agh.dbcourse.hotelapp.model.dto.res

data class ExtrasReservationInfoDto(
    val extrasReservationId: Long,
    val extrasId: Long,
    val extrasName: String,
    val st: Char,
    val price: Long
)