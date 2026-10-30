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
import org.springframework.web.bind.annotation.RestController;

import kigali.learning.rw.domain.Course;
import kigali.learning.rw.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCourse(@RequestBody Course course) {
        String message = courseService.saveCourse(course);
        if ("Course saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if (message.endsWith("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable UUID id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            return new ResponseEntity<>(course.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The course with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable UUID id, @RequestBody Course course) {
        String message = courseService.updateCourse(id, course);
        if ("Course updated successfully".equals(message)) {
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
    public ResponseEntity<String> deleteCourse(@PathVariable UUID id) {
        String message = courseService.deleteCourse(id);
        if ("Course deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

  
    @GetMapping("/free")
    public ResponseEntity<List<Course>> getFreeCourses() {
        return new ResponseEntity<>(courseService.getFreeCourses(), HttpStatus.OK);
    }
}
