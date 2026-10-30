package kigali.learning.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    boolean existsByCourseNumber(int courseNumber);

    boolean existsByCourseNumberAndIdNot(int courseNumber, UUID id);

   
    @Query("SELECT c FROM Course c WHERE NOT EXISTS "
            + "(SELECT i FROM Instructor i WHERE i.course = c) ORDER BY c.courseNumber")
    List<Course> findFreeCourses();
}
