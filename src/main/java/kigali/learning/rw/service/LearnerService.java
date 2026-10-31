package kigali.learning.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.learning.rw.domain.Learner;
import kigali.learning.rw.repository.InstructorRepository;
import kigali.learning.rw.repository.LearnerRepository;

@Service
public class LearnerService {

    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;

    public LearnerService(LearnerRepository learnerRepository, InstructorRepository instructorRepository) {
        this.learnerRepository = learnerRepository;
        this.instructorRepository = instructorRepository;
    }

    public String saveLearner(Learner learner) {
        String problem = validate(learner);
        if (problem != null) {
            return problem;
        }

        String learnerNumber = learner.getLearnerNumber().trim();
        if (learnerRepository.existsByLearnerNumber(learnerNumber)) {
            return "Learner with that learner number already exists";
        }

        Learner newLearner = new Learner();
        newLearner.setFirstName(learner.getFirstName().trim());
        newLearner.setLastName(learner.getLastName().trim());
        newLearner.setLearnerNumber(learnerNumber);
        newLearner.setEmail(learner.getEmail());
        learnerRepository.save(newLearner);
        return "Learner saved successfully";
    }

    public List<Learner> getAllLearners() {
        return learnerRepository.findAll();
    }

    public Optional<Learner> getLearnerById(UUID id) {
        return learnerRepository.findById(id);
    }

    public String updateLearner(UUID id, Learner updatedLearner) {
        Optional<Learner> existingLearner = learnerRepository.findById(id);
        if (existingLearner.isEmpty()) {
            return "The learner with that id does not exist";
        }

        String problem = validate(updatedLearner);
        if (problem != null) {
            return problem;
        }

        String learnerNumber = updatedLearner.getLearnerNumber().trim();
        if (learnerRepository.existsByLearnerNumberAndIdNot(learnerNumber, id)) {
            return "Learner with that learner number already exists";
        }

        Learner learner = existingLearner.get();
        learner.setFirstName(updatedLearner.getFirstName().trim());
        learner.setLastName(updatedLearner.getLastName().trim());
        learner.setLearnerNumber(learnerNumber);
        learner.setEmail(updatedLearner.getEmail());
        learnerRepository.save(learner);
        return "Learner updated successfully";
    }

    public String deleteLearner(UUID id) {
        Optional<Learner> existingLearner = learnerRepository.findById(id);
        if (existingLearner.isEmpty()) {
            return "The learner with that id does not exist";
        }

        learnerRepository.deleteById(id);
        return "Learner deleted successfully";
    }

    // Returns a message when the data is not acceptable, or null when it is fine.
    private String validate(Learner learner) {
        if (learner.getFirstName() == null || learner.getFirstName().isBlank()) {
            return "Learner first name is required";
        }
        if (learner.getLastName() == null || learner.getLastName().isBlank()) {
            return "Learner last name is required";
        }
        if (learner.getLearnerNumber() == null || learner.getLearnerNumber().isBlank()) {
            return "Learner number is required";
        }
        if (learner.getEmail() != null && !learner.getEmail().matches(EMAIL_PATTERN)) {
            return "Learner email is not valid";
        }
        return null;
    }

    // B1
    public List<Learner> getLearnersByLastName(String lastName) {
        return learnerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4: empty Optional means the instructor does not exist
    public Optional<List<Learner>> getLearnersOfInstructor(UUID instructorId) {
        if (instructorRepository.findById(instructorId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(learnerRepository.findLearnersOfInstructor(instructorId));
    }
}
