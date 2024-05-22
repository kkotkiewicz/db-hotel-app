package agh.dbcourse.hotelapp.controller

import agh.dbcourse.hotelapp.model.dao.ExtrasReservation
import agh.dbcourse.hotelapp.model.dto.req.ExtrasReservationDto
import agh.dbcourse.hotelapp.model.dto.res.ExtrasDetailsDto
import agh.dbcourse.hotelapp.model.dto.res.ResponseDto
import agh.dbcourse.hotelapp.repository.ExtrasReservationRepository
import agh.dbcourse.hotelapp.service.ExtrasReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/extras")
class ExtrasReservationController(
    private val extrasReservationRepository: ExtrasReservationRepository,
    private val extrasReservationService: ExtrasReservationService
) {

    @GetMapping("/{extrasReservationId}")
    fun getExtrasReservationById(@PathVariable extrasReservationId: Long): ResponseEntity<Any> {
        val eReservation: Optional<ExtrasReservation> = extrasReservationRepository.findById(extrasReservationId)
        if (eReservation.isEmpty) {
            return ResponseEntity(ResponseDto("Extras reservation not Found", 404), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(eReservation.get(), HttpStatus.OK)
    }

    @GetMapping("/available/{reservationId}")
    fun getAvailableExtrasForReservation(@PathVariable reservationId: Long): ResponseEntity<Any> {
        val result: List<ExtrasDetailsDto>
        try {
            result = extrasReservationService.getAvailableExtrasForReservation(reservationId)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("Cannot get extras for given Id", 403), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping
    fun addExtrasReservation(@RequestBody eReservation: ExtrasReservationDto): ResponseEntity<Any> {
        try {
            extrasReservationService.addExtrasReservation(eReservation)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("There was an error during adding new extras reservation", 403), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(ResponseDto("Successfully added new extras reservation", 200), HttpStatus.OK)
    }

    @DeleteMapping("/{extrasReservationId}")
    fun deleteExtrasReservation(@PathVariable extrasReservationId: Long): ResponseEntity<Any> {
        try {
            extrasReservationService.cancelExtrasReservation(extrasReservationId)
        } catch (exception: Exception) {
            println(exception)
            return ResponseEntity(ResponseDto("There was an error during deleting extras reservation", 403), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(ResponseDto("Successfully deleted extras reservation", 200), HttpStatus.OK)
    }
}