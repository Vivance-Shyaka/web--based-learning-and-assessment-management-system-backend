package kigali.learning.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.QuizAttempt;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

   
    List<QuizAttempt> findByStatusOrderByAttemptDateAsc(AttemptStatus status);

   
    List<QuizAttempt> findByAttemptDateBetweenOrderByAttemptDateAsc(LocalDate start, LocalDate end);
}
