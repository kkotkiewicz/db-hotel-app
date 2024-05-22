package agh.dbcourse.hotelapp.repository

import agh.dbcourse.hotelapp.model.dao.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long>
