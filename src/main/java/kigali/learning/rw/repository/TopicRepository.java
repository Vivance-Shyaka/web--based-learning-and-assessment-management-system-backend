package kigali.learning.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.learning.rw.domain.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);


    @Query("SELECT t FROM Topic t WHERE t.instructors IS EMPTY")
    List<Topic> findUnusedTopics();
}
