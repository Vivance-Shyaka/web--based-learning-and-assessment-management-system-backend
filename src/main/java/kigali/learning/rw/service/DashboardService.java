package kigali.learning.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.repository.CourseRepository;
import kigali.learning.rw.repository.InstructorRepository;
import kigali.learning.rw.repository.LearnerRepository;
import kigali.learning.rw.repository.QuizAttemptRepository;
import kigali.learning.rw.repository.ResultFeedbackRepository;

@Service
public class DashboardService {

    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final ResultFeedbackRepository resultFeedbackRepository;

    public DashboardService(
            LearnerRepository learnerRepository,
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            QuizAttemptRepository quizAttemptRepository,
            ResultFeedbackRepository resultFeedbackRepository) {
        this.learnerRepository = learnerRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.resultFeedbackRepository = resultFeedbackRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("learners", learnerRepository.count());
        summary.put("instructors", instructorRepository.count());
        summary.put("freeCourses", (long) courseRepository.findFreeCourses().size());   // reuses C2
        summary.put("attemptsToday", quizAttemptRepository.countByAttemptDate(LocalDate.now()));
        summary.put("started", quizAttemptRepository.countByStatus(AttemptStatus.STARTED));
        summary.put("feedbacks", resultFeedbackRepository.count());
        return summary;
    }
}
