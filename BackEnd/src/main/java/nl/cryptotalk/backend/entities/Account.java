package nl.cryptotalk.backend.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String username;

    @Column
    private int age;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String investorType;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Topic> createdTopics;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getInvestorType() {
        return investorType;
    }

    public List<Topic> getCreatedTopics() {
        return createdTopics;
    }

    public Account(){
        this.createdOn = LocalDateTime.now();
    }

}
