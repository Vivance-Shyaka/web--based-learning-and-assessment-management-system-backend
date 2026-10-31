package kigali.learning.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.Course;
import kigali.learning.rw.domain.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    // C1: JPQL, JOIN on the topics collection + LOWER
    @Query("SELECT i FROM Instructor i JOIN i.topics t WHERE LOWER(t.name) = LOWER(:name)")
    List<Instructor> findInstructorsByTopicName(@Param("name") String name);

    // D4: DERIVED query: does another instructor (not this id) already hold that course?
    boolean existsByCourseAndIdNot(Course course, UUID id);
}
