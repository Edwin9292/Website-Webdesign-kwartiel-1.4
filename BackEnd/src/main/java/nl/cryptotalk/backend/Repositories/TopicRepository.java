package nl.cryptotalk.backend.Repositories;

import nl.cryptotalk.backend.entities.Post;
import nl.cryptotalk.backend.entities.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

    /**
     * Get a list of all topics, ordered by when they are created in descending order
     * @return returns a list of topics, can be empty
     */
    @Query("select t from Topic t order by t.createdOn desc")
    List<Topic> findAll();

    /**
     * get a topic by id
     * @param id id of the topic to find
     * @return returns the topic
     */
    Topic findTopicById(Long id);

    /**
     * get a list of topics that contain the parameter in the topic title
     * @param topicTitle the title to search on
     * @return returns a list of topics, can be empty
     */
    @Query("select t from Topic t where t.topicTitle like %:topicTitle% order by t.createdOn desc")
    List<Topic> getTopicsByTitle(@Param("topicTitle") String topicTitle);

    /**
     * get a list of the newest topics, does not include topics where createdOn is null, ordered by createdOn in descending order
     * @param amount max amount of topics to return
     * @return returns a list of newest topics, list can be empty
     */
    @Query(value = "select * from Topic t where t.created_on is not null order by t.created_on desc LIMIT :amount", nativeQuery = true)
    List<Topic> getNewestTopics(@Param("amount") int amount);
}