package kigali.learning.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.learning.rw.domain.Learner;
import kigali.learning.rw.service.LearnerService;

@RestController
@RequestMapping("/api/learners")
public class LearnerController {

    private final LearnerService learnerService;

    public LearnerController(LearnerService learnerService) {
        this.learnerService = learnerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveLearner(@RequestBody Learner learner) {
        String message = learnerService.saveLearner(learner);
        if ("Learner saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if (message.endsWith("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Learner>> getAllLearners() {
        return new ResponseEntity<>(learnerService.getAllLearners(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLearnerById(@PathVariable UUID id) {
        Optional<Learner> learner = learnerService.getLearnerById(id);
        if (learner.isPresent()) {
            return new ResponseEntity<>(learner.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The learner with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateLearner(@PathVariable UUID id, @RequestBody Learner learner) {
        String message = learnerService.updateLearner(id, learner);
        if ("Learner updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.endsWith("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLearner(@PathVariable UUID id) {
        String message = learnerService.deleteLearner(id);
        if ("Learner deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    // B1
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Learner>> getLearnersByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(learnerService.getLearnersByLastName(lastName), HttpStatus.OK);
    }

    // C4
    @GetMapping("/of-instructor/{instructorId}")
    public ResponseEntity<?> getLearnersOfInstructor(@PathVariable UUID instructorId) {
        Optional<List<Learner>> learners = learnerService.getLearnersOfInstructor(instructorId);
        if (learners.isPresent()) {
            return new ResponseEntity<>(learners.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The instructor with that id does not exist", HttpStatus.NOT_FOUND);
    }
}
