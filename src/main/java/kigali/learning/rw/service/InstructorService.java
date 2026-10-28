package kigali.learning.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.learning.rw.domain.Course;
import kigali.learning.rw.domain.Instructor;
import kigali.learning.rw.domain.Topic;
import kigali.learning.rw.repository.CourseRepository;
import kigali.learning.rw.repository.InstructorRepository;
import kigali.learning.rw.repository.TopicRepository;

@Service
public class InstructorService {

    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;

    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            TopicRepository topicRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public String saveInstructor(Instructor instructor) {
        String problem = validate(instructor);
        if (problem != null) {
            return problem;
        }

        if (instructor.getEmail() != null && instructorRepository.existsByEmailIgnoreCase(instructor.getEmail())) {
            return "Instructor with that email already exists";
        }

        Course course = null;
        if (instructor.getCourse() != null && instructor.getCourse().getId() != null) {
            Optional<Course> requestedCourse = courseRepository.findById(instructor.getCourse().getId());
            if (requestedCourse.isEmpty()) {
                return "The course with that id does not exist";
            }
            course = requestedCourse.get();
        }

        List<Topic> topics = new ArrayList<>();
        if (instructor.getTopics() != null) {
            for (Topic requestedTopic : instructor.getTopics()) {
                if (requestedTopic.getId() == null) {
                    return "That topic does not exist";
                }
                Optional<Topic> topic = topicRepository.findById(requestedTopic.getId());
                if (topic.isEmpty()) {
                    return "That topic does not exist";
                }
                if (!topics.contains(topic.get())) {
                    topics.add(topic.get());
                }
            }
        }

        Instructor newInstructor = new Instructor();
        newInstructor.setFirstName(instructor.getFirstName().trim());
        newInstructor.setLastName(instructor.getLastName().trim());
        newInstructor.setEmail(instructor.getEmail());
        newInstructor.setCourse(course);
        newInstructor.setTopics(topics);
        instructorRepository.save(newInstructor);
        return "Instructor saved successfully";
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(UUID id) {
        return instructorRepository.findById(id);
    }

    public String updateInstructor(UUID id, Instructor updatedInstructor) {
        Optional<Instructor> existingInstructor = instructorRepository.findById(id);
        if (existingInstructor.isEmpty()) {
            return "The instructor with that id does not exist";
        }

        String problem = validate(updatedInstructor);
        if (problem != null) {
            return problem;
        }

        if (updatedInstructor.getEmail() != null
                && instructorRepository.existsByEmailIgnoreCaseAndIdNot(updatedInstructor.getEmail(), id)) {
            return "Instructor with that email already exists";
        }

        Instructor instructor = existingInstructor.get();
        instructor.setFirstName(updatedInstructor.getFirstName().trim());
        instructor.setLastName(updatedInstructor.getLastName().trim());
        instructor.setEmail(updatedInstructor.getEmail());
        instructorRepository.save(instructor);
        return "Instructor updated successfully";
    }

    public String deleteInstructor(UUID id) {
        Optional<Instructor> existingInstructor = instructorRepository.findById(id);
        if (existingInstructor.isEmpty()) {
            return "The instructor with that id does not exist";
        }

        instructorRepository.deleteById(id);
        return "Instructor deleted successfully";
    }
 //also heere you have to return a messge  if data is not correct 
    private String validate(Instructor instructor) {
        if (instructor.getFirstName() == null || instructor.getFirstName().isBlank()) {
            return "Instructor first name is required";
        }
        if (instructor.getLastName() == null || instructor.getLastName().isBlank()) {
            return "Instructor last name is required";
        }
        if (instructor.getEmail() != null && !instructor.getEmail().matches(EMAIL_PATTERN)) {
            return "Instructor email is not valid";
        }
        return null;
    }
}
