package kigali.learning.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.QuizAttempt;
import kigali.learning.rw.domain.ResultFeedback;

@Repository
public interface ResultFeedbackRepository extends JpaRepository<ResultFeedback, UUID> {

    // E2: JPQL, path navigation (feedback -> quiz attempt -> learner -> learnerNumber) + ORDER BY
    @Query("SELECT d FROM ResultFeedback d "
            + "WHERE d.quizAttempt.learner.learnerNumber = :learnerNumber "
            + "ORDER BY d.feedbackDate DESC")
    List<ResultFeedback> findResultFeedbacksOfLearner(@Param("learnerNumber") String learnerNumber);

    // E3: DERIVED query: does this quiz attempt have at least one result feedback?
    boolean existsByQuizAttempt(QuizAttempt quizAttempt);
}
