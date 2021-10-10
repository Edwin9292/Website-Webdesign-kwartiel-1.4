package nl.cryptotalk.backend.controllers;


import nl.cryptotalk.backend.entities.Post;
import nl.cryptotalk.backend.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    /**
     * get a Post by id
     * @param id id to search on
     * @return returns the post if it has been found or throws an error if it does not exist
     */
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id){
        Post post = this.postService.getPostById(id);
        if (post == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with id " + id + " found in the database!");
        }
        else{
            return post;
        }
    }

    // De POSTmapping staat in de topic controler

    /**
     * Update a post
     * @param id the id of the post to update
     * @param post the updated post
     * @return returns the updated post
     */
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post){
        if(post.getId() != id){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id of the given Post does not match the path id.");
        }
        if(!this.postService.postExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Post with this id has been found to update.");
        }
        return this.postService.savePost(post);
    }

    /**
     * Delete a post
     * @param id the id of the post to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id){
        Post post = getPostById(id);
        this.postService.deletePost(post);
    }


}
