package kigali.learning.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.Instructor;
import kigali.learning.rw.domain.Learner;
import kigali.learning.rw.domain.QuizAttempt;
import kigali.learning.rw.repository.InstructorRepository;
import kigali.learning.rw.repository.LearnerRepository;
import kigali.learning.rw.repository.QuizAttemptRepository;
import kigali.learning.rw.repository.ResultFeedbackRepository;

@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;
    private final ResultFeedbackRepository resultFeedbackRepository;

    public QuizAttemptService(
            QuizAttemptRepository quizAttemptRepository,
            LearnerRepository learnerRepository,
            InstructorRepository instructorRepository,
            ResultFeedbackRepository resultFeedbackRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.learnerRepository = learnerRepository;
        this.instructorRepository = instructorRepository;
        this.resultFeedbackRepository = resultFeedbackRepository;
    }

    @Transactional
    public String saveQuizAttempt(QuizAttempt quizAttempt) {
        String problem = validate(quizAttempt);
        if (problem != null) {
            return problem;
        }

        // D1: a quiz attempt cannot be created for a day that has already passed
        if (quizAttempt.getAttemptDate().isBefore(LocalDate.now())) {
            return "Quiz attempt date cannot be in the past";
        }

        if (quizAttempt.getLearner() == null || quizAttempt.getLearner().getId() == null) {
            return "The learner with that id does not exist";
        }
        Optional<Learner> requestedLearner = learnerRepository.findById(quizAttempt.getLearner().getId());
        if (requestedLearner.isEmpty()) {
            return "The learner with that id does not exist";
        }

        if (quizAttempt.getInstructor() == null || quizAttempt.getInstructor().getId() == null) {
            return "The instructor with that id does not exist";
        }
        Optional<Instructor> requestedInstructor =
                instructorRepository.findById(quizAttempt.getInstructor().getId());
        if (requestedInstructor.isEmpty()) {
            return "The instructor with that id does not exist";
        }

        // D1: the instructor may have only one attempt per date, unless the other one is ABANDONED
        if (quizAttemptRepository.existsByInstructorAndAttemptDateAndStatusNot(
                requestedInstructor.get(), quizAttempt.getAttemptDate(), AttemptStatus.ABANDONED)) {
            return "Instructor already grades an attempt on that date";
        }

        QuizAttempt newQuizAttempt = new QuizAttempt();
        newQuizAttempt.setAttemptDate(quizAttempt.getAttemptDate());
        newQuizAttempt.setStatus(AttemptStatus.STARTED);
        newQuizAttempt.setQuizTitle(quizAttempt.getQuizTitle().trim());
        newQuizAttempt.setTotalQuestions(quizAttempt.getTotalQuestions());
        newQuizAttempt.setLearner(requestedLearner.get());
        newQuizAttempt.setInstructor(requestedInstructor.get());
        quizAttemptRepository.save(newQuizAttempt);
        return "Quiz attempt saved successfully";
    }

    public List<QuizAttempt> getAllQuizAttempts() {
        return quizAttemptRepository.findAll();
    }

    public Optional<QuizAttempt> getQuizAttemptById(UUID id) {
        return quizAttemptRepository.findById(id);
    }

    public String updateQuizAttempt(UUID id, QuizAttempt updatedQuizAttempt) {
        Optional<QuizAttempt> existingQuizAttempt = quizAttemptRepository.findById(id);
        if (existingQuizAttempt.isEmpty()) {
            return "The quiz attempt with that id does not exist";
        }

        String problem = validate(updatedQuizAttempt);
        if (problem != null) {
            return problem;
        }

        QuizAttempt quizAttempt = existingQuizAttempt.get();
        quizAttempt.setAttemptDate(updatedQuizAttempt.getAttemptDate());
        quizAttempt.setQuizTitle(updatedQuizAttempt.getQuizTitle().trim());
        quizAttempt.setTotalQuestions(updatedQuizAttempt.getTotalQuestions());
        quizAttemptRepository.save(quizAttempt);
        return "Quiz attempt updated successfully";
    }

    public String deleteQuizAttempt(UUID id) {
        Optional<QuizAttempt> existingQuizAttempt = quizAttemptRepository.findById(id);
        if (existingQuizAttempt.isEmpty()) {
            return "The quiz attempt with that id does not exist";
        }

        // E3: a quiz attempt that has result feedbacks cannot be deleted
        if (resultFeedbackRepository.existsByQuizAttempt(existingQuizAttempt.get())) {
            return "That quiz attempt has result feedbacks, delete them first";
        }

        quizAttemptRepository.deleteById(id);
        return "Quiz attempt deleted successfully";
    }

    // Returns a message when the data is not acceptable, or null when it is fine.
    // The 'date not in the past' rule is checked in saveQuizAttempt (D1), not here.
    private String validate(QuizAttempt quizAttempt) {
        if (quizAttempt.getAttemptDate() == null) {
            return "Quiz attempt date is required";
        }
        if (quizAttempt.getQuizTitle() == null || quizAttempt.getQuizTitle().isBlank()) {
            return "Quiz title is required";
        }
        if (quizAttempt.getTotalQuestions() <= 0) {
            return "Total questions must be greater than zero";
        }
        return null;
    }

    // B2
    public List<QuizAttempt> getQuizAttemptsByStatus(AttemptStatus status) {
        return quizAttemptRepository.findByStatusOrderByAttemptDateAsc(status);
    }

    // B3
    public List<QuizAttempt> getQuizAttemptsBetween(LocalDate start, LocalDate end) {
        return quizAttemptRepository.findByAttemptDateBetweenOrderByAttemptDateAsc(start, end);
    }

    // D2: status flow  STARTED -> SUBMITTED or ABANDONED, SUBMITTED -> GRADED or ABANDONED
    public String updateQuizAttemptStatus(UUID id, AttemptStatus newStatus) {
        Optional<QuizAttempt> existingQuizAttempt = quizAttemptRepository.findById(id);
        if (existingQuizAttempt.isEmpty()) {
            return "The quiz attempt with that id does not exist";
        }

        QuizAttempt quizAttempt = existingQuizAttempt.get();
        AttemptStatus currentStatus = quizAttempt.getStatus();

        boolean allowed = false;
        if (currentStatus == AttemptStatus.STARTED) {
            allowed = newStatus == AttemptStatus.SUBMITTED || newStatus == AttemptStatus.ABANDONED;
        } else if (currentStatus == AttemptStatus.SUBMITTED) {
            allowed = newStatus == AttemptStatus.GRADED || newStatus == AttemptStatus.ABANDONED;
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        quizAttempt.setStatus(newStatus);
        quizAttemptRepository.save(quizAttempt);
        return "Quiz attempt status updated successfully";
    }

    // F1
    public List<Object[]> getQuizAttemptCountsByStatus() {
        return quizAttemptRepository.countQuizAttemptsByStatus();
    }

    // F2: the instructor is absent, so every attempt of that day becomes ABANDONED,
    // except the ones that are already GRADED. @Transactional is required for UPDATE queries.
    @Transactional
    public String cancelDayOfInstructor(UUID instructorId, LocalDate date) {
        if (instructorRepository.findById(instructorId).isEmpty()) {
            return "The instructor with that id does not exist";
        }

        int cancelled = quizAttemptRepository.cancelQuizAttemptsOfDay(
                instructorId, date, AttemptStatus.ABANDONED, AttemptStatus.GRADED);
        return cancelled + " quiz attempts cancelled";
    }

    // Bonus: quiz attempts page by page
    public Page<QuizAttempt> getQuizAttemptsPage(Pageable pageable) {
        return quizAttemptRepository.findAll(pageable);
    }
}
