package com.prodapt.learningspring.entity;

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
public class Post {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;
  
  private String content;
  
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  private LocalDateTime timeStamp;

}
