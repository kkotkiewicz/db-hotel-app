package agh.dbcourse.hotelapp.controller

import agh.dbcourse.hotelapp.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
@RequestMapping("/rooms")
class RoomController(
    private val reservationService: ReservationService
) {

    @GetMapping
    fun getAvailableRooms(@RequestParam dateFrom: LocalDate, @RequestParam dateTo: LocalDate): ResponseEntity<Any> {
        return ResponseEntity(reservationService.getAvailableRooms(dateFrom, dateTo), HttpStatus.OK)
    }
}