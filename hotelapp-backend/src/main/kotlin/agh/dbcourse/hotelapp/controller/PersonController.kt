package agh.dbcourse.hotelapp.controller

import agh.dbcourse.hotelapp.model.dao.Person
import agh.dbcourse.hotelapp.repository.PersonRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun getAllPersons(): List<Person> {
        return personRepository.findAll()
    }

}
