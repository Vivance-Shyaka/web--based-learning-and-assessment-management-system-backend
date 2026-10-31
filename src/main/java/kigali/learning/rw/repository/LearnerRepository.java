package kigali.learning.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.Learner;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, UUID> {

    boolean existsByLearnerNumber(String learnerNumber);

    boolean existsByLearnerNumberAndIdNot(String learnerNumber, UUID id);

    // B1: DERIVED query (IgnoreCase + OrderBy)
    List<Learner> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // C4: JPQL, DISTINCT + path navigation (quiz attempt -> instructor -> id)
    @Query("SELECT DISTINCT a.learner FROM QuizAttempt a WHERE a.instructor.id = :instructorId")
    List<Learner> findLearnersOfInstructor(@Param("instructorId") UUID instructorId);
}
