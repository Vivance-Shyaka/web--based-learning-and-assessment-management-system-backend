package kigali.learning.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.learning.rw.domain.Course;
import kigali.learning.rw.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public String saveCourse(Course course) {
        String problem = validate(course);
        if (problem != null) {
            return problem;
        }

        if (courseRepository.existsByCourseNumber(course.getCourseNumber())) {
            return "Course with that course number already exists";
        }

        Course newCourse = new Course();
        newCourse.setCourseNumber(course.getCourseNumber());
        newCourse.setName(course.getName().trim());
        courseRepository.save(newCourse);
        return "Course saved successfully";
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(UUID id) {
        return courseRepository.findById(id);
    }

    public String updateCourse(UUID id, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isEmpty()) {
            return "The course with that id does not exist";
        }

        String problem = validate(updatedCourse);
        if (problem != null) {
            return problem;
        }

        if (courseRepository.existsByCourseNumberAndIdNot(updatedCourse.getCourseNumber(), id)) {
            return "Course with that course number already exists";
        }

        Course course = existingCourse.get();
        course.setCourseNumber(updatedCourse.getCourseNumber());
        course.setName(updatedCourse.getName().trim());
        courseRepository.save(course);
        return "Course updated successfully";
    }

    public String deleteCourse(UUID id) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isEmpty()) {
            return "The course with that id does not exist";
        }

        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }

    // It have to Reply a message niba data ushyizemo bidakunze
    private String validate(Course course) {
        if (course.getCourseNumber() <= 0) {
            return "Course number must be greater than zero";
        }
        if (course.getName() == null || course.getName().isBlank()) {
            return "Course name is required";
        }
        return null;
    }
}
