package kigali.learning.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
