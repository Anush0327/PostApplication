package com.prodapt.learningspring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CommentRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String comment;
    
    @ManyToOne
    @JoinColumn(name = "commenter_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_comment_id")
    private CommentRecord parentComment;

    private LocalDateTime timeStamp;

}