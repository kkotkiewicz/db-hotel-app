package agh.dbcourse.hotelapp.service

import agh.dbcourse.hotelapp.model.dto.req.PersonDto
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PersonService {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun addPerson(personDto: PersonDto) {
        entityManager.createNativeQuery("CALL p_add_user(:fn, :ln, :email, :password)")
            .setParameter("fn", personDto.firstname)
            .setParameter("ln", personDto.lastname)
            .setParameter("email", personDto.email)
            .setParameter("password", personDto.password)
            .executeUpdate()
    }

    @Transactional
    fun addAdmin(personDto: PersonDto) {
        entityManager.createNativeQuery("CALL p_add_admin(:fn, :ln, :email, :password)")
            .setParameter("fn", personDto.firstname)
            .setParameter("ln", personDto.lastname)
            .setParameter("email", personDto.email)
            .setParameter("password", personDto.password)
            .executeUpdate()
    }

    fun getPersonReservationIds(personId: Long): List<Long> {
        val query = entityManager.createNativeQuery(
            "SELECT * FROM F_GET_PERSON_RESERVATION_IDS(:p_id)"
        )
        query.setParameter("p_id", personId)

        return query.resultList.map { result -> (result as BigDecimal).toLong() }
    }
}