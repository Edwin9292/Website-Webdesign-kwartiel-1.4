package nl.cryptotalk.backend.services;


import nl.cryptotalk.backend.Repositories.PostRepository;
import nl.cryptotalk.backend.entities.Post;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public Post getPostById(Long id) {
        return this.postRepository.findPostById(id);
    }

    public boolean postExists(Long id) {
        return this.postRepository.existsById(id);
    }

    public Post savePost(Post post) {
        return this.postRepository.save(post);
    }

    public void deletePost(Post post) {
        this.postRepository.delete(post);
    }
}
