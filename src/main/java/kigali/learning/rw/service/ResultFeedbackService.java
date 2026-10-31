package kigali.learning.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.QuizAttempt;
import kigali.learning.rw.domain.ResultFeedback;
import kigali.learning.rw.repository.QuizAttemptRepository;
import kigali.learning.rw.repository.ResultFeedbackRepository;

@Service
public class ResultFeedbackService {

    private final ResultFeedbackRepository resultFeedbackRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public ResultFeedbackService(
            ResultFeedbackRepository resultFeedbackRepository,
            QuizAttemptRepository quizAttemptRepository) {
        this.resultFeedbackRepository = resultFeedbackRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Transactional
    public String saveResultFeedback(UUID quizAttemptId, ResultFeedback resultFeedback) {
        Optional<QuizAttempt> existingQuizAttempt = quizAttemptRepository.findById(quizAttemptId);
        if (existingQuizAttempt.isEmpty()) {
            return "The quiz attempt with that id does not exist";
        }

        QuizAttempt quizAttempt = existingQuizAttempt.get();
        if (quizAttempt.getStatus() != AttemptStatus.GRADED) {
            return "Result feedbacks are only for GRADED quiz attempts";
        }

        String problem = validate(resultFeedback);
        if (problem != null) {
            return problem;
        }

        ResultFeedback newResultFeedback = new ResultFeedback();
        newResultFeedback.setQuestionArea(resultFeedback.getQuestionArea().trim());
        newResultFeedback.setComment(resultFeedback.getComment());
        newResultFeedback.setMarks(resultFeedback.getMarks());
        newResultFeedback.setFeedbackDate(LocalDate.now());
        newResultFeedback.setQuizAttempt(quizAttempt);
        resultFeedbackRepository.save(newResultFeedback);
        return "Result feedback saved successfully";
    }

    // E2
    public List<ResultFeedback> getResultFeedbacksOfLearner(String learnerNumber) {
        return resultFeedbackRepository.findResultFeedbacksOfLearner(learnerNumber);
    }

    // E3
    public String deleteResultFeedback(UUID id) {
        Optional<ResultFeedback> existingResultFeedback = resultFeedbackRepository.findById(id);
        if (existingResultFeedback.isEmpty()) {
            return "The result feedback with that id does not exist";
        }

        resultFeedbackRepository.deleteById(id);
        return "Result feedback deleted successfully";
    }

    // Returns a message when the data is not acceptable, or null when it is fine.
    private String validate(ResultFeedback resultFeedback) {
        if (resultFeedback.getQuestionArea() == null || resultFeedback.getQuestionArea().isBlank()) {
            return "Result feedback question area is required";
        }
        if (resultFeedback.getMarks() < 0) {
            return "Result feedback marks cannot be negative";
        }
        return null;
    }
}
