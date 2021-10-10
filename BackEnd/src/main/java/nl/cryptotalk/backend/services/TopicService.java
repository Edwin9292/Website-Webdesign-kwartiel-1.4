package nl.cryptotalk.backend.services;

import nl.cryptotalk.backend.Repositories.TopicRepository;
import nl.cryptotalk.backend.entities.Post;
import nl.cryptotalk.backend.entities.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics(){
        return this.topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return this.topicRepository.findTopicById(id);
    }

    public Topic saveTopic(Topic topic) {
        return this.topicRepository.save(topic);
    }

    public void deleteTopic(Topic topic) {
        this.topicRepository.delete(topic);
    }

    public List<Topic> getTopicsByTitle(String topicTitle) {
        return this.topicRepository.getTopicsByTitle(topicTitle);
    }

    public boolean topicExists(Long id) {
        return this.topicRepository.existsById(id);
    }

    public List<Topic> getNewestTopics(int amount){
        return this.topicRepository.getNewestTopics(amount);
    }

    /**
     * add a topic to a post and update it in the database
     * @param topic topic to update
     * @param post post to add to the topic
     * @return returns the updated topic
     */
    public Topic addPostToTopic(Topic topic, Post post) {
        topic.addPost(post);
        return this.topicRepository.save(topic);
    }
}
