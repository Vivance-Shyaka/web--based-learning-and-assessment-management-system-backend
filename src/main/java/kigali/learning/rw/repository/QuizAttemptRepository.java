package kigali.learning.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.Instructor;
import kigali.learning.rw.domain.QuizAttempt;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

  
    List<QuizAttempt> findByStatusOrderByAttemptDateAsc(AttemptStatus status);

  
    List<QuizAttempt> findByAttemptDateBetweenOrderByAttemptDateAsc(LocalDate start, LocalDate end);

    boolean existsByInstructorAndAttemptDateAndStatusNot(
            Instructor instructor, LocalDate attemptDate, AttemptStatus status);

  
    @Query("SELECT a.status, COUNT(a) FROM QuizAttempt a GROUP BY a.status")
    List<Object[]> countQuizAttemptsByStatus();

   
    @Modifying
    @Query("UPDATE QuizAttempt a SET a.status = :newStatus "
            + "WHERE a.instructor.id = :instructorId "
            + "AND a.attemptDate = :date "
            + "AND a.status <> :keepStatus")
    int cancelQuizAttemptsOfDay(
            @Param("instructorId") UUID instructorId,
            @Param("date") LocalDate date,
            @Param("newStatus") AttemptStatus newStatus,
            @Param("keepStatus") AttemptStatus keepStatus);
}
