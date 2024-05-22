package agh.dbcourse.hotelapp.model.dto.res

data class RoomDetailsDto(
    val roomId: Long,
    val roomNumber: Long,
    val common: Long,
    val price: Long,
    val maxNoPlaces: Long,
    val availablePlaces: Long?
)