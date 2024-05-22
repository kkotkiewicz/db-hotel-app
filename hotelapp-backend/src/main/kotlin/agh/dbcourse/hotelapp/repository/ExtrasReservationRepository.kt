package agh.dbcourse.hotelapp.repository

import agh.dbcourse.hotelapp.model.dao.ExtrasReservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExtrasReservationRepository: JpaRepository<ExtrasReservation, Long>