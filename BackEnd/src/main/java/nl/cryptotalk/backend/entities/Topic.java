package nl.cryptotalk.backend.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountID;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String topicTitle;

    @Column(columnDefinition="Text")
    private String topicText;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> replyPosts;

    public Topic(){
        this.createdOn = LocalDateTime.now();
    }

    public List<Post> getReplyPosts(){
        return this.replyPosts;
    }

    public Long getId(){
        return this.id;
    }

    public Long getAccountID(){
        return this.accountID;
    }

    public LocalDateTime getCreatedOn(){
        return this.createdOn;
    }

    public String getTopicTitle(){
        return this.topicTitle;
    }

    public String getTopicText(){
        return this.topicText;
    }

    public Post addPost(Post post){
        this.replyPosts.add(post);
        return post;
    }


}
