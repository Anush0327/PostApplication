package com.prodapt.learningspring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.CommentRecord;
import com.prodapt.learningspring.entity.Post;

public interface CommentCRUDRepository extends CrudRepository<CommentRecord,Integer>{
    List<CommentRecord> findAllByPost(Post post);
}
