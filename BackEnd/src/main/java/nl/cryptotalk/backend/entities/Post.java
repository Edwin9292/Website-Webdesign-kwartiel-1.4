package nl.cryptotalk.backend.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountID;

    @Column
    private LocalDateTime createdOn;

    @Column(columnDefinition="Text")
    private String text;

    public Post(){
        this.createdOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getAccountID(){ return this.accountID; }

    public LocalDateTime getCreatedOn(){ return this.createdOn; }

    public String getText(){ return this.text; }


}
