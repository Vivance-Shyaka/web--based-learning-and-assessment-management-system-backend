package kigali.learning.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.learning.rw.domain.AttemptStatus;
import kigali.learning.rw.domain.QuizAttempt;
import kigali.learning.rw.service.QuizAttemptService;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveQuizAttempt(@RequestBody QuizAttempt quizAttempt) {
        String message = quizAttemptService.saveQuizAttempt(quizAttempt);
        if ("Quiz attempt saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if ("Instructor already grades an attempt on that date".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuizAttempt>> getAllQuizAttempts() {
        return new ResponseEntity<>(quizAttemptService.getAllQuizAttempts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizAttemptById(@PathVariable UUID id) {
        Optional<QuizAttempt> quizAttempt = quizAttemptService.getQuizAttemptById(id);
        if (quizAttempt.isPresent()) {
            return new ResponseEntity<>(quizAttempt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The quiz attempt with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQuizAttempt(@PathVariable UUID id, @RequestBody QuizAttempt quizAttempt) {
        String message = quizAttemptService.updateQuizAttempt(id, quizAttempt);
        if ("Quiz attempt updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuizAttempt(@PathVariable UUID id) {
        String message = quizAttemptService.deleteQuizAttempt(id);
        if ("Quiz attempt deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if ("That quiz attempt has result feedbacks, delete them first".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    // B2
    @GetMapping("/by-status")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByStatus(@RequestParam AttemptStatus status) {
        return new ResponseEntity<>(quizAttemptService.getQuizAttemptsByStatus(status), HttpStatus.OK);
    }

    // B3
    @GetMapping("/between")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsBetween(
            @RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(quizAttemptService.getQuizAttemptsBetween(startDate, endDate), HttpStatus.OK);
    }

    // D2
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateQuizAttemptStatus(
            @PathVariable UUID id, @RequestParam AttemptStatus status) {
        String message = quizAttemptService.updateQuizAttemptStatus(id, status);
        if ("Quiz attempt status updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if ("The quiz attempt with that id does not exist".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // F1
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getQuizAttemptCountsByStatus() {
        return new ResponseEntity<>(quizAttemptService.getQuizAttemptCountsByStatus(), HttpStatus.OK);
    }

    // F2
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDayOfInstructor(
            @RequestParam UUID instructorId, @RequestParam String date) {
        LocalDate day = LocalDate.parse(date);
        String message = quizAttemptService.cancelDayOfInstructor(instructorId, day);
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

   
    @GetMapping("/page")
    public ResponseEntity<?> getQuizAttemptsPage(
            @RequestParam int page, @RequestParam int size, @RequestParam String sort) {
        if (page < 0 || size < 1) {
            return new ResponseEntity<>("Page must be 0 or more and size must be at least 1",
                    HttpStatus.BAD_REQUEST);
        }


        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        return new ResponseEntity<>(quizAttemptService.getQuizAttemptsPage(pageable), HttpStatus.OK);
    }
}
