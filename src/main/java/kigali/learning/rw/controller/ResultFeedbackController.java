package kigali.learning.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.learning.rw.domain.ResultFeedback;
import kigali.learning.rw.service.ResultFeedbackService;

@RestController
@RequestMapping("/api/result-feedbacks")
public class ResultFeedbackController {

    private final ResultFeedbackService resultFeedbackService;

    public ResultFeedbackController(ResultFeedbackService resultFeedbackService) {
        this.resultFeedbackService = resultFeedbackService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveResultFeedback(
            @RequestParam UUID quizAttemptId, @RequestBody ResultFeedback resultFeedback) {
        String message = resultFeedbackService.saveResultFeedback(quizAttemptId, resultFeedback);
        if ("Result feedback saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  
    @GetMapping("/learner/{learnerNumber}")
    public ResponseEntity<List<ResultFeedback>> getResultFeedbacksOfLearner(
            @PathVariable String learnerNumber) {
        return new ResponseEntity<>(
                resultFeedbackService.getResultFeedbacksOfLearner(learnerNumber), HttpStatus.OK);
    }

  
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteResultFeedback(@PathVariable UUID id) {
        String message = resultFeedbackService.deleteResultFeedback(id);
        if ("Result feedback deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
