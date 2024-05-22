package agh.dbcourse.hotelapp.controller

import agh.dbcourse.hotelapp.model.dao.Reservation
import agh.dbcourse.hotelapp.model.dto.req.ReservationDto
import agh.dbcourse.hotelapp.model.dto.res.ResponseDto
import agh.dbcourse.hotelapp.repository.ReservationRepository
import agh.dbcourse.hotelapp.service.ExtrasReservationService
import agh.dbcourse.hotelapp.service.PersonService
import agh.dbcourse.hotelapp.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/reservations")
class ReservationController(
    private val reservationRepository: ReservationRepository,
    private val reservationService: ReservationService,
    private val extrasReservationService: ExtrasReservationService,
    private val personService: PersonService
) {

    @GetMapping("/{reservationId}")
    fun getReservationById(@PathVariable reservationId: Long): ResponseEntity<Any> {
        val reservation: Optional<Reservation> = reservationRepository.findById(reservationId)
        if (reservation.isEmpty) {
            return ResponseEntity(ResponseDto("Reservation not Found", 404), HttpStatus.NOT_FOUND)
        }

        val reservationPrice = reservationService.getReservationPrice(reservationId)
        val extrasInfo = extrasReservationService.getReservationExtras(reservationId)

        val response = reservationService.createReservationResponse(
            reservation.get(), reservationPrice, extrasInfo)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping
    fun addReservation(@RequestBody reservation: ReservationDto): ResponseEntity<Any> {
        try {
            reservationService.addReservation(reservation)
            return ResponseEntity(ResponseDto("Successfully added new reservation", 200), HttpStatus.OK)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("There was an error during adding new reservation", 401), HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{reservationId}")
    fun updateReservationStatus(@PathVariable reservationId: Long,
                                @RequestParam rStatus: Char): ResponseEntity<Any> {
        try {
            when (rStatus) {
                'C' -> {
                    reservationService.cancelReservation(reservationId)
                }
                'P' -> {
                    reservationService.payForReservation(reservationId)
                }
                'N' -> {
                    reservationService.restoreReservationStatus(reservationId)
                }
                else -> {
                    return ResponseEntity(ResponseDto("Given status must be either 'N', 'P' or 'C'", 403), HttpStatus.BAD_REQUEST)
                }
            }
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("There was an error during modifying reservation status", 403), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(ResponseDto("Successfully modified reservation status",200), HttpStatus.OK)
    }

    @GetMapping("/person/{personId}")
    fun getAllPersonReservations(@PathVariable personId: Long): ResponseEntity<Any> {
        try {
            val result = personService.getPersonReservationIds(personId)
            val response = result.map { resId ->
                reservationService.createReservationResponse(
                    reservationRepository.findById(resId).get(),
                    reservationService.getReservationPrice(resId),
                    extrasReservationService.getReservationExtras(resId)
                )
            }
            return ResponseEntity(response, HttpStatus.OK)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("Error during finding reservations with given Id",401), HttpStatus.BAD_REQUEST)
        }
    }
}