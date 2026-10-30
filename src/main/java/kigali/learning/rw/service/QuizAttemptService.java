package kigali.learning.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.Instructor;
import kigali.learning.rw.domain.Learner;
import kigali.learning.rw.domain.QuizAttempt;
import kigali.learning.rw.repository.InstructorRepository;
import kigali.learning.rw.repository.LearnerRepository;
import kigali.learning.rw.repository.QuizAttemptRepository;

@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;

    public QuizAttemptService(
            QuizAttemptRepository quizAttemptRepository,
            LearnerRepository learnerRepository,
            InstructorRepository instructorRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.learnerRepository = learnerRepository;
        this.instructorRepository = instructorRepository;
    }

    @Transactional
    public String saveQuizAttempt(QuizAttempt quizAttempt) {
        String problem = validate(quizAttempt);
        if (problem != null) {
            return problem;
        }

      
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

        quizAttemptRepository.deleteById(id);
        return "Quiz attempt deleted successfully";
    }

   
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

   
    public List<QuizAttempt> getQuizAttemptsByStatus(AttemptStatus status) {
        return quizAttemptRepository.findByStatusOrderByAttemptDateAsc(status);
    }

   
    public List<QuizAttempt> getQuizAttemptsBetween(LocalDate start, LocalDate end) {
        return quizAttemptRepository.findByAttemptDateBetweenOrderByAttemptDateAsc(start, end);
    }

  
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
}
