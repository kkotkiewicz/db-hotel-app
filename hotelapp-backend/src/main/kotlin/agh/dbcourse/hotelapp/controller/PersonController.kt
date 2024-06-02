package agh.dbcourse.hotelapp.controller

import agh.dbcourse.hotelapp.model.dao.Person
import agh.dbcourse.hotelapp.model.dto.req.PersonDto
import agh.dbcourse.hotelapp.model.dto.res.ResponseDto
import agh.dbcourse.hotelapp.repository.PersonRepository
import agh.dbcourse.hotelapp.service.PersonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/persons")
class PersonController(
    private val personRepository: PersonRepository,
    private val personService: PersonService) {

    @GetMapping
    fun getAllPersons(): List<Person> {
        return personRepository.findAll()
    }

    @PostMapping("/user")
    fun createUser(@RequestBody person: PersonDto): ResponseEntity<Any> {
        try {
            personService.addPerson(person)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("An error occurred during user creation", 400), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(ResponseDto("User successfully created", 400), HttpStatus.OK)
    }

    @PostMapping("/admin")
    fun createAdmin(@RequestBody person: PersonDto): ResponseEntity<Any> {
        try {
            personService.addAdmin(person)
        } catch (exception: Exception) {
            println(exception.message)
            return ResponseEntity(ResponseDto("An error occurred during admin creation", 400), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(ResponseDto("Admin successfully created", 400), HttpStatus.OK)
    }

}
