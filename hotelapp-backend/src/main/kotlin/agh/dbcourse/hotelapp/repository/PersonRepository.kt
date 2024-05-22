package agh.dbcourse.hotelapp.repository

import agh.dbcourse.hotelapp.model.dao.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long>
