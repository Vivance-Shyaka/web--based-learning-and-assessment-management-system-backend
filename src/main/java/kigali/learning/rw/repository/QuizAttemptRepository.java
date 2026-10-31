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

    // B2: DERIVED query (enum parameter + OrderBy)
    List<QuizAttempt> findByStatusOrderByAttemptDateAsc(AttemptStatus status);

    // B3: DERIVED query (Between, both dates included, + OrderBy)
    List<QuizAttempt> findByAttemptDateBetweenOrderByAttemptDateAsc(LocalDate start, LocalDate end);

    // D1: DERIVED query (existsBy + And + Not): does this instructor already have an
    // attempt on that date whose status is NOT the given one (ABANDONED)?
    boolean existsByInstructorAndAttemptDateAndStatusNot(
            Instructor instructor, LocalDate attemptDate, AttemptStatus status);

    // F1: JPQL, GROUP BY + COUNT. One row per status that has at least one attempt.
    @Query("SELECT a.status, COUNT(a) FROM QuizAttempt a GROUP BY a.status")
    List<Object[]> countQuizAttemptsByStatus();

    // F2: JPQL bulk UPDATE, one query and no Java loop. Returns how many rows were changed.
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

    // Bonus dashboard: DERIVED countBy queries
    long countByAttemptDate(LocalDate attemptDate);

    long countByStatus(AttemptStatus status);
}
