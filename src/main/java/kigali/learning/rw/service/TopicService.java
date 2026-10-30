package kigali.learning.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.learning.rw.domain.Topic;
import kigali.learning.rw.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public String saveTopic(Topic topic) {
        String problem = validate(topic);
        if (problem != null) {
            return problem;
        }

        String name = topic.getName().trim();
        if (topicRepository.existsByNameIgnoreCase(name)) {
            return "Topic with that name already exists";
        }

        Topic newTopic = new Topic();
        newTopic.setName(name);
        topicRepository.save(newTopic);
        return "Topic saved successfully";
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(UUID id) {
        return topicRepository.findById(id);
    }

    public String updateTopic(UUID id, Topic updatedTopic) {
        Optional<Topic> existingTopic = topicRepository.findById(id);
        if (existingTopic.isEmpty()) {
            return "The topic with that id does not exist";
        }

        String problem = validate(updatedTopic);
        if (problem != null) {
            return problem;
        }

        String name = updatedTopic.getName().trim();
        if (topicRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            return "Topic with that name already exists";
        }

        Topic topic = existingTopic.get();
        topic.setName(name);
        topicRepository.save(topic);
        return "Topic updated successfully";
    }

    public String deleteTopic(UUID id) {
        Optional<Topic> existingTopic = topicRepository.findById(id);
        if (existingTopic.isEmpty()) {
            return "The topic with that id does not exist";
        }

        topicRepository.deleteById(id);
        return "Topic deleted successfully";
    }

    // Returns a message when the data is not acceptable, or null when it is fine.
    private String validate(Topic topic) {
        if (topic.getName() == null || topic.getName().isBlank()) {
            return "Topic name is required";
        }
        return null;
    }
}
