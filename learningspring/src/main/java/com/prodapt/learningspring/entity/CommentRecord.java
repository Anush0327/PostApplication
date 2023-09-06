package com.prodapt.learningspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CommentRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;
    
    @ManyToOne
    @JoinColumn(name = "commenter_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private Post post;

}