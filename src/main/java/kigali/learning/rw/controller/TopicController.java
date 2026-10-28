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

import kigali.learning.rw.domain.Topic;
import kigali.learning.rw.service.TopicService;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTopic(@RequestBody Topic topic) {
        String message = topicService.saveTopic(topic);
        if ("Topic saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        if (message.endsWith("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Topic>> getAllTopics() {
        return new ResponseEntity<>(topicService.getAllTopics(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable UUID id) {
        Optional<Topic> topic = topicService.getTopicById(id);
        if (topic.isPresent()) {
            return new ResponseEntity<>(topic.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The topic with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTopic(@PathVariable UUID id, @RequestBody Topic topic) {
        String message = topicService.updateTopic(id, topic);
        if ("Topic updated successfully".equals(message)) {
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
    public ResponseEntity<String> deleteTopic(@PathVariable UUID id) {
        String message = topicService.deleteTopic(id);
        if ("Topic deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
