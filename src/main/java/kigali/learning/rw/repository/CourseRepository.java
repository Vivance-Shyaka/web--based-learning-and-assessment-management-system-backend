package kigali.learning.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    boolean existsByCourseNumber(int courseNumber);

    boolean existsByCourseNumberAndIdNot(int courseNumber, UUID id);

}
