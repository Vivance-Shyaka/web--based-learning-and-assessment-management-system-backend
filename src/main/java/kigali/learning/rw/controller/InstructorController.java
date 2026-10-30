package kigali.learning.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import kigali.learning.rw.domain.Instructor;
import kigali.learning.rw.service.InstructorService;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveInstructor(@RequestBody Instructor instructor) {
        String message = instructorService.saveInstructor(instructor);
        if ("Instructor saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if (message.endsWith("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        return new ResponseEntity<>(instructorService.getAllInstructors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable UUID id) {
        Optional<Instructor> instructor = instructorService.getInstructorById(id);
        if (instructor.isPresent()) {
            return new ResponseEntity<>(instructor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The instructor with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInstructor(@PathVariable UUID id, @RequestBody Instructor instructor) {
        String message = instructorService.updateInstructor(id, instructor);
        if ("Instructor updated successfully".equals(message)) {
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
    public ResponseEntity<String> deleteInstructor(@PathVariable UUID id) {
        String message = instructorService.deleteInstructor(id);
        if ("Instructor deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

   
    @GetMapping("/by-topic")
    public ResponseEntity<List<Instructor>> getInstructorsByTopic(@RequestParam String name) {
        return new ResponseEntity<>(instructorService.getInstructorsByTopic(name), HttpStatus.OK);
    }

   
    @PutMapping("/{instructorId}/topics/{topicId}")
    public ResponseEntity<String> addTopicToInstructor(
            @PathVariable UUID instructorId, @PathVariable UUID topicId) {
        String message = instructorService.addTopicToInstructor(instructorId, topicId);
        if ("Topic added to the instructor".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if ("The instructor already has that topic".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

  
    @PatchMapping("/{instructorId}/course")
    public ResponseEntity<String> changeInstructorCourse(
            @PathVariable UUID instructorId, @RequestParam int courseNumber) {
        String message = instructorService.changeInstructorCourse(instructorId, courseNumber);
        if ("Course changed successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if ("That course is taken by another instructor".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
