package nl.cryptotalk.backend.controllers;


import nl.cryptotalk.backend.entities.Post;
import nl.cryptotalk.backend.entities.Topic;
import nl.cryptotalk.backend.services.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/topics")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * get a list of topics sorted by moment of creation in descending order
     * @param topicTitle if entered, it will filter the results by the provided topic title
     * @return returns a list of topics, can be empty
     */
    @GetMapping
    public List<Topic> getAllTopics(@RequestParam(required = false) String topicTitle){
       if(topicTitle != null){
           return this.topicService.getTopicsByTitle(topicTitle);
       }
       else{
           return this.topicService.getAllTopics();
       }
    }

    /**
     * get a list of the newest topics
     * @param amount max amount of topics to get
     * @return returns a list of topics, can be empty
     */
    @GetMapping("/newest")
    public List<Topic> getNewestTopics(@RequestParam int amount){
        return this.topicService.getNewestTopics(amount);
    }

    /**
     * get a topic by topic id
     * @param id id of the topic to get
     * @return returns the found topic or throws an error if it does not exist
     */
    @GetMapping("/{id}")
    public Topic getTopicById(@PathVariable Long id){
        Topic topic = this.topicService.getTopicById(id);
        if(topic == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No topic with this id exists");
        }
        else{
            return topic;
        }
    }

    /**
     * get a list of all reply posts of a topic
     * @param id id of the topic
     * @return returns a list of posts from the topic, list can be empty
     */
    @GetMapping("/{id}/posts")
    public List<Post> getAllPostsByTopicId(@PathVariable Long id){
        Topic topic = getTopicById(id);
        return topic.getReplyPosts();
    }

    /**
     * add a new Topic to the database
     * @param topic topic to add
     * @return returns the topic that has been added
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topic addTopic(@RequestBody Topic topic){
        if(topic.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please do not provide an ID");
        }
        return this.topicService.saveTopic(topic);
    }

    /**
     * add a Post to a specific topic
     * @param id the id of the topic
     * @param post the post to add to the topic
     * @return returns the updated topic
     */
    @PostMapping ("/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Topic createPostForTopic(@PathVariable Long id, @RequestBody Post post){
        Topic topic = getTopicById(id);

        if(post.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please do not provide an ID for the post");
        }
        return this.topicService.addPostToTopic(topic, post);
    }

    /**
     * update a topic
     * @param id id of the topic to update
     * @param topic the updated topic
     * @return returns the updated topic
     */
    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable Long id, @RequestBody Topic topic){
        if(topic.getId() != id){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id of the given Topic does not match the path id.");
        }
        if(!this.topicService.topicExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Topic with id " + id + " has been found to update.");
        }
        return this.topicService.saveTopic(topic);
    }

    /**
     * delete a topic by id
     * @param id id of the topic to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id){
        Topic topic = getTopicById(id);

        this.topicService.deleteTopic(topic);
    }


}
