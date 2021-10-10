package nl.cryptotalk.backend.Repositories;

import nl.cryptotalk.backend.entities.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    /**
     * get a post by its id
     * @param id id of the post
     * @return returns the post
     */
    Post findPostById(Long id);
}
